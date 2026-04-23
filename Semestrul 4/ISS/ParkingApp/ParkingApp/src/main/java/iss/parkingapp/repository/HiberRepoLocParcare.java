package iss.parkingapp.repository;

import iss.parkingapp.domain.LocParcare;
import iss.parkingapp.utils.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.List;

public class HiberRepoLocParcare implements IRepoLocParcare {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void add(LocParcare entity) {
        logger.info("Adding parking spot: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> session.persist(entity));
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting parking spot with id: {}", id);
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            LocParcare loc = session.find(LocParcare.class, id);
            if (loc != null) {
                session.remove(loc);
                logger.info("Parking spot deleted with id: {}", id);
            } else {
                logger.warn("No parking spot found with id: {}", id);
            }
        });
    }

    @Override
    public void update(LocParcare entity) {
        logger.info("Updating parking spot: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> session.merge(entity));
    }

    @Override
    public LocParcare findOne(Long id) {
        logger.info("Finding parking spot by id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(LocParcare.class, id);
        }
    }

    @Override
    public ObservableList<LocParcare> findAll() {
        logger.info("Finding all parking spots");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<LocParcare> list = session.createQuery("FROM LocParcare", LocParcare.class).getResultList();
            return FXCollections.observableArrayList(list);
        }
    }

    @Override
    public int size() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("SELECT COUNT(l) FROM LocParcare l", Long.class).getSingleResult();
            return count.intValue();
        }
    }
}
