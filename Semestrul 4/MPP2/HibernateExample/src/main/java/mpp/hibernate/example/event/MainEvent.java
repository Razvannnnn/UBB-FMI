package mpp.hibernate.example.event;

import mpp.hibernate.example.book.Book;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.List;

public class MainEvent {
    public static void main(String[] args) {
        System.out.println("Starting Hibernate6 Example - Event");
        var sessionFactory = new Configuration()
                .addAnnotatedClass(Event.class)
                .buildSessionFactory();


        Event e1=new Event("Event A", new Date());
        Event e2=new Event("Event B", new Date());
        // persist an entity
        sessionFactory.inTransaction(session -> {
            session.persist(e1);
            session.persist(e2);

        });

        // query data using HQL
        sessionFactory.inSession(session -> {
            List<Event> events=session.createQuery("from Event", Event.class).getResultList();
            for(Event ee:events)
                System.out.println(ee);
        });


        sessionFactory.close();

    }
}
