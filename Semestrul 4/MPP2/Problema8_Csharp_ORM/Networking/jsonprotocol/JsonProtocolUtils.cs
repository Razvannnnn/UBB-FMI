using Networking.dto;

namespace Networking.jsonprotocol;

using Problema8SC_CSharp.Model;
using Problema8SC_CSharp.Services;

public static class JsonProtocolUtils
{
    public static Response CreateErrorResponse(string message)
    {
        var response = new Response
        {
            Type = ResponseType.ERROR,
            ErrorMessage = message
        };
        return response;
    }

    public static Response CreateOkResponse()
    {
        var response = new Response
        {
            Type = ResponseType.OK
        };
        return response;
    }

    public static Response CreateUserLoggedInResponse(User user)
    {
        var response = new Response
        {
            Type = ResponseType.USER_LOGGED_IN,
            User = DTOUtils.GetDTO(user)
        };
        return response;
    }

    public static Response CreateUserLoggedOutResponse(User user)
    {
        var response = new Response
        {
            Type = ResponseType.USER_LOGGED_OUT,
            User = DTOUtils.GetDTO(user)
        };
        return response;
    }

    public static Request CreateLoginRequest(User user)
    {
        var request = new Request
        {
            Type = RequestType.LOGIN,
            User = DTOUtils.GetDTO(user)
        };
        return request;
    }

    public static Request CreateGetAgeGroupsRequest()
    {
        var request = new Request
        {
            Type = RequestType.GET_AGE_GROUPS
        };
        return request;
    }

    public static Response CreateGetAgeGroupsResponse(IEnumerable<AgeGroup> ageGroups)
    {
        var response = new Response
        {
            Type = ResponseType.SEND_AGE_GROUPS,
            Data = ageGroups
        };
        return response;
    }

    public static Request CreateGetEventsRequest()
    {
        var request = new Request
        {
            Type = RequestType.GET_EVENTS
        };
        return request;
    }

    public static Response CreateGetEventsResponse(IEnumerable<Event> events)
    {
        var response = new Response
        {
            Type = ResponseType.SEND_EVENTS,
            Data = events
        };
        return response;
    }

    public static Request CreateGetEventsByAgeGroupRequest(long ageGroupId)
    {
        var request = new Request
        {
            Type = RequestType.GET_EVENTS_BY_AGE_GROUP,
            AgeGroupId = ageGroupId
        };
        return request;
    }

    public static Response CreateGetEventsByAgeGroupResponse(IEnumerable<Event> events)
    {
        var response = new Response
        {
            Type = ResponseType.SEND_EVENTS_BY_AGE_GROUP,
            Data = events
        };
        return response;
    }

    public static Request CreateSaveChildAndEnrollmentRequest(string nume, string cnp, Event eventName1)
    {
        var request = new Request
        {
            Type = RequestType.SAVE_CHILD_AND_ENROLLMENT,
            Nume = nume,
            Cnp = cnp,
            EventName1 = eventName1
        };
        return request;
    }

    public static Request CreateGetChildrenByEventRequest(long id)
    {
        var request = new Request
        {
            Type = RequestType.GET_CHILDREN_BY_EVENT,
            Id = id
        };
        return request;
    }

    public static Response CreateGetChildrenByEventResponse(IEnumerable<Child> childrenByEvent)
    {
        var response = new Response
        {
            Type = ResponseType.SEND_CHILDREN_BY_EVENT,
            Data = childrenByEvent
        };
        return response;
    }

    public static Request CreateLogoutRequest(User user)
    {
        var request = new Request
        {
            Type = RequestType.LOGOUT,
            User = DTOUtils.GetDTO(user)
        };
        return request;
    }

    public static Response CreateSaveChildAndEnrollmentResponse(Child child, Enrollment enrollment)
    {
        var response = new Response
        {
            Type = ResponseType.SEND_CHILD_AND_ENROLLMENT,
            Data = new ChildEnrollDTO(child, enrollment)
        };
        return response;
    }

    public static Response CreateChildEnrolledResponse()
    {
        var response = new Response
        {
            Type = ResponseType.CHILD_ENROLLED,
        };
        return response;
    }

    public static Request CreateGetChildrensRequest()
    {
        var request = new Request
        {
            Type = RequestType.GET_CHILDREN
        };
        return request;
    }

    public static Response CreateGetChildrenResponse(IEnumerable<Child> children)
    {
        var response = new Response
        {
            Type = ResponseType.SEND_CHILDREN,
            Data = children
        };
        return response;
    }

    public static Request CreateDetailsForAllChildrenRequest()
    {
        var request = new Request
        {
            Type = RequestType.GET_DETAILS_FOR_ALL_CHILDREN
        };
        return request;
    }

    public static Response CreateGetDetailsForAllChildrenResponse(List<ChildDetails> children)
    {
        var response = new Response
        {
            Type = ResponseType.SEND_DETAILS_FOR_ALL_CHILDREN,
            Data = children
        };
        return response;
    }
}
