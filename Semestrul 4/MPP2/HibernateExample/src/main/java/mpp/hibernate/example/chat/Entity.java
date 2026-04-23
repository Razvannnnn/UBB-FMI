package mpp.hibernate.example.chat;


public interface Entity<ID> {
    void setId(ID id);
    ID getId();
}
