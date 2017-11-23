package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity (name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user_id")
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id")
    private Long userId;
    @Column(nullable = false, name = "user_name")
    private String userName;
    @Column(nullable = false, length = 60)       //encoded; length = 60
    private String password;
    @Column(nullable = false)
    private Integer role;
    @Column(nullable = false)
    private boolean blocked;

    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String emailAdress;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
