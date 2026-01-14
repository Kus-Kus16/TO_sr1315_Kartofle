package pl.edu.agh.to.bgg.user;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "app_user";
    public static class Columns {
        public static final String ID = "id";
        public static final String USERNAME = "username";
    }

    @Id
    @GeneratedValue
    @Column(name = Columns.ID)
    private int id;

    @Column(name = Columns.USERNAME, unique = true, nullable = false)
    private String username;

    public User(String username) {
        this.username = username;
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}
