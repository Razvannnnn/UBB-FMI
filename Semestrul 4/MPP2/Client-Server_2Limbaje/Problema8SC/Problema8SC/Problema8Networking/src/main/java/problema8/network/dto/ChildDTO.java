package problema8.network.dto;

import java.io.Serializable;

public class ChildDTO implements Serializable {
    private String Name;
    private String CNP;

    public ChildDTO(String name, String CNP) {
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

    @Override
    public String toString() {
        return "ChildDTO[" + Name + " " + CNP + "]";
    }
}
