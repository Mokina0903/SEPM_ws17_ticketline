package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "users")
public class User {

    public static final Integer LOGIN_ATTEMPTS = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user_id")
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false, length = 60)       //encoded; length = 60
    private String password;
    //counts down to 0 if password is wrong + blocks user afterwards
    @Column(nullable = false)
    private Integer attempts = LOGIN_ATTEMPTS;
    @Column(nullable = false)
    private Integer role;
    @Column(nullable = false)
    private boolean blocked;

    @ManyToMany()
    @JoinTable(
        name = "notSeen",
        joinColumns = @JoinColumn(name = "users_id"),
        inverseJoinColumns = @JoinColumn(name = "news_id")) //toDo: make unique
    private List<News> notSeen = new ArrayList<>();

    /*private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String emailAdress;*/



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
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

    public List<News> getNotSeen() {
        return notSeen;
    }

    public void setNotSeen( List<News> notSeen ) {
        this.notSeen = notSeen;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public void resetAttempts() {
        this.attempts = LOGIN_ATTEMPTS;
    }


    public static final class UserBuilder {
        private Long id;
        private String userName;
        private String password;
        private Integer role;
        private boolean blocked;
        private List<News> notSeen;

        private UserBuilder() {
        }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Integer role) {
            this.role = role;
            return this;
        }

        public UserBuilder blocked(boolean blocked) {
            this.blocked = blocked;
            return this;
        }

        public UserBuilder notSeen(List<News> notSeen){
            this.notSeen = notSeen;
            return this;
        }

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUserName(userName);
            user.setPassword(password);
            user.setAttempts(LOGIN_ATTEMPTS);
            user.setRole(role);
            user.setBlocked(blocked);
            user.setNotSeen(notSeen);
            return user;
        }
    }


}
