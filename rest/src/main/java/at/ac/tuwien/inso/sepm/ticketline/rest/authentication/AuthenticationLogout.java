package at.ac.tuwien.inso.sepm.ticketline.rest.authentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "AuthenticationLogout", description = "Data Transfer Objects for de-Authentication Requests via REST")
public class AuthenticationLogout {

    @ApiModelProperty(required = true, name = "The unique name of the user", example = "admin")
    private String username;

    @ApiModelProperty(required = true, name = "The password of the user", example = "password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CharSequence getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthenticationLogout{" +
            "username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
    }



    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    public static AuthenticationLogoutBuilder builder() {
        return new AuthenticationLogoutBuilder();
    }

    public static final class AuthenticationLogoutBuilder {

        private String username;
        private String password;

        public AuthenticationLogoutBuilder username(String username) {
            this.username = username;
            return this;
        }

        public AuthenticationLogoutBuilder password(String password) {
            this.password = password;
            return this;
        }

        public AuthenticationLogout build() {
            AuthenticationLogout authenticationLogout = new AuthenticationLogout();
            //AuthenticationLogout.setUsername(username);
            //AuthenticationLogout.setPassword(password);
            return authenticationLogout;
        }
    }
}
