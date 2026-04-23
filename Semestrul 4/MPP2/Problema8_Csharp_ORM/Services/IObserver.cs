using Problema8SC_CSharp.Model;

namespace Problema8SC_CSharp.Services;

public interface IObserver
{
    void UserLoggedIn(User user);
    void UserLoggedOut(User user);
    void GetChildrenByEvent(List<Child> children);
    void SaveChildAndEnrollment(Child child, Enrollment enrollment);

}