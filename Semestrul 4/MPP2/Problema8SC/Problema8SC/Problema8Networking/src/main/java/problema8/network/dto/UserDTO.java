package problema8.network.dto;

import java.io.Serializable;

public class UserDTO implements Serializable {

    private String username;
    private String password;

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "UserDTO[" + username + " " + password + "]";
    }
}
