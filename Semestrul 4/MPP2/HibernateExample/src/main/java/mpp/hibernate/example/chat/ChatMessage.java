package mpp.hibernate.example.chat;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "ChatMessages" )
public class ChatMessage implements mpp.hibernate.example.chat.Entity<Integer> {

    private Integer id;
    private String from, to, message;

    public ChatMessage() {
        id=0;
        from=to=message="default";
    }

    public ChatMessage(String from, String to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
    }
    @Column(name = "FromUser")
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Column(name = "ToUser")
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Column(name = "TextMessage")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }


    @Override
    @Id
    @GeneratedValue(generator="increment")
    //   @GenericGenerator(name="increment", strategy = "increment")
    public Integer getId() {
        return id;
    }

    @Override
    public String
    toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
