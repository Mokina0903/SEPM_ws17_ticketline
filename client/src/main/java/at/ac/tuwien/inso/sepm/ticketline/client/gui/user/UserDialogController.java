package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDialogController implements LocalizationObserver {
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
    @FXML
    public Label lblInvalidUsername;
    @FXML
    public Label lblInvalidPassword;
    @FXML
    public Label lblInvalidRole;


    private MainController mainController;
    private SpringFxmlLoader springFxmlLoader;
    private UserController userController;
    private final UserService userService;
    private DetailedUserDTO user;
    private Node oldContent;
    private UserSimpleProperty userSimpleProperty;

    @Autowired
    private LocalizationSubject localizationSubject;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;


    public UserDialogController(MainController mainController, SpringFxmlLoader springFxmlLoader, UserController userController, UserService userService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.userController = userController;
        this.userService = userService;
    }

    void initializeData(Node oldContent, UserSimpleProperty userSimpleProperty){
        this.oldContent = oldContent;
        this.userSimpleProperty = userSimpleProperty;

        lblInvalidUsername.setVisible(false);
        lblInvalidPassword.setVisible(false);
        lblInvalidRole.setVisible(false);

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
        localizationSubject.attach(this);
        setButtonGraphic(saveBtn, "CHECK", Color.OLIVE);
        setButtonGraphic(backBtn, "TIMES", Color.CRIMSON);

    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }

    @FXML
    public void saveUserData(ActionEvent actionEvent) {
        mainController.setGeneralErrorUnvisable();

        lblInvalidUsername.setVisible(false);
        lblInvalidPassword.setVisible(false);
        lblInvalidRole.setVisible(false);

        lblInvalidUsername.setText("");
        lblInvalidPassword.setText("");
        lblInvalidRole.setText("");

        update();
        if (usernameTF.getText().trim().isEmpty())
        {
            System.out.println(lblInvalidUsername.getText());
            lblInvalidUsername.setVisible(true);
            return;
        }

        if (passwordPF.getText().trim().isEmpty() || !passwordPF.getText().equals(passwordConfirmPF.getText())) {
            lblInvalidPassword.setVisible(true);
            return;
        }

        int role = roleCombo.getSelectionModel().getSelectedIndex();
        if (role == -1) {
            lblInvalidRole.setVisible(true);
            return;
        }

        SimpleUserDTO simpleUserDTO = SimpleUserDTO.builder()
            .userName(usernameTF.getText())
            .password(passwordPF.getText())
            .role(role + 1)
            .build();

        Task<Void> workerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (userSimpleProperty == null) {
                    // Add new User
                    userService.addNewUser(simpleUserDTO);
                } else {
                    // Reset Password
                    userService.resetUserPassword(simpleUserDTO);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                userController.loadUsers();
                userController.getUserTab().setContent(oldContent);
            }

            @Override
            protected void failed() {
                mainController.showGeneralError(getException().toString());
            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();
    }

    @FXML
    public void goBackWithoutSave(ActionEvent actionEvent) {
        userController.loadUsers();
        userController.getUserTab().setContent(oldContent);
    }

    @Override
    public void update() {
        usernameLb.setText(BundleManager.getBundle().getString("authenticate.userName"));
        passwordLb.setText(BundleManager.getBundle().getString("authenticate.password"));
        passwordConfirmLb.setText(BundleManager.getBundle().getString("user.confirm"));
        roleLb.setText(BundleManager.getBundle().getString("user.role"));

        passwordPF.setPromptText("authenticate.password");
        passwordConfirmPF.setPromptText("authenticate.password");

        lblInvalidUsername.setText(BundleManager.getBundle().getString("user.invalidusername"));
        lblInvalidPassword.setText(BundleManager.getBundle().getString("user.invalidpasswort"));
        lblInvalidRole.setText(BundleManager.getBundle().getString("user.invalidrole"));

        saveBtn.setText(BundleManager.getBundle().getString("general.save"));
        backBtn.setText(BundleManager.getBundle().getString("general.back"));
    }
}
