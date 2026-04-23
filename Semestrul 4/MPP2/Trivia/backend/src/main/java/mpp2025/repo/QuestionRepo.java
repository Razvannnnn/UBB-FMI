package mpp2025.repo;

import mpp2025.domain.Game;
import mpp2025.domain.Question;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepo {
    public List<Question> findAll(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Question").list();
        }
    }

    public Question findOne(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Question.class, id);
        }
    }

    public void modify(Question question) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.merge(question);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.rollback();
                } catch (Exception rollbackEx) {
                    System.err.println("Rollback failed: " + rollbackEx.getMessage());
                }
            }
            throw e;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
