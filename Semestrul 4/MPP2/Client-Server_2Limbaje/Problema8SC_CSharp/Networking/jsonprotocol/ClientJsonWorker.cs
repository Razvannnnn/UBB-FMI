using Networking.dto;

namespace Networking.jsonprotocol;

using System;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Text.Json;
using System.Collections.Generic;
using log4net;

using Problema8SC_CSharp.Model;
using Problema8SC_CSharp.Services;

public class ClientJsonWorker : IObserver
{
    private IServices server;
    private TcpClient connection;
    private NetworkStream stream;
    private volatile bool connected;
    private static readonly ILog log = LogManager.GetLogger(typeof(ClientJsonWorker));

    private static Response okResponse = JsonProtocolUtils.CreateOkResponse();

    public ClientJsonWorker(IServices server, TcpClient connection)
    {
        this.server = server;
        this.connection = connection;
        try
        {
            stream = connection.GetStream();
            connected = true;
        }
        catch (Exception e)
        {
            log.Error(e.StackTrace);
        }
    }

    public void Run()
    {
        using StreamReader reader = new StreamReader(stream, Encoding.UTF8);
        while (connected)
        {
            try
            {
                string requestJson = reader.ReadLine();
                if (string.IsNullOrEmpty(requestJson))
                {
                    continue;
                }
                log.Debug($"Received JSON request: {requestJson}");
                Request request = JsonSerializer.Deserialize<Request>(requestJson);
                Response response = HandleRequest(request);
                if (response != null)
                {
                    SendResponse(response);
                }
            }
            catch (Exception e)
            {
                log.Error($"Run error: {e.Message}");
                if (e.InnerException != null)
                    log.Error($"Inner error: {e.InnerException.Message}");
                log.Error(e.StackTrace);
                connected = false;
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
            log.Error($"Closing connection error: {e.Message}");
        }
    }

    private Response HandleRequest(Request request)
    {
        Response response = null;
        if (request.Type == 2)
        {
            log.Debug("Login request...");
            User user = DTOUtils.GetFromDTO(request.User);
            try
            {
                lock (server)
                {
                    server.Login(user, this);
                }
                return okResponse;
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 3)
        {
            log.Debug("Logout request...");
            User user = DTOUtils.GetFromDTO(request.User);
            try
            {
                lock (server)
                {
                    server.Logout(user, this);
                }
                connected = false;
                return okResponse;
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 4)
        {
            log.Debug("Get age groups request...");
            try
            {
                var groups = server.GetAgeGroups();
                return JsonProtocolUtils.CreateGetAgeGroupsResponse(groups);
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 5)
        {
            log.Debug("Get events request...");
            try
            {
                var events = server.GetEvents();
                return JsonProtocolUtils.CreateGetEventsResponse(events);
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 6)
        {
            log.Debug("Get events by age group request...");
            try
            {
                var events = server.GetEventsByAgeGroup(request.AgeGroupId);
                return JsonProtocolUtils.CreateGetEventsByAgeGroupResponse(events);
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 7)
        {
            log.Debug("Save child and enrollment request...");
            try
            {
                server.SaveChildAndEnrollment(request.Nume, request.Cnp, request.EventName1);
                Child child = new Child(0, request.Nume, request.Cnp);
                Enrollment enrollment = new Enrollment(0, child, request.EventName1);
                
                return JsonProtocolUtils.CreateSaveChildAndEnrollmentResponse(child, enrollment);
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 8)
        {
            log.Debug("Get children by event request...");
            try
            {
                var children = server.GetChildrenByEvent(request.Id);
                return JsonProtocolUtils.CreateGetChildrenByEventResponse(children);
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 1)
        {
            log.Debug("Get children request...");
            try
            {
                var children = server.GetChildrens();
                return JsonProtocolUtils.CreateGetChildrenResponse(children);
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }
        if (request.Type == 0)
        {
            log.Debug("Get details for all children request...");
            try
            {
                var children = server.GetDetailsForAllChildren();
                return JsonProtocolUtils.CreateGetDetailsForAllChildrenResponse(children);
            }
            catch (ProbExceptions e)
            {
                connected = false;
                return JsonProtocolUtils.CreateErrorResponse(e.Message);
            }
        }

        return response;
    }

    private void SendResponse(Response response)
    {
        string jsonString = JsonSerializer.Serialize(response);
        log.Debug($"Sending response: {jsonString}");
        lock (stream)
        {
            byte[] data = Encoding.UTF8.GetBytes(jsonString + "\n"); // newline to separate messages
            stream.Write(data, 0, data.Length);
            stream.Flush();
        }
    }
    
    public void UserLoggedIn(User user)
    {
        log.Debug($"User logged in: {user}");
        try
        {
            SendResponse(JsonProtocolUtils.CreateUserLoggedInResponse(user));
        }
        catch (Exception e)
        {
            log.Error($"Error notifying user logged in: {e.Message}");
        }
    }

    public void UserLoggedOut(User user)
    {
        log.Debug($"User logged out: {user}");
        try
        {
            SendResponse(JsonProtocolUtils.CreateUserLoggedOutResponse(user));
        }
        catch (Exception e)
        {
            log.Error($"Error notifying user logged out: {e.Message}");
        }
    }

    public void GetChildrenByEvent(List<Child> childrens)
    {
        log.Debug($"Update children by event: {childrens}");
        try
        {
            SendResponse(JsonProtocolUtils.CreateGetChildrenByEventResponse(childrens));
        }
        catch (Exception e)
        {
            log.Error($"Error sending children list: {e.Message}");
        }
    }

    public void SaveChildAndEnrollment(Child child, Enrollment enrollment)
    {
        log.Debug($"Child enrolled: {child}");
        try
        {
            SendResponse(JsonProtocolUtils.CreateChildEnrolledResponse());
        }
        catch (Exception e)
        {
            log.Error($"Error sending enrollment: {e.Message}");
        }
    }
}
