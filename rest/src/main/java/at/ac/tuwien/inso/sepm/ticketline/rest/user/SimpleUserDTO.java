package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "DetailedUserDTO", description = "A simple DTO for user entries via rest")
public class SimpleUserDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, readOnly = true, name = "The name of the user")
    private String userName;

    @ApiModelProperty(required = true, readOnly = true, name = "The password of the user")
    private String password;

    @ApiModelProperty(required = true, readOnly = true, name = "The number of free login attempts of the user")
    private Integer attempts;

    @ApiModelProperty(required = true, readOnly = true, name = "The role of the user")
    private Integer role;

    @ApiModelProperty(required = true, readOnly = true, name = "The blocked status of the user")
    private boolean blocked;

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

    @Override
    public String toString() {
        return "SimpleUserDTO{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            ", blocked=" + blocked +
            '}';
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static final class UserDTOBuilder {

        private Long id;
        private String userName;
        private String password;
        private Integer attempts;
        private Integer role;
        private boolean blocked;

        private UserDTOBuilder() {
        }

        public UserDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserDTOBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDTOBuilder attempts(Integer attempts) {
            this.attempts = attempts;
            return this;
        }

        public UserDTOBuilder role(Integer role) {
            this.role = role;
            return this;
        }

        public UserDTOBuilder blocked(boolean blocked) {
            this.blocked = blocked;
            return this;
        }

        public SimpleUserDTO build() {
            SimpleUserDTO userDTO = new SimpleUserDTO();
            userDTO.setId(id);
            userDTO.setUserName(userName);
            userDTO.setPassword(password);
            userDTO.setAttempts(attempts);
            userDTO.setRole(role);
            userDTO.setBlocked(blocked);
            return userDTO;
        }
    }
}