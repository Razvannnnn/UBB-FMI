package problema8.network.dto;

import java.io.Serializable;

public class AgeGroupDTO implements Serializable {
    private String Name;
    private int MinAge;
    private int MaxAge;

    public AgeGroupDTO(String name, int minAge, int maxAge) {
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

    @Override
    public String toString() {
        return "AgeGroupDTO[" + Name + " " + MinAge + " " + MaxAge + "]";
    }
}
