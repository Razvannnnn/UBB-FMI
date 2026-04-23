package ro.mpp2025.problema8_fx.Service;

import ro.mpp2025.problema8_fx.Domain.*;
import ro.mpp2025.problema8_fx.Repository.*;

public class Service {
    private final IRepoAgeGroup repoAgeGroup;
    private final IRepoUser repoUser;
    private final IRepoChild repoChild;
    private final IRepoEvent repoEvent;
    private final IRepoEnrollment repoEnrollment;

    public Service(IRepoAgeGroup repoAgeGroup, IRepoUser repoUser, IRepoChild repoChild, IRepoEvent repoEvent, IRepoEnrollment repoEnrollment) {
        this.repoAgeGroup = repoAgeGroup;
        this.repoUser = repoUser;
        this.repoChild = repoChild;
        this.repoEvent = repoEvent;
        this.repoEnrollment = repoEnrollment;
    }

    public User login(String username, String password) {
        return repoUser.login(username, password);
    }

    public Iterable<AgeGroup> getAgeGroups() {
        return repoAgeGroup.findAll();
    }

    public Iterable<Event> getEvents() {
        return repoEvent.findAll();
    }

    public Iterable<Event> getEventsByAgeGroup(Long ageGroupId) {
        return repoEvent.findByAgeGroup(ageGroupId);
    }


    public void saveChildAndEnrollment(String nume, String cnp, Event eventName1) {
        Child child = new Child(null, nume, cnp);
        repoChild.save(child);
        child = repoChild.findOneCNP(child.getCNP());
        Enrollment enrollment = new Enrollment(null, child, eventName1);
        repoEnrollment.save(enrollment);
    }

    public Iterable<Child> getChildrenByEvent(Long id) {
        return repoChild.findByEvent(id);
    }
}
