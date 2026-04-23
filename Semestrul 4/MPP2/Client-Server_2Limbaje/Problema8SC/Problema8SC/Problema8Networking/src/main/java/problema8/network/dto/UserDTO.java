package problema8.network.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDTO implements Serializable {

    @SerializedName("Username")
    private String Username;

    @SerializedName("Password")
    private String Password;

    public UserDTO(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    @Override
    public String toString() {
        return "UserDTO[" + Username + " " + Password + "]";
    }
}
