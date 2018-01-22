package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserController extends TabElement implements LocalizationObserver{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    @FXML
    public TableView<UserSimpleProperty> userTableView;
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
    public TableColumn roleCol;
    @FXML
    public Button addUser;
    @FXML
    public BorderPane userOverviewRoot;
    @FXML
    private Label lblinvalidAction;



    @FXML
    private TabHeaderController tabHeaderController;

    private Tab userTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    @Autowired
    private LocalizationSubject localizationSubject;

    private final UserService userService;

    private List<SimpleUserDTO> userList;

    public Tab getUserTab() {
        return userTab;
    }

    public void setUserTab(Tab userTab) {
        this.userTab = userTab;
    }


    public UserController(MainController mainController, SpringFxmlLoader springFxmlLoader, UserService userService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USER);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.user"));

        update();
        localizationSubject.attach(this);

        addUser.setGraphic(fontAwesome.create("USER_PLUS").size(FONT_SIZE));
        lock.setGraphic(fontAwesome.create("LOCK").size(FONT_SIZE));
        unlock.setGraphic(fontAwesome.create("UNLOCK").size(FONT_SIZE));

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        blockedCol.setCellValueFactory(new PropertyValueFactory<>("blocked"));
        // TODO: (David) Translate int to String
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        lblinvalidAction.setVisible(false);
        disableUI();
    }

    public void loadUsers(){
        Task<List<SimpleUserDTO>> taskloadUsers = new Task<>() {
            @Override
            protected List<SimpleUserDTO> call() throws DataAccessException {
                userTableView.getItems().clear();
                disableUI();
                List<SimpleUserDTO> users = userService.findAll();
                userList = users;
                return users;
            }

            @Override
            protected void succeeded() {
                super.succeeded();

                ObservableList<UserSimpleProperty> list = FXCollections.observableArrayList(
                    UserSimpleProperty.SimpleUserDTOtoUserColumns(userList)
                );
                userTableView.getItems().addAll(list);
                enableUI();
            }

            @Override
            protected void failed() {
                super.failed();
                mainController.showGeneralError("Failure at loadUser: " + getException().getMessage());
            }
        };

        taskloadUsers.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(taskloadUsers).start();
    }

    @FXML
    public void unlockUser(ActionEvent actionEvent) {
        lblinvalidAction.setVisible(false);

        UserSimpleProperty userSimpleProperty = userTableView.getSelectionModel().getSelectedItem();

        if (userSimpleProperty == null) {
            // No User selected
            return;
        }

        if (userSimpleProperty.getUsername().equals(mainController.getUser().getUserName())) {
            lblinvalidAction.setVisible(true);
            return;
        }

        Task<Void> workerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                userService.unblockUser(userSimpleProperty.getUsername());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                loadUsers();

            }

            @Override
            protected void failed() {
                super.failed();
                loadUsers();
                mainController.showGeneralError("Failure at loadUser: " + getException().getMessage());
            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();
    }

    @FXML
    public void lockUser(ActionEvent actionEvent) {
        lblinvalidAction.setVisible(false);

        UserSimpleProperty userSimpleProperty = userTableView.getSelectionModel().getSelectedItem();

        if (userSimpleProperty == null) {
            // No User selected
            return;
        }

        if (userSimpleProperty.getUsername().equals(mainController.getUser().getUserName())) {
            lblinvalidAction.setVisible(true);
            return;
        }

        Task<Void> workerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                userService.blockUser(userSimpleProperty.getUsername());
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                loadUsers();
            }

            @Override
            protected void failed() {
                super.failed();
                loadUsers();
                mainController.showGeneralError("Failure at loadUser: " + getException().getMessage());
            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();
    }

    @FXML
    public void openEditUserDialog(ActionEvent actionEvent) {
        UserSimpleProperty userSimpleProperty = userTableView.getSelectionModel().getSelectedItem();

        SpringFxmlLoader.Wrapper<UserDialogController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/user/editUserDialog.fxml");
        wrapper.getController().initializeData(userOverviewRoot,userSimpleProperty);
        userTab.setContent(wrapper.getLoadedObject());

    }

    @FXML
    public void openAddUserDialog(ActionEvent actionEvent){
        SpringFxmlLoader.Wrapper<UserDialogController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/user/editUserDialog.fxml");
        wrapper.getController().initializeData(userOverviewRoot,null);
        userTab.setContent(wrapper.getLoadedObject());
    }

    @Override
    public void update() {
        lblinvalidAction.setText(BundleManager.getBundle().getString("user.invalidUser"));

        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.title"));
        resetPwd.setText(BundleManager.getBundle().getString("user.resetPassoword"));

        usernameCol.setText(BundleManager.getBundle().getString("authenticate.userName"));
        blockedCol.setText(BundleManager.getBundle().getString("user.locked"));
        roleCol.setText(BundleManager.getBundle().getString("user.role"));
    }

    @Override
    protected void setTab(Tab tab) {
        userTab = tab;
    }

    private void disableUI() {
        lock.setDisable(true);
        unlock.setDisable(true);
        resetPwd.setDisable(true);
        addUser.setDisable(true);
        userTableView.setDisable(true);
    }

    private void enableUI() {
        lock.setDisable(false);
        unlock.setDisable(false);
        resetPwd.setDisable(false);
        addUser.setDisable(false);
        userTableView.setDisable(false);
    }

}
