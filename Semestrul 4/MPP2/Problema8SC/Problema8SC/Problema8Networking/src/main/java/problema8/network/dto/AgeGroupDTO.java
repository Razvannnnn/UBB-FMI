package problema8.network.dto;

import java.io.Serializable;

public class AgeGroupDTO implements Serializable {
    private String name;
    private int minAge;
    private int maxAge;

    public AgeGroupDTO(String name, int minAge, int maxAge) {
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

    @Override
    public String toString() {
        return "AgeGroupDTO[" + name + " " + minAge + " " + maxAge + "]";
    }
}
