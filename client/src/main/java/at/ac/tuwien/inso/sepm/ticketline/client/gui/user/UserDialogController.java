package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private TabHeaderController tabHeaderController;

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
    public Label versionLb;
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
    private int role;

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

    void initializeData(Node oldContent, UserSimpleProperty userSimpleProperty) {
        this.oldContent = oldContent;
        this.userSimpleProperty = userSimpleProperty;

        lblInvalidUsername.setVisible(false);
        versionLb.setVisible(false);
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
    void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USER);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.user"));

        localizationSubject.attach(this);
        setButtonGraphic(saveBtn, "CHECK", Color.OLIVE);
        setButtonGraphic(backBtn, "TIMES", Color.CRIMSON);

        setUpValidation(usernameTF);
        setUpValidation(passwordPF);
        setUpValidation(passwordConfirmPF);
        setUpValidation(roleCombo);
    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }

    private void setUpValidation(final TextField tf) {
        tf.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                validate(tf);
            }

        });
    }

    private void setUpValidation(final ComboBox cb) {
        cb.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {
                validate(cb);
            }

        });
    }

    private boolean validate(Node validationField) {

        ObservableList<String> styleClass = validationField.getStyleClass();

        if (validationField.equals(usernameTF)) {
            if (usernameTF.getText().isEmpty() || usernameTF.getText().trim().equals("")) {
                if (!styleClass.contains("error")) {
                    styleClass.add("error");
                    return false;
                }
            } else {
                styleClass.remove("error");
                return true;
            }
        }

        if (validationField.equals(passwordPF)) {
            if (passwordPF.getText().isEmpty() || passwordPF.getText().trim().equals("")) {
                if (!styleClass.contains("error")) {
                    styleClass.add("error");
                    return false;
                }
            } else {
                styleClass.remove("error");
                return true;
            }
        }

        if (validationField.equals(passwordConfirmPF)) {
            if (!userService.confirmPasswordIsOk(passwordPF.getText(), passwordConfirmPF.getText())) {
                if (!styleClass.contains("error")) {
                    styleClass.add("error");
                    return false;
                }
            } else {
                styleClass.remove("error");
                return true;
            }
        }

        if (validationField.equals(roleCombo)) {
            if (roleCombo.getSelectionModel().getSelectedIndex() == -1) {
                if (!styleClass.contains("error")) {
                    styleClass.add("error");
                    return false;
                }
            } else {
                styleClass.remove("error");
                return true;
            }
        }
        return false;
    }


    @FXML
    public void saveUserData(ActionEvent actionEvent) {


        lblInvalidUsername.setVisible(false);
        versionLb.setVisible(false);
        lblInvalidPassword.setVisible(false);
        lblInvalidRole.setVisible(false);

        lblInvalidUsername.setText("");
        versionLb.setText("");
        lblInvalidPassword.setText("");
        lblInvalidRole.setText("");

        update();

        boolean validInput = true;

        role = roleCombo.getSelectionModel().getSelectedIndex() + 1;
        if (userSimpleProperty != null) {
            if ((userSimpleProperty.getRole() == 1) || userSimpleProperty.getRole() == 2) {
                role = userSimpleProperty.getRole();
            }
        } else if (!validate(roleCombo)) {
            validInput = false;
        }

        if (!validate(usernameTF)) {
            validInput = false;
        }

        if (!validate(passwordPF)) {
            validInput = false;
        }
        if (!validate(passwordConfirmPF)) {
            validInput = false;
        }

        if (!validInput) {
            return;
        }


        Task<Void> workerTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (userSimpleProperty == null) {
                    // Add new User

                    SimpleUserDTO simpleUserDTO = SimpleUserDTO.builder()
                        .userName(usernameTF.getText())
                        .password(passwordPF.getText())
                        .role(role)
                        .build();
                    userService.addNewUser(simpleUserDTO);
                } else {
                    // Reset Password

                    SimpleUserDTO simpleUserDTO = SimpleUserDTO.builder()
                        .userName(usernameTF.getText())
                        .password(passwordPF.getText())
                        .version(userSimpleProperty.getVersion())
                        .role(role)
                        .build();
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
                if (getException().getMessage().trim().equals("409")) {
                    // Username already exists
                    lblInvalidUsername.setVisible(true);
                    return;
                }
                else if(getException().getMessage().trim().equals("424")){
                    //Version ist nicht aktuell

                    versionLb.setVisible(true);
                    getUser();
                    return;
                }

                mainController.showGeneralError(getException().toString());
            }
        };

        workerTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(workerTask).start();
    }

    public void getUser() {
        Task<Void> workerTask = new Task<Void>() {
            DetailedUserDTO userHelp;
            @Override
            protected Void call() throws Exception {
                userHelp = userService.findByName(usernameTF.getText());

                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                userController.loadUsers();
                userSimpleProperty.setVersion(userHelp.getVersion());
            }

            @Override
            protected void failed() {
                super.failed();
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
    public void goBackWithoutSave(ActionEvent actionEvent) {
        userController.loadUsers();
        userController.getUserTab().setContent(oldContent);
    }

    @Override
    public void update() {
        usernameLb.setText(BundleManager.getBundle().getString("user.userName_"));
        passwordLb.setText(BundleManager.getBundle().getString("user.password_"));
        passwordConfirmLb.setText(BundleManager.getBundle().getString("user.confirm"));
        roleLb.setText(BundleManager.getBundle().getString("user.role_"));

        passwordPF.setPromptText(BundleManager.getBundle().getString("authenticate.password"));
        passwordConfirmPF.setPromptText(BundleManager.getBundle().getString("authenticate.password"));

        lblInvalidUsername.setText(BundleManager.getBundle().getString("user.invalidusername"));
        versionLb.setText(BundleManager.getBundle().getString("user.version"));
        lblInvalidPassword.setText(BundleManager.getBundle().getString("user.invalidpasswort"));
        lblInvalidRole.setText(BundleManager.getBundle().getString("user.invalidrole"));

        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.user"));

    }
}
