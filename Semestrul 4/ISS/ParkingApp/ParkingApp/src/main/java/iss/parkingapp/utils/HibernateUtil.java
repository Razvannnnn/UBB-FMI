package iss.parkingapp.utils;

import iss.parkingapp.domain.LocParcare;
import iss.parkingapp.domain.Masina;
import iss.parkingapp.domain.Rezervare;
import iss.parkingapp.domain.Utilizator;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory();
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(Utilizator.class)
                .addAnnotatedClass(Masina.class)
                .addAnnotatedClass(LocParcare.class)
                .addAnnotatedClass(Rezervare.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}