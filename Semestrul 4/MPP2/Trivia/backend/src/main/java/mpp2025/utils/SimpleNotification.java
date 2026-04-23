package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleNotification extends ResponseBase{
    @JsonProperty("message")
    private String message;

    public SimpleNotification(){}

    public SimpleNotification(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}