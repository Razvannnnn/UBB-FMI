package problema8.model;

public class ChildDetails extends Entity<Long> {
    public String Name;
    public int Age;
    public long NumberOfEvents;

    public void setName(String name) {
        this.Name = name;
    }
    public String getName() {
        return Name;
    }

    public void setAge(int age) {
        this.Age = age;
    }
    public int getAge() {
        return Age;
    }

    public void setNumberOfEvents(long numberOfEvents) {
        this.NumberOfEvents = numberOfEvents;
    }
    public long getNumberOfEvents() {
        return NumberOfEvents;
    }

    public ChildDetails(Long id, String name, int age, long numberOfEvents) {
        setId(id);
        this.Name = name;
        this.Age = age;
        this.NumberOfEvents = numberOfEvents;
    }
}
