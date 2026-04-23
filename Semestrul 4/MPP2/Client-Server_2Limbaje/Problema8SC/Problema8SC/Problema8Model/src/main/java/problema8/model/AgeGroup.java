package problema8.model;

import java.util.Objects;

public class AgeGroup extends Entity<Long> {
    private String Name;
    private int MinAge;
    private int MaxAge;

    public AgeGroup(Long id, String name, int minAge, int maxAge) {
        setId(id);
        this.Name = name;
        this.MinAge = minAge;
        this.MaxAge = maxAge;
    }

    public AgeGroup(String name, int minAge, int maxAge) {
        this.Name = name;
        this.MinAge = minAge;
        this.MaxAge = maxAge;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public int getMinAge() {
        return MinAge;
    }

    public void setMinAge(int minAge) {
        this.MinAge = minAge;
    }

    public int getMaxAge() {
        return MaxAge;
    }

    public void setMaxAge(int maxAge) {
        this.MaxAge = maxAge;
    }

    public Tuple<Integer, Integer> getAgeRange() {
        return new Tuple<>(MinAge, MaxAge);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgeGroup ageGroup = (AgeGroup) o;
        return MinAge == ageGroup.MinAge && MaxAge == ageGroup.MaxAge && Objects.equals(Name, ageGroup.Name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, MinAge, MaxAge);
    }

    @Override
    public String toString() {
        return "AgeGroup{" +
                "name='" + Name + '\'' +
                ", minAge=" + MinAge +
                ", maxAge=" + MaxAge +
                '}';
    }
}
