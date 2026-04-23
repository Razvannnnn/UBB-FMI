package mpp.hibernate.example.book;

import mpp.hibernate.example.book.Book;
import mpp.hibernate.example.chat.ChatMessage;
import mpp.hibernate.example.chat.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;



import java.util.List;

public class MainBook {
    static SessionFactory sessionFactory;
    public static void main(String[] args) {
        System.out.println("Starting Hibernate6 Example - Book");
         sessionFactory = new Configuration()
                .addAnnotatedClass(Book.class)
                .buildSessionFactory();


        Book b=new Book("Test Book 2025 a");
        // persist an entity
      /*  sessionFactory.inTransaction(session -> {
            session.persist(b);
            System.out.println("Book added 1 "+b);
        //    session.persist(b);
         //   System.out.println("Book added 2 "+b);
        });*/
        /* sessionFactory.inTransaction(session -> {
            session.persist(b);
            System.out.println("Book added 2 "+b);
            //    session.persist(b);
            //   System.out.println("Book added 2 "+b);
        });*/
        addBook(b);

        System.out.println("Cautam cartea cu id 1 "+findBook(1));

        update(1);
        // query data using HQL
       /* sessionFactory.inSession(session -> {
            List<Book> books=session.createQuery("from Book", Book.class).getResultList();
            for(Book bb:books)
                System.out.println(bb);
        });*/
        for(Book bb:getBooks())
            System.out.println(bb);

        //delete an entity
    /*   sessionFactory.inTransaction(session->{
            session.remove(9);
        });*/

      //  delete(b);
       sessionFactory.close();

    }

    static void addBook(Book b){
        sessionFactory.inTransaction(session -> {
            session.persist(b);
            System.out.println("Book added "+b);
        });
    }

    static Book findBook(int id){
        try(Session session=sessionFactory.openSession()){
           return session.find(Book.class,id);
        }catch (Exception e){
            System.err.println("Eroare la findBook " +e);
        }
        return null;
    }

    static Book update(int id){
        Book b=findBook(id);
        b.setTitle("New Title e 2025");
       sessionFactory.inTransaction(session->{
           session.merge(b);
       });
        System.out.println("After update "+b);
        return b;
    }

    static void delete(Book book){
        sessionFactory.inTransaction(session -> {
            session.remove(book);
        });
    }

    static List<Book> getBooks(){
        try( Session session= sessionFactory.openSession()) {
            return session.createQuery("from Book where title like 'Test %'", Book.class).
                    setFirstResult(0).
                    setMaxResults(5).list();
        }
    }
}
