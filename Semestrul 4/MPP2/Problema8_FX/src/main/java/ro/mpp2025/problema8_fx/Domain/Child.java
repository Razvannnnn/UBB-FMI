package ro.mpp2025.problema8_fx.Domain;

import ro.mpp2025.problema8_fx.Utils.AgeConverter;

import java.util.Objects;

public class Child extends Entity<Long> {

    private String name;
    private String CNP;

    public Child(Long aLong, String name, String CNP) {
        setId(aLong);
        this.name = name;
        this.CNP = CNP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(name, child.name) && Objects.equals(CNP, child.CNP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, CNP);
    }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                ", CNP='" + CNP + '\'' +
                '}';
    }
}
