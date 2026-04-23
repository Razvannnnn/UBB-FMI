package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest extends RequestBase {
    @JsonProperty("username")
    private String username;

    public LoginRequest(){}
    public LoginRequest(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
