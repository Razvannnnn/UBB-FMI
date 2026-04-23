package ro.mpp2025.problema8_fx.Domain;

import java.util.Objects;

public class AgeGroup extends Entity<Long> {
    private String name;
    private int minAge;
    private int maxAge;

    public AgeGroup(Long id, String name, int minAge, int maxAge) {
        setId(id);
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public Tuple<Integer, Integer> getAgeRange() {
        return new Tuple<>(minAge, maxAge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeGroup ageGroup = (AgeGroup) o;
        return minAge == ageGroup.minAge && maxAge == ageGroup.maxAge && Objects.equals(name, ageGroup.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, minAge, maxAge);
    }

    @Override
    public String toString() {
        return "AgeGroup{" +
                "name='" + name + '\'' +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                '}';
    }
}
