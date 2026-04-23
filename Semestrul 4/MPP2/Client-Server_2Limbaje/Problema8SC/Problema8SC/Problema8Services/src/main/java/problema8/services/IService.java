package problema8.services;

import problema8.model.*;

import java.util.List;
import java.util.Map;

public interface IService {
    void login(User user, IObserver client) throws ProbExceptions;
    Iterable<AgeGroup> getAgeGroups()throws ProbExceptions;
    Iterable<Event> getEvents()throws ProbExceptions;
    Iterable<Event> getEventsByAgeGroup(Long ageGroupId)throws ProbExceptions;
    void saveChildAndEnrollment(String nume, String cnp, Event eventName1)throws ProbExceptions;
    Iterable<Child> getChildrenByEvent(Long id)throws ProbExceptions;
    void logout(User user, IObserver client) throws ProbExceptions;
    Iterable<Child> getChildren() throws ProbExceptions;
    int getNumberOfEvents(Long id) throws ProbExceptions;
    List<ChildDetails> getDetailsForAllChildren() throws ProbExceptions;
}
