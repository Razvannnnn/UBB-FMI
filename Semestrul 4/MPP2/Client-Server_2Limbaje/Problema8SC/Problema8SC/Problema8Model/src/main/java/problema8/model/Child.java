package problema8.model;

import java.util.Objects;

public class Child extends Entity<Long> {

    private String Name;
    private String CNP;

    public Child(Long aLong, String name, String CNP) {
        setId(aLong);
        this.Name = name;
        this.CNP = CNP;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public Integer getAge() {
        return AgeConverter.getAgeFromCNP(CNP);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Child child = (Child) o;
        return Objects.equals(Name, child.Name) && Objects.equals(CNP, child.CNP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, CNP);
    }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + Name + '\'' +
                ", CNP='" + CNP + '\'' +
                '}';
    }
}
