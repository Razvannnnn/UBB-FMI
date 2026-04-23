package iss.parkingapp.repository;

import iss.parkingapp.domain.Rezervare;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public class HiberRepoRezervare implements IRepoRezervare {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void add(Rezervare entity) {
        logger.info("Adding reservation: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> session.persist(entity));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting reservation with id: {}", id);
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            Rezervare rezervare = session.find(Rezervare.class, id);
            if (rezervare != null) {
                session.remove(rezervare);
                logger.info("Reservation deleted successfully with id: {}", id);
            } else {
                logger.warn("No reservation found with id: {}", id);
            }
        });
    }

    @Override
    public void update(Rezervare entity) {
        logger.info("Updating reservation: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> session.merge(entity));
    }

    @Override
    public Rezervare findOne(Long id) {
        logger.info("Finding reservation by id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Rezervare.class, id);
        }
    }

    @Override
    public ObservableList<Rezervare> findAll() {
        logger.info("Finding all reservations");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Rezervare> rezervari = session.createQuery("from Rezervare", Rezervare.class).getResultList();
            return FXCollections.observableArrayList(rezervari);
        }
    }

    @Override
    public int size() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("select count(r) from Rezervare r", Long.class).getSingleResult();
            return count.intValue();
        }
    }

    @Override
    public ObservableList<Rezervare> getAllRezervariForUser(Utilizator user) {
        logger.info("Getting all active reservations for user: {}", user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Rezervare> rezervari = session.createQuery(
                            "from Rezervare r where r.id_utilizator = :userId and r.status = 'active'", Rezervare.class)
                    .setParameter("userId", user.getId_utilizator())
                    .getResultList();
            return FXCollections.observableArrayList(rezervari);
        }
    }

    @Override
    public void updateExpiredRezervari() {
        logger.info("Updating expired reservations");
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            int updated = session.createQuery(
                            "update Rezervare r set r.status = 'expired' " +
                                    "where r.data_sfarsit_rezervare < :now and r.status = 'active'")
                    .setParameter("now", LocalDateTime.now())
                    .executeUpdate();
            if (updated > 0) {
                logger.info("Expired reservations updated successfully, rows affected: {}", updated);
            } else {
                logger.info("No expired reservations to update");
            }
        });
    }

    @Override
    public ObservableList<Rezervare> getHistoryRezervariUser(Utilizator user) {
        logger.info("Getting reservation history for user: {}", user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Rezervare> history = session.createQuery(
                            "from Rezervare r where r.id_utilizator = :userId", Rezervare.class)
                    .setParameter("userId", user.getId_utilizator())
                    .getResultList();
            return FXCollections.observableArrayList(history);
        }
    }
}
