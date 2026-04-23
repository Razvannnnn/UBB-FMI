package rest.client;

import chat.model.User;
import chat.services.rest.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;


public class UsersClient {
    public static final String URL = "http://localhost:8080/chat/users";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public User[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, User[].class));
    }

    public User getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), User.class));
    }

    public User create(User user) {
        return execute(() -> restTemplate.postForObject(URL, user, User.class));
    }

    public void update(User user) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, user.getId()), user);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}
