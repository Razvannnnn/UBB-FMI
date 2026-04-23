package problema8.services;

import problema8.model.Child;
import problema8.model.Enrollment;
import problema8.model.User;

import java.util.List;

public interface IObserver {
    void userLoggedIn(User user) throws ProbExceptions;
    void userLoggedOut(User user) throws ProbExceptions;
    void getChildrenByEvent(List<Child> childrens) throws ProbExceptions;
    void saveChildAndEnrollment(Child child, Enrollment enrollment) throws ProbExceptions;
}
