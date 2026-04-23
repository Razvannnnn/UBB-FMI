package rest.client;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import problema8.model.Event;
import problema8.services.rest.ServiceException;

import java.util.concurrent.Callable;

public class EventClient {
    public static final String URL = "http://localhost:8080/problema8/events";

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

    public Event[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Event[].class));
    }

    public Event getById(Long id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%d", URL, id), Event.class));
    }

    public Event create(Event event) {
        return execute(() -> restTemplate.postForObject(URL, event, Event.class));
    }

    public void update(Event event) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, event.getId()), event);
            return null;
        });
    }

    public void delete(Event event) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%d", URL, event.getId()));
            return null;
        });
    }
}
