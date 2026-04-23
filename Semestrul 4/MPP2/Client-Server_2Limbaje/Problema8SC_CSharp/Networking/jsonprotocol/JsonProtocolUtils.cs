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
            Type = 3,
            ErrorMessage = message
        };
        return response;
    }

    public static Response CreateOkResponse()
    {
        var response = new Response
        {
            Type = 2
        };
        return response;
    }

    public static Response CreateUserLoggedInResponse(User user)
    {
        var response = new Response
        {
            Type = 4,
            User = DTOUtils.GetDTO(user)
        };
        return response;
    }

    public static Response CreateUserLoggedOutResponse(User user)
    {
        var response = new Response
        {
            Type = 5,
            User = DTOUtils.GetDTO(user)
        };
        return response;
    }

    public static Request CreateLoginRequest(User user)
    {
        var request = new Request
        {
            Type = 2,
            User = DTOUtils.GetDTO(user)
        };
        return request;
    }

    public static Request CreateGetAgeGroupsRequest()
    {
        var request = new Request
        {
            Type = 4
        };
        return request;
    }

    public static Response CreateGetAgeGroupsResponse(IEnumerable<AgeGroup> ageGroups)
    {
        var response = new Response
        {
            Type = 6,
            Data = ageGroups
        };
        return response;
    }

    public static Request CreateGetEventsRequest()
    {
        var request = new Request
        {
            Type = 5
        };
        return request;
    }

    public static Response CreateGetEventsResponse(IEnumerable<Event> events)
    {
        var response = new Response
        {
            Type = 7,
            Data = events
        };
        return response;
    }

    public static Request CreateGetEventsByAgeGroupRequest(long ageGroupId)
    {
        var request = new Request
        {
            Type = 6,
            AgeGroupId = ageGroupId
        };
        return request;
    }

    public static Response CreateGetEventsByAgeGroupResponse(IEnumerable<Event> events)
    {
        var response = new Response
        {
            Type = 8,
            Data = events
        };
        return response;
    }

    public static Request CreateSaveChildAndEnrollmentRequest(string nume, string cnp, Event eventName1)
    {
        var request = new Request
        {
            Type = 7,
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
            Type = 8,
            Id = id
        };
        return request;
    }

    public static Response CreateGetChildrenByEventResponse(IEnumerable<Child> childrenByEvent)
    {
        var response = new Response
        {
            Type = 10,
            Data = childrenByEvent
        };
        return response;
    }

    public static Request CreateLogoutRequest(User user)
    {
        var request = new Request
        {
            Type = 3,
            User = DTOUtils.GetDTO(user)
        };
        return request;
    }

    public static Response CreateSaveChildAndEnrollmentResponse(Child child, Enrollment enrollment)
    {
        var response = new Response
        {
            Type = 9,
            Data = new ChildEnrollDTO(child, enrollment)
        };
        return response;
    }

    public static Response CreateChildEnrolledResponse()
    {
        var response = new Response
        {
            Type = 11,
        };
        return response;
    }

    public static Request CreateGetChildrensRequest()
    {
        var request = new Request
        {
            Type = 1
        };
        return request;
    }

    public static Response CreateGetChildrenResponse(IEnumerable<Child> children)
    {
        var response = new Response
        {
            Type = 1,
            Data = children
        };
        return response;
    }

    public static Request CreateDetailsForAllChildrenRequest()
    {
        var request = new Request
        {
            Type = 0
        };
        return request;
    }

    public static Response CreateGetDetailsForAllChildrenResponse(List<ChildDetails> children)
    {
        var response = new Response
        {
            Type = 0,
            Data = children
        };
        return response;
    }
}
