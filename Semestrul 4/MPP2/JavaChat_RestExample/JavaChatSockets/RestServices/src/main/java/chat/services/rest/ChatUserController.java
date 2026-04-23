package chat.services.rest;

import chat.model.User;
import chat.persistence.RepositoryException;
import chat.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/chat/users")
public class ChatUserController {

    private static final String template = "Hello, %s!";

    @Autowired
    private UserRepository userRepository;

    @RequestMapping("/greeting")
    public  String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }


    @RequestMapping( method=RequestMethod.GET)
    public User[] getAll(){
        System.out.println("Get all users ...");
        return userRepository.getUsers();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String id){
        System.out.println("Get by id "+id);
        User user=userRepository.findBy(id);
        if (user==null)
            return new ResponseEntity<String>("User not found",HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public User create(@RequestBody User user){
            userRepository.save(user);
            return user;

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User update(@RequestBody User user, @PathVariable String id) {
            System.out.println("Updating user ...");
             userRepository.update(id,user);
             return user;

    }
   // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value="/{username}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String username){
        System.out.println("Deleting user ... "+username);
        try {
            userRepository.delete(username);
            return new ResponseEntity<User>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Ctrl Delete user exception");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/{user}/name")
    public String name(@PathVariable String user){
        User result=userRepository.findBy(user);
        System.out.println("Result ..."+result);

        return result.getName();
    }



    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
