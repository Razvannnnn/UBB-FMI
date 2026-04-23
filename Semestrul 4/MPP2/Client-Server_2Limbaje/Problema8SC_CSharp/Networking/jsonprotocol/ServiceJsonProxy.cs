using Networking.dto;

namespace Networking.jsonprotocol;

using System;
using System.Collections.Generic;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Text.Json;
using log4net;

using Problema8SC_CSharp.Model;
using Problema8SC_CSharp.Services;

public class ServicesJsonProxy : IServices
    {
        private string host;
        private int port;

        private IObserver client;
        private NetworkStream stream;
        private TcpClient connection;
        private Queue<Response> responses;
        private volatile bool finished;
        private EventWaitHandle _waitHandle;
        private static readonly ILog logger = LogManager.GetLogger(typeof(ServicesJsonProxy));

        public ServicesJsonProxy(string host, int port)
        {
            this.host = host;
            this.port = port;
            responses = new Queue<Response>();
        }

        public void Login(User user, IObserver client)
        {
            InitializeConnection();
            // user.Password = TextUtils.SimpleEncode(user.Password);
            SendRequest(JsonProtocolUtils.CreateLoginRequest(user));
            Response response = ReadResponse();
            Console.WriteLine(response.GetType());
            if (response.Type == 2)
            {
                this.client = client;
                return;
            }
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
        }

        public void Logout(User user, IObserver client)
        {
            SendRequest(JsonProtocolUtils.CreateLogoutRequest(user));
            Response response = ReadResponse();
            CloseConnection();
            if (response.Type == 3)
            {
                throw new ProbExceptions(response.ErrorMessage);
            }
        }

        public IEnumerable<Child> GetChildrens()
        {
            SendRequest(JsonProtocolUtils.CreateGetChildrensRequest());
            Response response = ReadResponse();
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
            if (response.Type == 1)
            {
                string jsonData = JsonSerializer.Serialize(response.Data);
                var children = JsonSerializer.Deserialize<List<Child>>(jsonData);
                return children;
            }
            return null;
        }

        public List<ChildDetails> GetDetailsForAllChildren()
        {
            SendRequest(JsonProtocolUtils.CreateDetailsForAllChildrenRequest());
            Response response = ReadResponse();
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
            if (response.Type == 0)
            {
                string jsonData = JsonSerializer.Serialize(response.Data);
                var details = JsonSerializer.Deserialize<List<ChildDetails>>(jsonData);
                return details;
            }
            return null;
        }

        public IEnumerable<AgeGroup> GetAgeGroups()
        {
            SendRequest(JsonProtocolUtils.CreateGetAgeGroupsRequest());
            Response response = ReadResponse();
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
            if (response.Type == 6)
            {
                string jsonData = JsonSerializer.Serialize(response.Data);
                Console.WriteLine(JsonSerializer.Serialize(response.Data));
                var ageGroups = JsonSerializer.Deserialize<List<AgeGroup>>(jsonData);
                return ageGroups;
            }
            return null;
        }

        public IEnumerable<Event> GetEvents()
        {
            SendRequest(JsonProtocolUtils.CreateGetEventsRequest());
            Response response = ReadResponse();
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
            if (response.Type == 7)
            {
                string jsonData = JsonSerializer.Serialize(response.Data);
                var events = JsonSerializer.Deserialize<List<Event>>(jsonData);
                return events;
            }
            return null;
        }

        public IEnumerable<Event> GetEventsByAgeGroup(long ageGroupId)
        {
            InitializeConnection();
            SendRequest(JsonProtocolUtils.CreateGetEventsByAgeGroupRequest(ageGroupId));
            Response response = ReadResponse();
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
            if (response.Type == 8)
            {
                string jsonData = JsonSerializer.Serialize(response.Data);
                var events = JsonSerializer.Deserialize<List<Event>>(jsonData);
                return events;
            }
            return null;
        }

        public void SaveChildAndEnrollment(string nume, string cnp, Event eventName1)
        {
            InitializeConnection();
            SendRequest(JsonProtocolUtils.CreateSaveChildAndEnrollmentRequest(nume, cnp, eventName1));
            Response response = ReadResponse();
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
            if (response.Type == 9)
            {
                logger.Debug("Child and enrollment saved successfully");
                return;
            }
        }

        public IEnumerable<Child> GetChildrenByEvent(long id)
        {
            InitializeConnection();
            SendRequest(JsonProtocolUtils.CreateGetChildrenByEventRequest(id));
            Response response = ReadResponse();
            if (response.Type == 3)
            {
                string err = response.ErrorMessage;
                CloseConnection();
                throw new ProbExceptions(err);
            }
            if (response.Type == 10)
            {
                string jsonData = JsonSerializer.Serialize(response.Data);
                var children = JsonSerializer.Deserialize<List<Child>>(jsonData);
                return children;
            }
            return null;
        }

        private void HandleUpdate(Response response)
        {
            if (response.GetType() == 11)
            {
                try
                {
                    client.SaveChildAndEnrollment(null, null);
                }
                catch (ProbExceptions e)
                {
                    logger.Error(e);
                }
            }
        }

        private void CloseConnection()
        {
            finished = true;
            try
            {
                stream.Close();
                connection.Close();
                _waitHandle.Close();
                client = null;
            }
            catch (Exception e)
            {
                logger.Error(e);
                logger.Error(e.StackTrace);
            }
        }

        private void InitializeConnection()
        {
            try
            {
                connection = new TcpClient(host, port);
                stream = connection.GetStream();
                finished = false;
                _waitHandle = new AutoResetEvent(false);
                StartReader();
            }
            catch (Exception e)
            {
                logger.Error(e);
                logger.Error(e.StackTrace);
                throw new ProbExceptions("Error connecting to server");
            }
        }

        private void StartReader()
        {
            Thread thread = new Thread(Run);
            thread.Start();
        }

        private void SendRequest(Request request)
        {
            try
            {
                lock (stream)
                {
                    string jsonRequest = JsonSerializer.Serialize(request);
                    logger.Debug($"Sending request {jsonRequest}");
                    byte[] data = Encoding.UTF8.GetBytes(jsonRequest + "\n"); // Append newline
                    stream.Write(data, 0, data.Length);
                    stream.Flush();
                }
            }
            catch (Exception e)
            {
                throw new ProbExceptions($"Error sending object {e}");
            }
        }

        private Response ReadResponse()
        {
            Response response = null;
            try
            {
                _waitHandle.WaitOne();
                lock (responses)
                {
                    response = responses.Dequeue();
                }
            }
            catch (Exception e)
            {
                logger.Error("Reading error", e);
            }
            return response;
        }

        private bool IsUpdate(Response response)
        {
            return response.Type == 11;
        }

        public void Run()
        {
            using (StreamReader reader = new StreamReader(stream, Encoding.UTF8))
            {
                while (!finished)
                {
                    try
                    { 
                        string responseJson = reader.ReadLine();
                        if (string.IsNullOrEmpty(responseJson)) continue;
                        Response response = JsonSerializer.Deserialize<Response>(responseJson);
                        Console.WriteLine(response.Type);
                        logger.Debug($"Response received {response}");
                        if (IsUpdate(response))
                        {
                            HandleUpdate(response);
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
                        logger.Error($"Reading error {e}");
                    }
                }
            }
        }
    }