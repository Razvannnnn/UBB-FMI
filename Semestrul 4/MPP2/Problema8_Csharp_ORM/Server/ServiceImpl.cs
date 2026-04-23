using log4net;
using Persistence;
using Problema8SC_CSharp.Model;
using Problema8SC_CSharp.Services;
using Problema8SC_CSharp.Persistence;

namespace Server;

public class ServiceImpl : IServices
{
    private const int DefaultThreadsNo = 3;
    private static readonly ILog logger = LogManager.GetLogger(typeof(ServiceImpl));
    private readonly IDictionary<long, IObserver> loggedUsers;
    private readonly IRepoAgeGroup repoAgeGroup;
    private readonly IRepoChild repoChild;
    private readonly IRepoEnrollment repoEnrollment;
    private readonly IRepoEvent repoEvent;
    private readonly IRepoUser repoUser;

    public ServiceImpl(IRepoAgeGroup repoAgeGroup, IRepoUser repoUser, IRepoChild repoChild, IRepoEvent repoEvent,
        IRepoEnrollment repoEnrollment)
    {
        this.repoAgeGroup = repoAgeGroup;
        this.repoUser = repoUser;
        this.repoChild = repoChild;
        this.repoEvent = repoEvent;
        this.repoEnrollment = repoEnrollment;
        loggedUsers = new Dictionary<long, IObserver>();
    }

    public void Login(User user, IObserver client)
    {
        User userOk = repoUser.Login(user.GetUsername(), user.GetPassword());
        if (userOk != null)
        {
            if(loggedUsers.ContainsKey(userOk.Id)) 
                throw new ProbExceptions("User is already logged in.");
            loggedUsers[userOk.Id] = client;
            logger.Info($"User {user} logged in.");
        }
        else
        {
            throw new ProbExceptions("Authentication failed.");
        }
    }

    public void Logout(User user, IObserver client)
    {
        User loggedUser = repoUser.Login(user.GetUsername(), user.GetPassword());
        long userId = loggedUser.GetId();
        IObserver localClient = loggedUsers[userId];
        if (localClient == null) throw new ProbExceptions("User is not logged in.");
        loggedUsers.Remove(userId);
        logger.Info($"User {user} logged out.");
    }

    public IEnumerable<Child> GetChildrens()
    {
        var children = repoChild.FindAll();
        if (children != null) return children;
        throw new ProbExceptions("No children found.");
    }

    public List<ChildDetails> GetDetailsForAllChildren()
    {
        var detailsList = new List<ChildDetails>();
        var children = repoChild.FindAll();

        if (children != null)
        {
            try
            {
                foreach (var child in children)
                {
                    int age = AgeConverter.GetAgeFromCNP(child.CNP);
                    long eventCount = repoEnrollment.GetNumberOfEvents(child.GetId());

                    detailsList.Add(new ChildDetails(0, child.Name, age, eventCount));
                }
            } 
            catch (ProbExceptions e)
            {
                logger.Error($"Error getting details for children: {e}");
                throw;
            }
        }
        return detailsList;
    }


    private void NotifyObservers(Child child, Enrollment enrollment)
    {
        logger.Debug($"Child enrolled: {child}");
        foreach (var kvp in loggedUsers)
        {
            var observer = kvp.Value;
            if (observer != null)
            {
                Task.Run(() =>
                {
                    try
                    {
                        logger.Debug($"Notifying user: {observer}");
                        observer.SaveChildAndEnrollment(child, enrollment);
                    }
                    catch (ProbExceptions e)
                    {
                        logger.Error($"Error notifying user: {e}");
                    }
                });
            }
        }
    }


    public IEnumerable<AgeGroup> GetAgeGroups()
    {
        var ageGroups = repoAgeGroup.FindAll();
        if (ageGroups != null) return ageGroups;
        throw new ProbExceptions("No age groups found.");
    }

    public IEnumerable<Event> GetEvents()
    {
        var events = repoEvent.FindAll();
        if (events != null) return events;
        throw new ProbExceptions("No events found.");
    }

    public IEnumerable<Event> GetEventsByAgeGroup(long ageGroupId)
    {
        var events = repoEvent.GetEventsByAgeGroup(ageGroupId);
        if (events != null) return events;
        throw new ProbExceptions("No events found for this age group.");
    }

    public void SaveChildAndEnrollment(string name, string cnp, Event eventName)
    {
        var child = new Child(0, name, cnp);
        repoChild.Save(child);
        child = repoChild.FindOneCNP(child.CNP);
        var enrollment = new Enrollment(0, child, eventName);
        repoEnrollment.Save(enrollment);
        NotifyObservers(child, enrollment);
    }

    public IEnumerable<Child> GetChildrenByEvent(long id)
    {
        var children = repoChild.FindByEvent(id);
        if (children != null) return children;

        throw new ProbExceptions("No children found for this event.");
    }
}