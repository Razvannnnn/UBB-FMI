package problema8.network.jsonprotocol;

import problema8.model.*;
import problema8.network.dto.ChildEnrollDTO;
import problema8.network.dto.DTOUtils;

import java.util.List;
import java.util.Map;

public class JsonProtocolUtils {
    public static Response createErrorResponse(String message) {
        Response response = new Response();
        response.setType(3);
        response.setErrorMessage(message);
        return response;
    }

    public static Response createOkResponse() {
        Response response = new Response();
        response.setType(2);
        return response;
    }

    public static Response createUserLoggedInResponse(User user) {
        Response response = new Response();
        response.setType(4);
        response.setUser(DTOUtils.getDTO(user));
        return response;
    }

    public static Response createUserLoggedOutResponse(User user) {
        Response response = new Response();
        response.setType(5);
        response.setUser(DTOUtils.getDTO(user));
        return response;
    }

    public static Request createLoginRequest(User user) {
        Request request = new Request();
        request.setType(2);
        request.setUser(DTOUtils.getDTO(user));
        return request;
    }

    public static Request createGetAgeGroupsRequest() {
        Request request = new Request();
        request.setType(4);
        return request;
    }

    public static Response createGetAgeGroupsResponse(Iterable<AgeGroup> ageGroups) {
        Response response = new Response();
        response.setType(6);
        response.setData(ageGroups);
        return response;
    }

    public static Request createGetEventsRequest() {
        Request request = new Request();
        request.setType(5);
        return request;
    }

    public static Response createGetEventsResponse(Iterable<Event> events) {
        Response response = new Response();
        response.setType(7);
        response.setData(events);
        return response;
    }

    public static Request createGetEventsByAgeGroupRequest(Long ageGroupId) {
        Request request = new Request();
        request.setType(6);
        request.setAge_group_id(ageGroupId);
        return request;
    }

    public static Response createGetEventsByAgeGroupResponse(Iterable<Event> events) {
        Response response = new Response();
        response.setType(8);
        response.setData(events);
        return response;
    }

    public static Request createSaveChildAndEnrollmentRequest(String nume, String cnp, Event eventName1) {
        Request request = new Request();
        request.setType(7);
        request.setNume(nume);
        request.setCnp(cnp);
        request.setEventName1(eventName1);
        return request;
    }

    public static Request createGetChildrenByEventRequest(Long id) {
        Request request = new Request();
        request.setType(8);
        request.setId(id);
        return request;
    }

    public static Response createGetChildrenByEventResponse(Iterable<Child> childrenByEvent) {
        Response response = new Response();
        response.setType(10);
        response.setData(childrenByEvent);
        return response;
    }

    public static Request createLogoutRequest(User user) {
        Request request = new Request();
        request.setType(3);
        request.setUser(DTOUtils.getDTO(user));
        return request;
    }

    public static Response createSaveChildAndEnrollmentResponse(Child child, Enrollment enrollment) {
        Response response = new Response();
        response.setType(9);
        ChildEnrollDTO childEnrollDTO = new ChildEnrollDTO(child, enrollment);
        response.setData(childEnrollDTO);
        return response;
    }

    public static Response createChildEnrolledResponse() {
        Response response = new Response();
        response.setType(11);
        return response;
    }

    public static Request createGetChildrenRequest() {
        Request request = new Request();
        request.setType(1);
        return request;
    }

    public static Request createGetNumberOfEventsRequest(Long id) {
        Request request = new Request();
        request.setType(9);
        request.setId(id);
        return request;
    }

    public static Response createGetChildrenResponse(Iterable<Child> children) {
        Response response = new Response();
        response.setType(1);
        response.setData(children);
        return response;
    }

    public static Response createGetNumberOfEventsResponse(int numberOfEvents) {
        Response response = new Response();
        response.setType(12);
        response.setData(numberOfEvents);
        return response;
    }

    public static Request createGetDetailsForAllChildrenRequest() {
        Request request = new Request();
        request.setType(0);
        return request;
    }

    public static Response createGetDetailsForAllChildrenResponse(List<ChildDetails> children) {
        Response response = new Response();
        response.setType(0);
        response.setData(children);
        return response;
    }
}
