package problema8.network.dto;

import problema8.model.Child;
import problema8.model.Enrollment;

public class ChildEnrollDTO {
    private Child Child;
    private Enrollment Enrollment;

    public ChildEnrollDTO(Child child, Enrollment enrollment) {
        this.Child = child;
        this.Enrollment = enrollment;
    }

    public Child getChild() {
        return Child;
    }

    public void setChild(Child child) {
        this.Child = child;
    }

    public Enrollment getEnrollment() {
        return Enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.Enrollment = enrollment;
    }
}
