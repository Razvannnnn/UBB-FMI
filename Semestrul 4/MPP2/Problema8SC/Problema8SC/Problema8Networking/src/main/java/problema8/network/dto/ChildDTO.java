package problema8.network.dto;

import java.io.Serializable;

public class ChildDTO implements Serializable {
    private String name;
    private String CNP;

    public ChildDTO(String name, String CNP) {
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

    @Override
    public String toString() {
        return "ChildDTO[" + name + " " + CNP + "]";
    }
}
