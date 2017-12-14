package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsAddFormularController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.implementation.SimpleUserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    private TabHeaderController tabHeaderController;

    private Tab userTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

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
        update();
        localizationSubject.attach(this);

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        blockedCol.setCellValueFactory(new PropertyValueFactory<>("blocked"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        disableUI();
    }

    public void loadUsers(){
        // TODO: Wait for status, privileges

        int i = 10;

        //while (i > 0 && mainController.getUser() == null) {
            try {
                Thread.sleep(100);
                i--;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        //}

        // Takes more than 1 Second
        if (i <= 0)
            return;

        // No privileges

        /*
        DetailedUserDTO detailedUserDTO = mainController.getUser();

        System.out.println(detailedUserDTO.toString());

        if (mainController.getUser().getRole() != 1)
            return;
        */

        Task<List<SimpleUserDTO>> taskloadUsers = new Task<>() {
            @Override
            protected List<SimpleUserDTO> call() throws DataAccessException {
                userTableView.getItems().clear();
                disableUI();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                LOGGER.warn("Could not load userlist");
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
        UserSimpleProperty userSimpleProperty = userTableView.getSelectionModel().getSelectedItem();

        DetailedUserDTO detailedUserDTO = mainController.getUser();

        if (userSimpleProperty.getUsername().equals(detailedUserDTO.getUserName())) {
            // TODO: Allert: is not allowed
            return;
        }

        try {
            userService.unblockUser(userSimpleProperty.getUsername());
            loadUsers();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void lockUser(ActionEvent actionEvent) {
        UserSimpleProperty userSimpleProperty = userTableView.getSelectionModel().getSelectedItem();

        DetailedUserDTO detailedUserDTO = mainController.getUser();

        if (userSimpleProperty.getUsername().equals(detailedUserDTO.getUserName())) {
            // TODO: Allert: is not allowed
            return;
        }

        try {
            userService.blockUser(userSimpleProperty.getUsername());
            loadUsers();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openEditUserDialog(ActionEvent actionEvent) {
        UserSimpleProperty userSimpleProperty = userTableView.getSelectionModel().getSelectedItem();

        SpringFxmlLoader.Wrapper<UserDialogController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/user/editUserDialog.fxml");
        wrapper.getController().initializeData(new DetailedUserDTO(), userOverviewRoot,userSimpleProperty);
        userTab.setContent(wrapper.getLoadedObject());

    }

    @FXML
    public void openAddUserDialog(ActionEvent actionEvent){
        SpringFxmlLoader.Wrapper<UserDialogController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/user/editUserDialog.fxml");
        wrapper.getController().initializeData(new DetailedUserDTO(), userOverviewRoot,null);
        userTab.setContent(wrapper.getLoadedObject());
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.title"));
        lock.setText(BundleManager.getBundle().getString("user.lock"));
        unlock.setText(BundleManager.getBundle().getString("user.unlock"));
        resetPwd.setText(BundleManager.getBundle().getString("user.resetPassoword"));
        addUser.setText(BundleManager.getBundle().getString("user.addUser"));

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
