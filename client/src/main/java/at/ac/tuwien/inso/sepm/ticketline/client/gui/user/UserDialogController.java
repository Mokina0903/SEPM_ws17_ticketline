package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDialogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @FXML
    public TextField usernameTF;
    @FXML
    public PasswordField passwordPF;
    @FXML
    public PasswordField passwordConfirmPF;
    @FXML
    public ComboBox roleCombo;
    @FXML
    public Button saveBtn;
    @FXML
    public Button backBtn;
    @FXML
    public Label usernameLb;
    @FXML
    public Label passwordLb;
    @FXML
    public Label passwordConfirmLb;
    @FXML
    public Label roleLb;


    private MainController mainController;
    private SpringFxmlLoader springFxmlLoader;
    private UserController userController;
    private final UserService userService;
    private DetailedUserDTO user;
    private Node oldContent;
    private UserSimpleProperty userSimpleProperty;

    public UserDialogController(MainController mainController, SpringFxmlLoader springFxmlLoader, UserController userController, UserService userService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.userController = userController;
        this.userService = userService;
    }

    void initializeData(DetailedUserDTO user, Node oldContent, UserSimpleProperty userSimpleProperty){
        this.user = user;
        this.oldContent = oldContent;
        this.userSimpleProperty = userSimpleProperty;

        usernameTF.clear();
        passwordPF.clear();
        passwordConfirmPF.clear();

        usernameTF.setDisable(true);
        passwordPF.setDisable(true);
        passwordConfirmPF.setDisable(true);

        roleCombo.setDisable(true);

        if (userSimpleProperty == null) {
            usernameTF.setDisable(false);
            passwordPF.setDisable(false);
            passwordConfirmPF.setDisable(false);
            roleCombo.setDisable(false);

        } else {
            usernameTF.setText(userSimpleProperty.getUsername());
            passwordPF.setDisable(false);
            passwordConfirmPF.setDisable(false);


        }
    }

    @FXML
    void initialize(){

    }

    @FXML
    public void saveUserData(ActionEvent actionEvent) {
        //TODO: save the manipulated DTO to the Database. It should not make a difference either an new user or an exsisting user is updated

        if (userSimpleProperty == null) {
            // TODO: Implement here
            //userService.addNewUser()
            // Add new User
        } else {
            // TODO: Implement here
            // Reset Password
        }

        // TODO: Only when it was correct
        userController.loadUsers();
        userController.getUserTab().setContent(oldContent);
    }

    @FXML
    public void goBackWithoutSave(ActionEvent actionEvent) {
        //TODO: save the manipulated DTO to the Database. It should not make a difference either an new user or an exsisting user is updated

        userController.loadUsers();
        userController.getUserTab().setContent(oldContent);
    }

    public void update() {
        // TODO: Question How to update also this fields?

        usernameLb.setText(BundleManager.getBundle().getString("authenticate.userName"));
        passwordLb.setText(BundleManager.getBundle().getString("authenticate.password"));
        passwordConfirmLb.setText(BundleManager.getBundle().getString("user.confirm"));
        roleLb.setText(BundleManager.getBundle().getString("user.role"));

        passwordPF.setPromptText("authenticate.password");
        passwordConfirmPF.setPromptText("authenticate.password");

        saveBtn.setText(BundleManager.getBundle().getString("general.save"));
        backBtn.setText(BundleManager.getBundle().getString("general.back"));
    }
}
