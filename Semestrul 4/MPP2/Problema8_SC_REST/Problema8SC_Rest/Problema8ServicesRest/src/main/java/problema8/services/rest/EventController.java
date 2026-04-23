package problema8.services.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import problema8.model.Event;
import problema8.model.User;
import problema8.persistence.RepositoryException;
import problema8.persistence.jdbc.RepoEvent;
import problema8.persistence.jdbc.RepoUser;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/problema8/events")
public class EventController {
    private static final String template = "Hello, %s!";

    @Autowired
    private RepoEvent eventRepository;

    @RequestMapping("/greeting")
    public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }

    @RequestMapping( method= RequestMethod.GET)
    public Iterable<Event> getAll(){
        System.out.println("Get all events ...");
        return eventRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Long id){
        System.out.println("Get by id "+id);
        Event event=eventRepository.findOne(id);
        if (event==null)
            return new ResponseEntity<String>("Event not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Event>(event, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Event create(@RequestBody Event event){
        return eventRepository.saveReturnEvent(event);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Event update(@RequestBody Event event, @PathVariable Long id) {
        System.out.println("Updating event ...");
        eventRepository.update(event);
        return event;

    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Long id){
        System.out.println("Deleting event ... " + id);
        try {
            eventRepository.delete(id);
            return new ResponseEntity<Event>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete event exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/{id}/name")
    public String name(@PathVariable Long id){
        Event result = eventRepository.findOne(id);
        System.out.println("Result ..."+result);
        return result.getName();
    }


    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
