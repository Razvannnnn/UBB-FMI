package iss.parkingapp.repository;

import iss.parkingapp.domain.Masina;
import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

public class HiberRepoMasina implements IRepoMasina {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void add(Masina entity) {
        logger.info("Adding car: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> session.persist(entity));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting car with id: {}", id);
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            Masina masina = session.find(Masina.class, id);
            if (masina != null) {
                session.remove(masina);
                logger.info("Car deleted successfully with id: {}", id);
            } else {
                logger.warn("No car found with id: {}", id);
            }
        });
    }

    @Override
    public void update(Masina entity) {
        logger.info("Updating car: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> session.merge(entity));
    }

    @Override
    public Masina findOne(Long id) {
        logger.info("Finding car by id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Masina.class, id);
        }
    }

    @Override
    public ObservableList<Masina> findAll() {
        logger.info("Finding all cars");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Masina> masini = session.createQuery("from Masina", Masina.class).getResultList();
            return FXCollections.observableArrayList(masini);
        }
    }

    @Override
    public int size() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("select count(m) from Masina m", Long.class).getSingleResult();
            return count.intValue();
        }
    }

    @Override
    public ObservableList<Masina> getAllForUser(Utilizator user) {
        logger.info("Finding all cars for user: {}", user);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Masina> masini = session.createQuery(
                            "from Masina m where m.id_utilizator = :userId", Masina.class)
                    .setParameter("userId", user.getId_utilizator())
                    .getResultList();
            return FXCollections.observableArrayList(masini);
        }
    }
}
