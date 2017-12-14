package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsAddFormularController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.implementation.SimpleUserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserController extends TabElement implements LocalizationObserver{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @FXML
    public TableView userTableView;
    @FXML
    public Button unlock;
    @FXML
    public Button lock;
    @FXML
    public Button resetPwd;
    @FXML
    public TableColumn blockedCol;
    @FXML
    public TableColumn usernameCol;
    @FXML
    public Button addUser;
    @FXML
    public BorderPane userOverviewRoot;


    @FXML
    private TabHeaderController tabHeaderController;

    private Tab userTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    @Autowired
    private LocalizationSubject localizationSubject;


    public Tab getUserTab() {
        return userTab;
    }

    public void setUserTab(Tab userTab) {
        this.userTab = userTab;
    }


    public UserController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USER);
        update();
        localizationSubject.attach(this);

        loadUsers();
    }

    public void loadUsers(){
        //TODO: implemt fillig of the Tableview
    }

    @FXML
    public void unlockUser(ActionEvent actionEvent) {
        //TODO: implement unlock the selected user
    }

    @FXML
    public void lockUser(ActionEvent actionEvent) {
        //TODO: implemet lock the selected User
    }

    @FXML
    public void openEditUserDialog(ActionEvent actionEvent) {
       // TODO: open editUserDialog window to reset passwords

        SpringFxmlLoader.Wrapper<UserDialogController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/user/editUserDialog.fxml");
        wrapper.getController().initializeData(new DetailedUserDTO(), userOverviewRoot);
        userTab.setContent(wrapper.getLoadedObject());

    }

    @FXML
    public void openAddUserDialog(ActionEvent actionEvent){
        //TODO: open editUserDialog window to create a new user

    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.title"));
        lock.setText(BundleManager.getBundle().getString("user.unlock"));
        unlock.setText(BundleManager.getBundle().getString("user.lock"));
        resetPwd.setText(BundleManager.getBundle().getString("user.resetPassoword"));
        addUser.setText(BundleManager.getBundle().getString("user.addUser"));

        usernameCol.setText(BundleManager.getBundle().getString("authenticate.userName"));
        blockedCol.setText(BundleManager.getBundle().getString("user.locked"));
    }

    @Override
    protected void setTab(Tab tab) {
        userTab = tab;
    }


}
