package problema8.model;

import java.util.Objects;

public class User extends Entity<Long>{
    private String Username;
    private String Password;

    public User(Long aLong, String username, String password) {
        setId(aLong);
        this.Username = username;
        this.Password = password;
    }

    public User(Long aLong, String password) {
        this(aLong, "", password);
    }

    public User(String username, String password) {
        this(null, username, password);
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public Long getId() {
        return super.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(Username, user.Username) && Objects.equals(Password, user.Password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Username, Password);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + Username + '\'' +
                ", password='" + Password + '\'' +
                '}';
    }
}
