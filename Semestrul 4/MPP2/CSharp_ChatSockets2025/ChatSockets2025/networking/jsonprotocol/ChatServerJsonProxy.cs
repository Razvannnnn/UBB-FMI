using System.Net.Sockets;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Formatters.Binary;
using System.Text;
using System.Text.Json;
using chat.model;
using chat.networking.dto;
using chat.networking.protocol;
using chat.services;
using log4net;

namespace chat.networking.jsonprotocol;

public class ChatServerJsonProxy: IChatServices
{
		 private string host;
		private int port;

		private IChatObserver client;
		private NetworkStream stream;
		private TcpClient connection;
		private Queue<Response> responses;
		private volatile bool finished;
        private EventWaitHandle _waitHandle;
        private static readonly ILog log = LogManager.GetLogger(typeof(ChatServerJsonProxy));
		public ChatServerJsonProxy(string host, int port)
		{
			this.host = host;
			this.port = port;
			responses=new Queue<Response>();
		}

		public virtual void login(User user, IChatObserver client)
		{
			initializeConnection();
			user.Password=TextUtils.SimpleEncode(user.Password);
			sendRequest(JsonProtocolUtils.createLoginRequest(user));
			Response response =readResponse();
			if (response.Type==ResponseType.OK)
			{
				this.client=client;
				return;
			}
			if (response.Type==ResponseType.ERROR)
			{
				String err =response.ErrorMessage;
				closeConnection();
				throw new ChatException(err);
			}
		}

		public virtual void sendMessage(Message message)
		{
			
			sendRequest(JsonProtocolUtils.createSendMessageRequest(message));
			Response response =readResponse();
			if (response.Type==ResponseType.ERROR)
			{
				throw new ChatException(response.ErrorMessage);
			}
		}

	public virtual void logout(User user, IChatObserver client)
		{
			sendRequest(JsonProtocolUtils.createLogoutRequest(user));
			Response response =readResponse();
			closeConnection();
			if (response.Type==ResponseType.ERROR)
			{
				throw new ChatException(response.ErrorMessage);
			}
		}

		public virtual User[] getLoggedFriends(User user)
		{
			sendRequest(JsonProtocolUtils.createLoggedFriendsRequest(user));
			Response response =readResponse();
			if (response.Type==ResponseType.ERROR)
			{
				throw new ChatException(response.ErrorMessage);
			}
			User[] friends =DTOUtils.getFromDTO(response.Friends);
			return friends;
		}

		private void closeConnection()
		{
			finished=true;
			try
			{
				stream.Close();
				connection.Close();
                _waitHandle.Close();
				client=null;
			}
			catch (Exception e)
			{
				Console.WriteLine(e.StackTrace);
			}
		}

		private void sendRequest(Request request)
		{
			try
			{
				lock (stream)
				{
					
					string jsonRequest = JsonSerializer.Serialize(request);
					log.DebugFormat("Sending request {0}",jsonRequest);
					byte[] data = Encoding.UTF8.GetBytes(jsonRequest + "\n"); // Append newline 
					stream.Write(data, 0, data.Length);
					stream.Flush();
					
				}
			}
			catch (Exception e)
			{
				throw new ChatException("Error sending object "+e);
			}

		}

		private Response readResponse()
		{
			Response response =null;
			try
			{
                _waitHandle.WaitOne();
				lock (responses)
				{
                    response = responses.Dequeue();
                
				}
			}catch (Exception e) {
				Console.WriteLine(e.StackTrace);
			}
			return response;
		}
		private void initializeConnection()
		{
			 try
			 {
				connection=new TcpClient(host,port);
				stream=connection.GetStream();
				finished=false;
                _waitHandle = new AutoResetEvent(false);
				startReader();
			}catch (Exception e){
                Console.WriteLine(e.StackTrace);
			}
		}
		private void startReader()
		{
			Thread tw =new Thread(run);
			tw.Start();
		}


		private void handleUpdate(Response response)
		{
			log.DebugFormat("handleUpdate called with {0}",response);
			if (response.Type==ResponseType.FRIEND_LOGGED_IN)
			{
				User friend =DTOUtils.getFromDTO(response.User);
				log.Debug("Friend logged in "+friend);
				try
				{
					client.friendLoggedIn(friend);
				}
				catch (ChatException e)
				{
                    log.Error(e.StackTrace); 
				}
			}
			if (response.Type==ResponseType.FRIEND_LOGGED_OUT)
			{
				User friend =DTOUtils.getFromDTO(response.User);
				log.Debug("Friend logged out "+friend);
				try
				{
					client.friendLoggedOut(friend);
				}
				catch (ChatException e)
				{
                    log.Error(e.StackTrace);
				}
			}

			if (response.Type==ResponseType.NEW_MESSAGE)
			{
				Message message =DTOUtils.getFromDTO(response.Message);
				try
				{
					client.messageReceived(message);
				}
				catch (ChatException e)
				{
					log.Error(e.StackTrace);
				}
			}
		}

		private bool isUpdate(Response response)
		{
			return response.Type==ResponseType.FRIEND_LOGGED_OUT||response.Type==ResponseType.FRIEND_LOGGED_IN||response.Type==ResponseType.NEW_MESSAGE;
		}
		public virtual void run()
			{
				using StreamReader reader = new StreamReader(stream, Encoding.UTF8);
				while(!finished)
				{
					try
					{
			           string responseJson = reader.ReadLine();
                        if (string.IsNullOrEmpty(responseJson)) 
	                        continue; //nu s-a citit un raspuns valid
                        Response response=JsonSerializer.Deserialize<Response>(responseJson);
						log.Debug("response received "+response);
						if (isUpdate(response))
						{
							 handleUpdate(response);
						}
						else
						{
							lock (responses)
							{
                                responses.Enqueue(response);
							}
                            _waitHandle.Set();
						}
					}
					catch (Exception e)
					{
						log.Error("Reading error "+e);
					}
					
				}
			}
		
}