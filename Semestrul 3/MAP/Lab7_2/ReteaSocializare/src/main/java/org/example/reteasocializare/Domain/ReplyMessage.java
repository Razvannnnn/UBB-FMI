package org.example.reteasocializare.Domain;

import java.time.LocalDateTime;
import java.util.List;

public class ReplyMessage extends Message {
    private Message repliedMessage;

    public ReplyMessage(String message, Long from, Long to, Message repliedMessage) {
        super(message, from, to, LocalDateTime.now());
        this.repliedMessage = repliedMessage;
    }

    public Message getRepliedMessage() {
        return repliedMessage;
    }

    public void setRepliedMessage(Message repliedMessage) {
        this.repliedMessage = repliedMessage;
    }
}
