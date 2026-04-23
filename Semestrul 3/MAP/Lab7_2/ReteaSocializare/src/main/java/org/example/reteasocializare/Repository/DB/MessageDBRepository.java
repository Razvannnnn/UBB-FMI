package org.example.reteasocializare.Repository.DB;

import org.example.reteasocializare.Domain.Message;
import org.example.reteasocializare.Domain.Prietenie;
import org.example.reteasocializare.Domain.Utilizator;
import org.example.reteasocializare.Domain.Validators.Validator;
import org.example.reteasocializare.Repository.Memory.InMemoryRepository;
import org.example.reteasocializare.Repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepository extends InMemoryRepository<Long, Message> {

    private final String url;
    private final String username;
    private final String password;


    public MessageDBRepository(Validator<Message> validator, String url, String usename, String password) {
        super(validator);
        this.url = url;
        this.username = usename;
        this.password = password;
    }


    public List<Message> getMessages(Utilizator from, Utilizator to) {
        String sql = "SELECT * FROM message WHERE (id_from = ? AND id_to = ?) OR (id_from = ? AND id_to = ?) ORDER BY data";
        List<Message> messages = new ArrayList<>();
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1,from.getId());
            preparedStatement.setLong(2,to.getId());
            preparedStatement.setLong(3,to.getId());
            preparedStatement.setLong(4,from.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_message");
                Long idFr = resultSet.getLong("id_from");
                Long idT = resultSet.getLong("id_to");
                String message = resultSet.getString("msg");
                LocalDateTime dateTime = resultSet.getTimestamp("data").toLocalDateTime();
                Message msg = new Message(message, idFr, idT, dateTime);
                msg.setId(id);

                messages.add(msg);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Long addMessage(Message message) {
        String sql = "INSERT INTO message(msg, id_from, id_to, data) VALUES (?,?,?,?)";
        try(
                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        )
        {
            preparedStatement.setString(1, message.getMessage());
            preparedStatement.setLong(2, message.getFrom());
            preparedStatement.setLong(3, message.getTo());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(message.getData()));

            preparedStatement.executeUpdate();
            return message.getId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
