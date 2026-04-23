package problema8.network.dto;

import problema8.model.Child;
import problema8.model.Enrollment;
import problema8.model.Event;

public class ChildEnrollDTO {
    private Child child;
    private Enrollment enrollment;

    public ChildEnrollDTO(Child child, Enrollment enrollment) {
        this.child = child;
        this.enrollment = enrollment;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }
}
