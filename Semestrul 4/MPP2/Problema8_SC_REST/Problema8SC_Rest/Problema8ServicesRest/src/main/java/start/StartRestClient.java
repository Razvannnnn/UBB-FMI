package start;

import org.springframework.web.client.RestClientException;
import problema8.model.Event;
import problema8.services.rest.ServiceException;
import rest.client.EventClient;

public class StartRestClient {
    private final static EventClient eventClient = new EventClient();

    public static void main(String[] args) {
        Event newEvent = new Event(null, "Marathon", 42, 1L);
        try {
            System.out.println("Adding a new event: " + newEvent);
            show(() -> {
                Event created = eventClient.create(newEvent);
                System.out.println("Created event: " + created);
                newEvent.setId(created.getId());
            });

            System.out.println("\nAll events:");
            show(() -> {
                Event[] events = eventClient.getAll();
                for (Event e : events) {
                    System.out.println(e.getId() + ": " + e.getName() + " (" + e.getDistance() + "km)");
                }
            });

            System.out.println("\nGetting event by ID: " + newEvent.getId());
            show(() -> {
                Event found = eventClient.getById(newEvent.getId());
                System.out.println("Found event: " + found);
            });

            newEvent.setDistance(21); // Change from marathon to half-marathon
            System.out.println("\nUpdating event distance to: " + newEvent.getDistance());
            show(() -> {
                eventClient.update(newEvent);
                System.out.println("Update successful");
            });

            // print updated
            System.out.println("\nGetting event updated: " + newEvent.getId());
            show(() -> {
                Event found = eventClient.getById(newEvent.getId());
                System.out.println("Event updated: " + found);
            });

            // Test getting by name
            System.out.println("\nGetting event name for ID: " + newEvent.getId());
            show(() -> {
                Event event = eventClient.getById(newEvent.getId());
                String name = event.getName();
                System.out.println("Event name: " + name);
            });

            // Test deleting
            System.out.println("\nDeleting event by id: " + newEvent.getId());
            show(() -> {
                eventClient.delete(newEvent);
                System.out.println("Delete successful");
            });

        } catch (RestClientException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception: " + e.getMessage());
        }
    }
}