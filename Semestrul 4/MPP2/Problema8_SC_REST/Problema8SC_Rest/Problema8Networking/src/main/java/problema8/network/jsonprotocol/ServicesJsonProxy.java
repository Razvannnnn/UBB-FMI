package problema8.network.jsonprotocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.model.*;
import problema8.network.dto.ChildEnrollDTO;
import problema8.services.IObserver;
import problema8.services.IService;
import problema8.services.ProbExceptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesJsonProxy implements IService {
    private String host;
    private int port;

    private IObserver client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    private static Logger logger = LogManager.getLogger(ServicesJsonProxy.class);

    public ServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }


    @Override
    public void login(User user, IObserver client) throws ProbExceptions {
        initializeConnection();
        //user.setPasswd(TextUtils.simpleEncode(user.getPasswd()));
        Request req= JsonProtocolUtils.createLoginRequest(user);
        sendRequest(req);
        Response response=readResponse();
        if (response.getType()== ResponseType.OK){
            this.client=client;
            return;
        }
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();;
            closeConnection();
            throw new ProbExceptions(err);
        }
    }

    @Override
    public void logout(User user, IObserver client) throws ProbExceptions {
        Request req=JsonProtocolUtils.createLogoutRequest(user);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
    }

    @Override
    public Iterable<Child> getChildren() throws ProbExceptions {
        Request req=JsonProtocolUtils.createGetChildrenRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if(response.getType() == ResponseType.SEND_CHILDREN){
            String jsonData = gsonFormatter.toJson(response.getData());
            Type listType = new TypeToken<List<Child>>() {}.getType();
            List<Child> childrens = gsonFormatter.fromJson(jsonData, listType);
            return childrens;
        }
        return null;
    }

    @Override
    public int getNumberOfEvents(Long id) throws ProbExceptions {
        Request req=JsonProtocolUtils.createGetNumberOfEventsRequest(id);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if(response.getType() == ResponseType.SEND_NUMBER_OF_EVENTS){
            String jsonData = gsonFormatter.toJson(response.getData());
            Type listType = new TypeToken<Integer>() {}.getType();
            Integer numberOfEvents = gsonFormatter.fromJson(jsonData, listType);
            return numberOfEvents;
        }
        return 0;
    }

    @Override
    public List<Map<String, Object>> getDetailsForAllChildren() throws ProbExceptions {
        Request req = JsonProtocolUtils.createGetDetailsForAllChildrenRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if (response.getType() == ResponseType.SEND_DETAILS_FOR_ALL_CHILDREN) {
            String jsonData = gsonFormatter.toJson(response.getData());
            Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> detailsList = gsonFormatter.fromJson(jsonData, listType);
            return detailsList;
        }
        return null;
    }

    @Override
    public Iterable<AgeGroup> getAgeGroups() throws ProbExceptions {
        Request req=JsonProtocolUtils.createGetAgeGroupsRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if(response.getType() == ResponseType.SEND_AGE_GROUPS){
            String jsonData = gsonFormatter.toJson(response.getData());
            Type listType = new TypeToken<List<AgeGroup>>() {}.getType();
            List<AgeGroup> ageGroups = gsonFormatter.fromJson(jsonData, listType);
            return ageGroups;
        }
        return null;
    }

    @Override
    public Iterable<Event> getEvents() throws ProbExceptions {
        Request req=JsonProtocolUtils.createGetEventsRequest();
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if(response.getType() == ResponseType.SEND_EVENTS){
            String jsonData = gsonFormatter.toJson(response.getData());
            Type listType = new TypeToken<List<Event>>() {}.getType();
            List<Event> events = gsonFormatter.fromJson(jsonData, listType);
            return events;
        }
        return null;
    }

    @Override
    public Iterable<Event> getEventsByAgeGroup(Long ageGroupId) throws ProbExceptions {
        Request req=JsonProtocolUtils.createGetEventsByAgeGroupRequest(ageGroupId);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if(response.getType() == ResponseType.SEND_EVENTS_BY_AGE_GROUP){
            String jsonData = gsonFormatter.toJson(response.getData());
            Type listType = new TypeToken<List<Event>>() {}.getType();
            List<Event> events = gsonFormatter.fromJson(jsonData, listType);
            return events;
        }
        return null;
    }

    @Override
    public void saveChildAndEnrollment(String nume, String cnp, Event eventName1) throws ProbExceptions {
        Request req=JsonProtocolUtils.createSaveChildAndEnrollmentRequest(nume, cnp, eventName1);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if (response.getType() == ResponseType.SEND_CHILD_AND_ENROLLMENT) {
            logger.debug("Response data: " + gsonFormatter.toJson(response.getData()));
            return;
        }
    }

    @Override
    public Iterable<Child> getChildrenByEvent(Long id) throws ProbExceptions {

        Request req=JsonProtocolUtils.createGetChildrenByEventRequest(id);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR){
            String err=response.getErrorMessage();
            closeConnection();
            throw new ProbExceptions(err);
        }
        if(response.getType() == ResponseType.SEND_CHILDREN_BY_EVENT){
            String jsonData = gsonFormatter.toJson(response.getData());
            Type listType = new TypeToken<List<Child>>() {}.getType();
            List<Child> childrens = gsonFormatter.fromJson(jsonData, listType);
            return childrens;
        }
        return null;
    }

    private void handleUpdate(Response response) {
        if(response.getType() == ResponseType.CHILD_ENROLLED) {
            try {
                client.saveChildAndEnrollment(null, null);
            } catch (ProbExceptions e) {
                logger.error("Error saving child and enrollment", e);
            }
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }

    }

    private void initializeConnection() throws ProbExceptions{
        try {
            connection = new Socket(host, port);
            gsonFormatter = new Gson();
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (Exception e) {
            logger.error(e);
            logger.error(e.getStackTrace());
            throw new ProbExceptions("Error connecting to server");
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void sendRequest(Request request)throws ProbExceptions {
        String reqLine=gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new ProbExceptions("Error sending object "+e);
        }
    }

    private Response readResponse() throws ProbExceptions {
        Response response=null;
        try{
            response=qresponses.take();
        } catch (InterruptedException e) {
            logger.error(e);
            logger.error(e.getStackTrace());
        }
        return response;
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.CHILD_ENROLLED;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    String responseLine = input.readLine();
                    logger.debug("response received {}", responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);

                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            logger.error("Queue put interrupted", e);
                        }
                    }
                } catch (IOException e) {
                    logger.error("Reading error", e);
                }
            }
        }
    }
}
