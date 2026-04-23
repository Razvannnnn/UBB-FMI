package ro.mpp2025.domain;

public class AgeGroup implements Entity<Integer> {
    private Integer id;
    private final String name;
    private final int minAge;
    private final int maxAge;

    public AgeGroup(String name, int minAge, int maxAge) {
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public String getName() {
        return name;
    }

    public Tuple<Integer, Integer> getAgeInterval() {
        return new Tuple<>(minAge, maxAge);
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return name + "|" + minAge + "-" + maxAge;
    }
}
