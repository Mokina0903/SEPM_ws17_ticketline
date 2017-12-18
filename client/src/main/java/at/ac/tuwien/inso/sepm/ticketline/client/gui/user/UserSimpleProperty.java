package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class UserSimpleProperty {

    private final SimpleLongProperty userId;
    private final SimpleStringProperty username;
    private final SimpleBooleanProperty blocked;
    private final SimpleIntegerProperty role;


    public UserSimpleProperty(long userId,String username, boolean blocked, int role) {
        this.userId = new SimpleLongProperty(userId);
        this.username = new SimpleStringProperty(username);
        this.blocked = new SimpleBooleanProperty(blocked);
        this.role = new SimpleIntegerProperty(role);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public boolean isBlocked() {
        return blocked.get();
    }

    public SimpleBooleanProperty blockedProperty() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked.set(blocked);
    }

    public int getRole() {
        return role.get();
    }

    public SimpleIntegerProperty roleProperty() {
        return role;
    }

    public void setRole(int role) {
        this.role.set(role);
    }

    public long getUserId() {
        return userId.get();
    }

    public SimpleLongProperty userIdProperty() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId.set(userId);
    }

    public static List<UserSimpleProperty> SimpleUserDTOtoUserColumns(List<SimpleUserDTO> list) {
        List<UserSimpleProperty> rlist = new ArrayList<>();

        for (SimpleUserDTO simpleUserDTO : list) {
            rlist.add(new UserSimpleProperty(
                simpleUserDTO.getId(),
                simpleUserDTO.getUserName(),
                simpleUserDTO.isBlocked(),
                simpleUserDTO.getRole()
                ));
        }
        return rlist;
    }
}
