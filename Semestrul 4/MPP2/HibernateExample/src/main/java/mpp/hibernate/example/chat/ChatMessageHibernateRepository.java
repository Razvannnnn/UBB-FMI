package mpp.hibernate.example.chat;

import org.hibernate.Session;

import java.util.List;
import java.util.Objects;

public class ChatMessageHibernateRepository implements ChatMessageRepository{
    @Override
    public void save(ChatMessage chatMessage) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.persist(chatMessage));
    }

    @Override
    public void delete(Integer id) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            ChatMessage message=session.createQuery("from ChatMessage where id=?1",ChatMessage.class).
                    setParameter(1,id).uniqueResult();
            System.out.println("In delete am gasit mesajul "+message);
            if (message!=null) {
                session.remove(message);
                session.flush();
            }
        });
    }

    @Override
    public ChatMessage findOne(Integer id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
        return session.createSelectionQuery("from ChatMessage where id=:idM ", ChatMessage.class)
                .setParameter("idM", id)
                .getSingleResultOrNull();
      }
    }

    @Override
    public void update(Integer id, ChatMessage message) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            if (!Objects.isNull(session.find(ChatMessage.class, id))) {
                System.out.println("In update, am gasit mesajul cu id-ul "+id);
                session.merge(message);
                session.flush();
            }
        });

    }

    @Override
    public Iterable<ChatMessage> getAll() {

       try( Session session=HibernateUtils.getSessionFactory().openSession()) {
           return session.createQuery("from ChatMessage ", ChatMessage.class).getResultList();
       }
    }

    @Override
    public List<ChatMessage> getMessagesFrom(String name) {
        try(Session session=HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("from ChatMessage message where message.from=?1",ChatMessage.class).setParameter(1,name).getResultList();
        }
    }
}
