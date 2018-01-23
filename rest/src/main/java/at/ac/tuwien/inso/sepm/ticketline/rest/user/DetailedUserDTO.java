package at.ac.tuwien.inso.sepm.ticketline.rest.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;


@ApiModel(value = "DetailedUserDTO", description = "A detailed DTO for user entries via rest")
public class DetailedUserDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(readOnly = true, name = "The name of the user")
    private String userName;

    @ApiModelProperty(required = true, name = "The encrypted password of the user")
    private String password;

    @ApiModelProperty(required = true, name = "The role of the user")
    private Integer role;

    @ApiModelProperty(required = true, name = "Is the user blocked?")
    private boolean blocked;

    @ApiModelProperty(required = true, name = "the news the user has not yet seen")
    private List<SimpleNewsDTO> notSeen;

    @ApiModelProperty(required = true, readOnly = true, name = "The current version of the DTO")
    private Integer version;

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

    public List<SimpleNewsDTO> getNotSeen() {
        return notSeen;
    }

    public void setNotSeen( List<SimpleNewsDTO> notSeen ) {
        this.notSeen = notSeen;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "DetailedUserDTO{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            ", blocked=" + blocked +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailedUserDTO that = (DetailedUserDTO) o;

        if (blocked != that.blocked) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (blocked ? 1 : 0);
        return result;
    }

    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static final class UserDTOBuilder {

        private Long id;
        private String userName;
        private String password;
        private Integer role;
        private boolean blocked;
        private List<SimpleNewsDTO> notSeen;
        private Integer version;

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

        public UserDTOBuilder role(Integer role) {
            this.role = role;
            return this;
        }

        public UserDTOBuilder blocked(boolean blocked) {
            this.blocked = blocked;
            return this;
        }

        public UserDTOBuilder notSeen(List<SimpleNewsDTO> notSeen){
            this.notSeen = notSeen;
            return this;
        }

        public UserDTOBuilder version(Integer version){
            this.version = version;
            return this;
        }

        public DetailedUserDTO build() {
            DetailedUserDTO userDTO = new DetailedUserDTO();
            userDTO.setId(id);
            userDTO.setUserName(userName);
            userDTO.setPassword(password);
            userDTO.setRole(role);
            userDTO.setBlocked(blocked);
            userDTO.setNotSeen(notSeen);
            userDTO.setVersion(version);
            return userDTO;
        }
    }
}