package problema8.network.jsonprotocol;

import problema8.model.*;
import problema8.network.dto.ChildEnrollDTO;
import problema8.network.dto.DTOUtils;

import java.util.List;
import java.util.Map;

public class JsonProtocolUtils {
    public static Response createErrorResponse(String message) {
        Response response = new Response();
        response.setType(ResponseType.ERROR);
        response.setErrorMessage(message);
        return response;
    }

    public static Response createOkResponse() {
        Response response = new Response();
        response.setType(ResponseType.OK);
        return response;
    }

    public static Response createUserLoggedInResponse(User user) {
        Response response = new Response();
        response.setType(ResponseType.USER_LOGGED_IN);
        response.setUser(DTOUtils.getDTO(user));
        return response;
    }

    public static Response createUserLoggedOutResponse(User user) {
        Response response = new Response();
        response.setType(ResponseType.USER_LOGGED_OUT);
        response.setUser(DTOUtils.getDTO(user));
        return response;
    }

    public static Request createLoginRequest(User user) {
        Request request = new Request();
        request.setType(RequestType.LOGIN);
        request.setUser(DTOUtils.getDTO(user));
        return request;
    }

    public static Request createGetAgeGroupsRequest() {
        Request request = new Request();
        request.setType(RequestType.GET_AGE_GROUPS);
        return request;
    }

    public static Response createGetAgeGroupsResponse(Iterable<AgeGroup> ageGroups) {
        Response response = new Response();
        response.setType(ResponseType.SEND_AGE_GROUPS);
        response.setData(ageGroups);
        return response;
    }

    public static Request createGetEventsRequest() {
        Request request = new Request();
        request.setType(RequestType.GET_EVENTS);
        return request;
    }

    public static Response createGetEventsResponse(Iterable<Event> events) {
        Response response = new Response();
        response.setType(ResponseType.SEND_EVENTS);
        response.setData(events);
        return response;
    }

    public static Request createGetEventsByAgeGroupRequest(Long ageGroupId) {
        Request request = new Request();
        request.setType(RequestType.GET_EVENTS_BY_AGE_GROUP);
        request.setAge_group_id(ageGroupId);
        return request;
    }

    public static Response createGetEventsByAgeGroupResponse(Iterable<Event> events) {
        Response response = new Response();
        response.setType(ResponseType.SEND_EVENTS_BY_AGE_GROUP);
        response.setData(events);
        return response;
    }

    public static Request createSaveChildAndEnrollmentRequest(String nume, String cnp, Event eventName1) {
        Request request = new Request();
        request.setType(RequestType.SAVE_CHILD_AND_ENROLLMENT);
        request.setNume(nume);
        request.setCnp(cnp);
        request.setEventName1(eventName1);
        return request;
    }

    public static Request createGetChildrenByEventRequest(Long id) {
        Request request = new Request();
        request.setType(RequestType.GET_CHILDREN_BY_EVENT);
        request.setId(id);
        return request;
    }

    public static Response createGetChildrenByEventResponse(Iterable<Child> childrenByEvent) {
        Response response = new Response();
        response.setType(ResponseType.SEND_CHILDREN_BY_EVENT);
        response.setData(childrenByEvent);
        return response;
    }

    public static Request createLogoutRequest(User user) {
        Request request = new Request();
        request.setType(RequestType.LOGOUT);
        request.setUser(DTOUtils.getDTO(user));
        return request;
    }

    public static Response createSaveChildAndEnrollmentResponse(Child child, Enrollment enrollment) {
        Response response = new Response();
        response.setType(ResponseType.SEND_CHILD_AND_ENROLLMENT);
        ChildEnrollDTO childEnrollDTO = new ChildEnrollDTO(child, enrollment);
        response.setData(childEnrollDTO);
        return response;
    }

    public static Response createChildEnrolledResponse() {
        Response response = new Response();
        response.setType(ResponseType.CHILD_ENROLLED);
        return response;
    }

    public static Request createGetChildrenRequest() {
        Request request = new Request();
        request.setType(RequestType.GET_CHILDREN);
        return request;
    }

    public static Request createGetNumberOfEventsRequest(Long id) {
        Request request = new Request();
        request.setType(RequestType.GET_NUMBER_OF_EVENTS);
        request.setId(id);
        return request;
    }

    public static Response createGetChildrenResponse(Iterable<Child> children) {
        Response response = new Response();
        response.setType(ResponseType.SEND_CHILDREN);
        response.setData(children);
        return response;
    }

    public static Response createGetNumberOfEventsResponse(int numberOfEvents) {
        Response response = new Response();
        response.setType(ResponseType.SEND_NUMBER_OF_EVENTS);
        response.setData(numberOfEvents);
        return response;
    }

    public static Request createGetDetailsForAllChildrenRequest() {
        Request request = new Request();
        request.setType(RequestType.GET_DETAILS_FOR_ALL_CHILDREN);
        return request;
    }

    public static Response createGetDetailsForAllChildrenResponse(List<Map<String, Object>> children) {
        Response response = new Response();
        response.setType(ResponseType.SEND_DETAILS_FOR_ALL_CHILDREN);
        response.setData(children);
        return response;
    }
}
