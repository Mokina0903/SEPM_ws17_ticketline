package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;

@Entity
@Table (name = "users")
public class User {




    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user_id")
    @SequenceGenerator(name = "seq_user_id", sequenceName = "seq_user_id")
    private Long id;
    //@Column(nullable = false, name = "user_name")
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false, length = 60)       //encoded; length = 60
    private String password;
    @Column(nullable = false)
    private Integer role;
    @Column(nullable = false)
    private boolean blocked;

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

    public static UserBuilder builder() {
        return new UserBuilder();
    }


    public static final class UserBuilder {
        private Long id;
        private String userName;
        private String password;
        private Integer role;
        private boolean blocked;

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

        public User build() {
            User user = new User();
            user.setId(id);
            user.setUserName(userName);
            user.setPassword(password);
            user.setRole(role);
            user.setBlocked(blocked);
            return user;
        }
    }


}
