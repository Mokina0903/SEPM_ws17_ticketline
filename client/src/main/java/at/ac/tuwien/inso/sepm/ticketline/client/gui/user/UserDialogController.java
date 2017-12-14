package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
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
    private DetailedUserDTO user;
    private Node oldContent;

    public UserDialogController(MainController mainController, SpringFxmlLoader springFxmlLoader, UserController userController) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.userController = userController;

    }

    void initializeData( DetailedUserDTO user, Node oldContent){
        this.user = user;
        this.oldContent = oldContent;
    }

    @FXML
    void initialize(){
        //TODO: implement pre-filling of all Textfields/Boxes + visability issues of textfields
    }

    @FXML
    public void saveUserData(ActionEvent actionEvent) {
        //TODO: save the manipulated DTO to the Database. It should not make a difference either an new user or an exsisting user is updated

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
        // TODO: How to update also this fields?

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
