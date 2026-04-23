package problema8.network.jsonprotocol;

import problema8.model.AgeGroup;
import problema8.model.Child;
import problema8.model.Enrollment;
import problema8.model.User;
import problema8.network.dto.AgeGroupDTO;
import problema8.network.dto.DTOUtils;
import problema8.network.dto.UserDTO;
import problema8.services.IObserver;
import problema8.services.IService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import com.google.gson.Gson;
import problema8.services.ProbExceptions;

public class ClientJsonWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    private static Logger logger = LogManager.getLogger(ClientJsonWorker.class);

    public ClientJsonWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new Gson();
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
    }

    public void run() {
        while (connected) {
            try {
                String requestLine = input.readLine();
                if (requestLine == null) {
                    logger.info("Connection closed by client.");
                    connected = false;
                    break;
                }
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
                connected = false;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
                logger.error(e.getStackTrace());
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            logger.error("Error " + e);
        }
    }


    private void sendResponse(Response response) {
        String responseLine = gsonFormatter.toJson(response);
        logger.debug("Sending response: " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    private static Response okResponse=JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request) {
        Response response = null;
        if(request.getType() == 2) {
            logger.debug("Login request...{}" + request.getUser());
            UserDTO udto = request.getUser();
            User user = DTOUtils.getFromDTO(udto);
            try {
                server.login(user, this);
                return okResponse;
            } catch (Exception e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 3) {
            logger.debug("Logout request...{}" + request.getUser());
            UserDTO udto = request.getUser();
            User user = DTOUtils.getFromDTO(udto);
            try {
                server.logout(user, this);
                connected = false;
                return okResponse;
            } catch (Exception e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 4) {
            logger.debug("Get age groups request...");
            try {
                response = JsonProtocolUtils.createGetAgeGroupsResponse(server.getAgeGroups());
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 5) {
            logger.debug("Get events request...");
            try {
                response = JsonProtocolUtils.createGetEventsResponse(server.getEvents());
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 6) {
            logger.debug("Get events by age group request...");
            try {
                response = JsonProtocolUtils.createGetEventsByAgeGroupResponse(server.getEventsByAgeGroup(request.getAge_group_id()));
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 7) {
            logger.debug("Save child and enrollment request...");
            try {
                server.saveChildAndEnrollment(request.getNume(), request.getCnp(), request.getEventName1());
                Child child = new Child(null, request.getNume(), request.getCnp());
                Enrollment enrollment = new Enrollment(null, child, request.getEventName1());
                response = JsonProtocolUtils.createSaveChildAndEnrollmentResponse(child, enrollment);
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 8) {
            logger.debug("Get children by event request...");
            try {
                response = JsonProtocolUtils.createGetChildrenByEventResponse(server.getChildrenByEvent(request.getId()));
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 1) {
            logger.debug("Get children request...");
            try {
                response = JsonProtocolUtils.createGetChildrenResponse(server.getChildren());
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType() == 9) {
            logger.debug("Get number of events request...");
            try {
                long id = request.getId();
                response = JsonProtocolUtils.createGetNumberOfEventsResponse(server.getNumberOfEvents(id));
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == 0) {
            logger.debug("Send details for all children request...");
            try {
                response = JsonProtocolUtils.createGetDetailsForAllChildrenResponse(server.getDetailsForAllChildren());
                return response;
            } catch (ProbExceptions e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public void userLoggedIn(User user) throws ProbExceptions {
        logger.debug("User logged in: " + user);
        Response response = JsonProtocolUtils.createUserLoggedInResponse(user);
        sendResponse(response);
    }

    @Override
    public void userLoggedOut(User user) throws ProbExceptions {
        logger.debug("User logged out: " + user);
        Response response = JsonProtocolUtils.createUserLoggedOutResponse(user);
        sendResponse(response);
    }

    @Override
    public void getChildrenByEvent(List<Child> childrens) throws ProbExceptions {
        logger.debug("Update children by event: " + childrens);
        Response response = JsonProtocolUtils.createGetChildrenByEventResponse(childrens);
        sendResponse(response);
    }

    @Override
    public void saveChildAndEnrollment(Child child, Enrollment enrollment) throws ProbExceptions {
        logger.debug("ClientJsonWorker - Child enrolled: " + child);
        Response response = JsonProtocolUtils.createChildEnrolledResponse();
        sendResponse(response);
    }
}
