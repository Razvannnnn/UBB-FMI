using Problema8_FX_Csharp.Domain;
using Problema8_FX_Csharp.Repository;
using User = Microsoft.VisualBasic.ApplicationServices.User;

namespace Problema8_FX_Csharp.Service;

public class Service
{
    private IRepoAgeGroup _repoAgeGroup;
    private IRepoChild _repoChild;
    private IRepoEnrollment _repoEnrollment;
    private IRepoEvent _repoEvent;
    private IRepoUser _repoUser;
    
    public Service(IRepoAgeGroup repoAgeGroup, IRepoChild repoChild, IRepoEnrollment repoEnrollment, IRepoEvent repoEvent, IRepoUser repoUser)
    {
        _repoAgeGroup = repoAgeGroup;
        _repoChild = repoChild;
        _repoEnrollment = repoEnrollment;
        _repoEvent = repoEvent;
        _repoUser = repoUser;
    }
    
    public Problema8_FX_Csharp.Domain.User Login(String username, String password)
    {
        return _repoUser.Login(username, password);
    }
    
    public IEnumerable<AgeGroup> GetAgeGroups()
    {
        return _repoAgeGroup.FindAll();
    }

    public IEnumerable<Event> GetEvents()
    {
        return _repoEvent.FindAll();
    }
    
    public IEnumerable<Child> GetChildren()
    {
        return _repoChild.FindAll();
    }
    
    public IEnumerable<Event> GetEventsByAgeGroup(long ageGroupId)
    {
        return _repoEvent.GetEventsByAgeGroup(ageGroupId);
    }

    public void SaveChildAndEnrollment(Child child, Event eventName)
    {
        _repoChild.Save(child);
        child = _repoChild.FindOneCNP(child.CNP);
        Enrollment enrollment = new Enrollment(0, child, eventName);
        _repoEnrollment.Save(enrollment);
    }

    public void SaveChild(Child child)
    {
        if(_repoChild.FindOneCNP(child.CNP) != null) 
            throw new Exception("Child already exists");
        _repoChild.Save(child);
    }
    
    public void SaveEnrollment(Enrollment enrollment)
    {
        _repoEnrollment.Save(enrollment);
    }
    
    public IEnumerable<Child> GetChildrenByEvent(long eventId)
    {
        return _repoChild.FindByEvent(eventId);
    }

    public long GetNumberOfEvents(long id)
    {
        return _repoEnrollment.GetNumberOfEvents(id);
    }

    public Child GetChildrenByCNP(string childCnp)
    {
        return _repoChild.FindOneCNP(childCnp);
    }

    public void SaveEnrollmentAndChild(string nume, string cnp, Event eventName)
    {
        Child child = _repoChild.FindOneCNP(cnp);
        if (child == null)
        {
            child = new Child(0, nume, cnp);
            _repoChild.Save(child);
            child = _repoChild.FindOneCNP(child.CNP);
        }
        Enrollment enrollment = new Enrollment(0, child, eventName);
        _repoEnrollment.Save(enrollment);
    }
}