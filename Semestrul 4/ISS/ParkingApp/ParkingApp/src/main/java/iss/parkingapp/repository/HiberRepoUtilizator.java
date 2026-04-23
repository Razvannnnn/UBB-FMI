package iss.parkingapp.repository;

import iss.parkingapp.domain.Utilizator;
import iss.parkingapp.utils.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.Objects;

public class HiberRepoUtilizator implements IRepoUtilizator {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void add(Utilizator entity) {
        logger.info("Adding user: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            entity.setRol("user");
            session.persist(entity);
        });
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting user with id: {}", id);
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            Utilizator utilizator = session.find(Utilizator.class, id);
            if (utilizator != null) {
                session.remove(utilizator);
                logger.info("User deleted successfully with id: {}", id);
            } else {
                logger.warn("No user found with id: {}", id);
            }
        });
    }

    @Override
    public void update(Utilizator entity) {
        logger.info("Updating user: {}", entity);
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
    }

    @Override
    public Utilizator findOne(Long id) {
        logger.info("Finding user by id: {}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(Utilizator.class, id);
        }
    }

    @Override
    public ObservableList<Utilizator> findAll() {
        logger.info("Finding all users");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Utilizator> result = session.createQuery("from Utilizator", Utilizator.class).getResultList();
            return FXCollections.observableArrayList(result);
        }
    }

    @Override
    public int size() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("select count(u) from Utilizator u", Long.class).getSingleResult();
            return count.intValue();
        }
    }

    @Override
    public Utilizator login(String email, String password) {
        logger.info("Logging in user with email: {}", email);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Utilizator where email = :email and password = :password", Utilizator.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResultOrNull();
        } catch (NoResultException e) {
            logger.info("No user found with email: {} and provided password", email);
            return null;
        }
    }

    @Override
    public Utilizator findByEmail(String email) {
        logger.info("Finding user by email: {}", email);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Utilizator where email = :email", Utilizator.class)
                    .setParameter("email", email)
                    .getSingleResultOrNull();
        } catch (NoResultException e) {
            logger.info("No user found with email: {}", email);
            return null;
        }
    }
}
