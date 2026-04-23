package problema8.model;

import java.util.Objects;

public class User extends Entity<Long>{
    private String username;
    private String password;

    public User(Long aLong, String username, String password) {
        setId(aLong);
        this.username = username;
        this.password = password;
    }

    public User(Long aLong, String password) {
        this(aLong, "", password);
    }

    public User(String username, String password) {
        this(null, username, password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return super.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
