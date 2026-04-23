package mpp.hibernate.example.chat;

public class ChatMessageMain {
    public static void main(String[] args) {
        System.out.println("Starting ChatMessage main");
        ChatMessageRepository repo=new ChatMessageHibernateRepository();

        ChatMessage message=new ChatMessage("ion", "ana", "Ce  faci azi?");

        System.out.println("Salvam mesajul  "+message);
        repo.save(message);

        ChatMessage message1=repo.findOne(1);
        if (message1!=null)
            System.out.println("Am gasit mesajul cu id-ul 1 "+message1);
        else
            System.out.println("NU am gasit mesajul cu id-ul 1");

        System.out.println("Stergem mesajul cu id-ul 2");
        repo.delete(2);

        System.out.println("Actualizam  mesajul cu id-ul 3");
        ChatMessage message2=repo.findOne(3);
        message2.setMessage("Mesaj actualizat 2025");

        repo.update(3, message2);
        System.out.println("Mesajele ramanse in tabela");
        for(ChatMessage m: repo.getAll())
            System.out.println(m);
        System.out.println("-------------------------");


        System.out.println("Mesajele de la maria");
        for(ChatMessage m: repo.getMessagesFrom("maria"))
            System.out.println(m);

        HibernateUtils.closeSessionFactory();
    }
}
