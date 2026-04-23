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

public class ChatClientJsonWorker:  IChatObserver 
{
    private IChatServices server;
		private TcpClient connection;

		private NetworkStream stream;
		private volatile bool connected;
		private static readonly ILog log = LogManager.GetLogger(typeof(ChatClientJsonWorker));
		public ChatClientJsonWorker(IChatServices server, TcpClient connection)
		{
			this.server = server;
			this.connection = connection;
			try
			{
				
				stream=connection.GetStream();
				connected=true;
			}
			catch (Exception e)
			{
                log.Error(e.StackTrace);
			}
		}

		public virtual void run()
		{
			using StreamReader reader = new StreamReader(stream, Encoding.UTF8);
			while(connected)
			{
				try
				{
					string requestJson = reader.ReadLine(); // Read JSON line-by-line
					if (string.IsNullOrEmpty(requestJson)) continue;
					log.DebugFormat("Received json request {0}",requestJson);
					Request request = JsonSerializer.Deserialize<Request>(requestJson);
					log.DebugFormat("Deserializaed Request {0} ",request);
                  	Response response =handleRequest(request);
					if (response!=null)
					{
					   sendResponse(response);
					}
				}
				catch (Exception e)
				{
					log.ErrorFormat("run eroare {0}",e.Message);
					if (e.InnerException!=null)
						log.ErrorFormat("run inner error {0}",e.InnerException.Message);
                    log.Error(e.StackTrace);
				}
				
				try
				{
					Thread.Sleep(1000);
				}
				catch (Exception e)
				{
                    log.Error(e.StackTrace);
				}
			}
			try
			{
				stream.Close();
				connection.Close();
			}
			catch (Exception e)
			{
				log.Error("Error "+e);
			}
		}
		public virtual void messageReceived(Message message)
		{
			log.DebugFormat("Message received  {0}",message);
			try
			{
				sendResponse(JsonProtocolUtils.createNewMessageResponse(message));
			}
			catch (Exception e)
			{
				throw new ChatException("Sending error: "+e);
			}
		}

		public virtual void friendLoggedIn(User friend)
		{
			log.DebugFormat("Friend logged in {0}", friend);
			try
			{
				sendResponse(JsonProtocolUtils.createFriendLoggedInResponse(friend));
			}
			catch (Exception e)
			{
                log.Error(e.StackTrace);
			}
		}
	public virtual void friendLoggedOut(User friend)
		{	
			log.DebugFormat("Friend logged out {0}",friend);
			try
			{
				sendResponse(JsonProtocolUtils.createFriendLoggedOutResponse(friend));
			}
			catch (Exception e)
			{
                log.Error(e.StackTrace);
			}
		}
	private static Response okResponse=JsonProtocolUtils.createOkResponse();

	private Response handleRequest(Request request)
		{
			Response response =null;
			if (request.Type==RequestType.LOGIN)
			{
				log.Debug("Login request ...");
				User user =DTOUtils.getFromDTO(request.User);
				user.Password = TextUtils.SimpleDecode(user.Password);
				try
                {
                    lock (server)
                    {
                        server.login(user, this);
                    }
					return okResponse;
				}
				catch (ChatException e)
				{
					connected=false;
					return JsonProtocolUtils.createErrorResponse(e.Message);
				}
			}
			if (request.Type==RequestType.LOGOUT)
			{
				log.Debug("Logout request");
				
				User user =DTOUtils.getFromDTO(request.User);
				try
				{
                    lock (server)
                    {

                        server.logout(user, this);
                    }
					connected=false;
					return okResponse;

				}
				catch (ChatException e)
				{
				   return JsonProtocolUtils.createErrorResponse(e.Message);
				}
			}
			if (request.Type==RequestType.SEND_MESSAGE)
			{
				log.Debug("SendMessageRequest ...");
				Message message =DTOUtils.getFromDTO(request.Message);
				try
				{
                    lock (server)
                    {
                        server.sendMessage(message);
                    }
                        return okResponse;
				}
				catch (ChatException e)
				{
					return JsonProtocolUtils.createErrorResponse(e.Message);
				}
			}

			if (request.Type==RequestType.GET_LOGGED_FRIENDS)
			{
				log.Debug("GetLoggedFriends Request ...");
				User user =DTOUtils.getFromDTO(request.User);
				try
				{
                    User[] friends;
                    lock (server)
                    {

                         friends = server.getLoggedFriends(user);
                    }
					return JsonProtocolUtils.createGetLoggedFriendsResponse(friends);
				}
				catch (ChatException e)
				{
					return JsonProtocolUtils.createErrorResponse(e.Message);
				}
			}
			return response;
		}

	private void sendResponse(Response response)
		{
			//de modificat pentru json
			String jsonString=JsonSerializer.Serialize(response);
			log.DebugFormat("sending response {0}",jsonString);
			lock (stream)
			{
				byte[] data = Encoding.UTF8.GetBytes(jsonString + "\n"); // Append newline 
				stream.Write(data, 0, data.Length);
				stream.Flush();
			}

		}
}