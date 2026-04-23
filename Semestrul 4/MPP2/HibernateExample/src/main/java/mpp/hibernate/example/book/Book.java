package mpp.hibernate.example.book;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
public class Book {
   private  Long  id;
    private String title;

    public Book() {}
    public Book(String title){
        this.title=title;
    }
    @Id
    @GeneratedValue(strategy=IDENTITY)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @NotNull
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
