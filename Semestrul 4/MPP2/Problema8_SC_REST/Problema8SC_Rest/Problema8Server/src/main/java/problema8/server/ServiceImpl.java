package problema8.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import problema8.model.*;
import problema8.persistence.irepository.IRepoAgeGroup;
import problema8.persistence.irepository.IRepoEnrollment;
import problema8.persistence.irepository.IRepoEvent;
import problema8.persistence.irepository.IRepoUser;
import problema8.persistence.irepository.IRepoChild;
import problema8.persistence.jdbc.*;
import problema8.services.IObserver;
import problema8.services.IService;

import problema8.services.ProbExceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements IService {
    private final IRepoAgeGroup repoAgeGroup;
    private final IRepoUser repoUser;
    private final IRepoChild repoChild;
    private final IRepoEvent repoEvent;
    private final IRepoEnrollment repoEnrollment;
    private Map<Long, IObserver> loggedUsers;
    private static Logger logger = LogManager.getLogger(ServiceImpl.class);

    public ServiceImpl(IRepoAgeGroup repoAgeGroup, IRepoUser repoUser, IRepoChild repoChild, IRepoEvent repoEvent, IRepoEnrollment repoEnrollment) {
        this.repoAgeGroup = repoAgeGroup;
        this.repoUser = repoUser;
        this.repoChild = repoChild;
        this.repoEvent = repoEvent;
        this.repoEnrollment = repoEnrollment;
        loggedUsers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void login(User user, IObserver client) throws ProbExceptions {
        User loggedUser = repoUser.login(user.getUsername(), user.getPassword());
        if (loggedUser!=null){
            if(loggedUsers.get(loggedUser.getId())!=null)
                throw new ProbExceptions("User already logged in.");
            loggedUsers.put(loggedUser.getId(), client);
        }else
            throw new ProbExceptions("Authentication failed.");
    }

    @Override
    public void logout(User user, IObserver client) throws ProbExceptions {
        User loggedUser = repoUser.login(user.getUsername(), user.getPassword());
        Long userId = loggedUser.getId();
        IObserver localClient = loggedUsers.remove(userId);
        if (localClient == null)
            throw new ProbExceptions("User " + user.getId() + " is not logged in.");
    }

    @Override
    public Iterable<Child> getChildren() throws ProbExceptions {
        Iterable<Child> children = repoChild.findAll();
        if (children != null) {
            return children;
        } else {
            throw new ProbExceptions("No children found.");
        }
    }

    @Override
    public int getNumberOfEvents(Long id) throws ProbExceptions {
        int numberOfEvents = repoEnrollment.getNumberOfEvents(id);
        if (numberOfEvents != 0) {
            return numberOfEvents;
        } else {
            throw new ProbExceptions("No events found for this child.");
        }
    }

    @Override
    public List<Map<String, Object>> getDetailsForAllChildren() throws ProbExceptions {
        List<Map<String, Object>> detailsList = new ArrayList<>();
        Iterable<Child> children = repoChild.findAll();

        if (children != null) {
            for (Child child : children) {
                int age = AgeConverter.getAgeFromCNP(child.getCNP());
                long eventCount = repoEnrollment.getNumberOfEvents(child.getId());

                Map<String, Object> data = new HashMap<>();
                data.put("name", child.getName());
                data.put("age", age);
                data.put("numberOfEvents", eventCount);

                detailsList.add(data);
            }
        } else {
            logger.warn("No children found for details view.");
        }
        return detailsList;
    }

    private final int defaultThreadsNo=3;

    private void notifyObservers(Child child, Enrollment enrollment) {
        logger.debug("Child enrolled "+child);
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for (IObserver observer : loggedUsers.values()) {
            executor.execute(() -> {
                try {
                    logger.debug("Notifying user "+observer);
                    System.out.println("Notifying user " + observer);
                    observer.saveChildAndEnrollment(child, enrollment); // -> clientjsonworker
                } catch (ProbExceptions e) {
                    logger.error("Error notifying user " + e);
                }
            });
        }
        executor.shutdown();
    }


    public Iterable<AgeGroup> getAgeGroups() throws ProbExceptions {
        Iterable<AgeGroup> ageGroups = repoAgeGroup.findAll();
        if (ageGroups != null) {
            return ageGroups;
        } else {
            throw new ProbExceptions("No age groups found.");
        }
    }

    public Iterable<Event> getEvents() throws ProbExceptions{
        Iterable<Event> events = repoEvent.findAll();
        if (events != null) {
            return events;
        } else {
            throw new ProbExceptions("No events found.");
        }
    }

    public Iterable<Event> getEventsByAgeGroup(Long ageGroupId) throws ProbExceptions{
        Iterable<Event> events = repoEvent.findByAgeGroup(ageGroupId);
        if (events != null) {
            return events;
        } else {
            throw new ProbExceptions("No events found for this age group.");
        }
    }

    @Override
    public void saveChildAndEnrollment(String nume, String cnp, Event eventName1) throws ProbExceptions{
        Child child = new Child(null, nume, cnp);
        repoChild.save(child);
        child = repoChild.findOneCNP(child.getCNP());
        Enrollment enrollment = new Enrollment(null, child, eventName1);
        repoEnrollment.save(enrollment);
        notifyObservers(child, enrollment);
    }

    public Iterable<Child> getChildrenByEvent(Long id) throws ProbExceptions{
        Iterable<Child> children = repoChild.findByEvent(id);
        if (children != null) {
            return children;
        } else {
            throw new ProbExceptions("No children found for this event.");
        }
    }
}
