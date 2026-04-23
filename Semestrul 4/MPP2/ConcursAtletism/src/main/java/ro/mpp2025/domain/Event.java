package ro.mpp2025.domain;

public class Event implements Entity<Integer> {
    private Integer id;
    private final String name;
    private final int distance;

    public Event(String name, int distance) {
        this.name = name;
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
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
        return name + "|" + distance + "m";
    }
}
