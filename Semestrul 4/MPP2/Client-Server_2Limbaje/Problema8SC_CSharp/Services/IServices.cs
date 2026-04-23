using Problema8SC_CSharp.Model;

namespace Problema8SC_CSharp.Services;

public interface IServices
{
    void Login(User user, IObserver client);
    IEnumerable<AgeGroup> GetAgeGroups();
    IEnumerable<Event> GetEvents();
    IEnumerable<Event> GetEventsByAgeGroup(long ageGroupId);
    void SaveChildAndEnrollment(string name, string cnp, Event eventName1);
    IEnumerable<Child> GetChildrenByEvent(long id);
    void Logout(User user, IObserver client);
    IEnumerable<Child> GetChildrens();
    List<ChildDetails> GetDetailsForAllChildren();
}