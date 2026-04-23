package org.example.reteasocializare.Domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private Long from;
    private Long to;
    private String message;
    private LocalDateTime data;
    private Message reply;

    public Message(String message, Long from, Long to, LocalDateTime data) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.data = data;
        this.reply = null;
    }
    public Long getId() {
        return super.getId();
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
