package start;

import chat.model.User;
import chat.services.rest.ServiceException;
import org.springframework.web.client.RestClientException;
import rest.client.NewUsersClient;
import rest.client.UsersClient;

public class StartRestClientClient {
    private final static NewUsersClient usersClient=new NewUsersClient();
    public static void main(String[] args) {

        User userT=new User("test2025nou","133","Test 3");
        try{
         System.out.println("Adding a new user "+userT);
            show(()-> System.out.println(usersClient.create(userT)));
            System.out.println("\nPrinting all users ...");
            show(()->{
                User[] res=usersClient.getAll();
                for(User u:res){
                    System.out.println(u.getId()+": "+u.getName());
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

        System.out.println("\nInfo for user with id=ana");
        show(()-> System.out.println(usersClient.getById("ana")));

        System.out.println("\nDeleting user with id="+userT.getId());
        show(()-> usersClient.delete(userT.getId()));
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
