package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblNumberFreeAttempts;
    @FXML
    private Label lblLoginAttempts;
    @FXML
    private Label lblLoginFailed;
    @FXML
    private Label lblBlocked;
    @FXML
    private Label lblContactAdmin;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);


    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final MainController mainController;


    public AuthenticationController(AuthenticationService authenticationService, UserService userService, MainController mainController) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {

        lblLoginAttempts.setVisible(false);
        lblLoginAttempts.setText(BundleManager.getBundle().getString("authenticate.attempts"));
        lblLoginFailed.setVisible(false);
        lblLoginFailed.setText(BundleManager.getBundle().getString("authenticate.loginFailed"));
        lblBlocked.setVisible(false);
        lblBlocked.setText(BundleManager.getBundle().getString("authenticate.blocked"));
        lblContactAdmin.setVisible(false);
        lblContactAdmin.setText(BundleManager.getBundle().getString("authenticate.contactAdmin"));
    }


    //todo login after log out not possible -> task failed() instead of succeeded()


    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {

        try {
            boolean blocked = userService.isBlocked(txtUsername.getText());
            if (!blocked) {
                Task<AuthenticationTokenInfo> task = new Task<>() {

                    @Override
                    protected AuthenticationTokenInfo call() throws DataAccessException {

                        AuthenticationTokenInfo authenticationTokenInfo = authenticationService.authenticate(
                            AuthenticationRequest.builder()
                                .username(txtUsername.getText())
                                .password(txtPassword.getText())
                                .build());
                        return authenticationTokenInfo;
                    }

                    @Override
                    protected void succeeded() {
                        mainController.loadDetailedUserDTO(getValue().getUsername());
                    }

                    @Override
                    protected void failed() {

                        super.failed();
                   /* JavaFXUtils.createExceptionDialog(getException(),
                        ((Node) actionEvent.getTarget()).getScene().getWindow()).showAndWait();*/
                        try {
                            setLabels();
                        } catch (DataAccessException e) {
                            LOGGER.warn("Could not get left login attempts and set labels");
                        }
                    }
                };
                task.runningProperty().addListener((observable, oldValue, running) ->
                    mainController.setProgressbarProgress(
                        running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
                );
                new Thread(task).start();
            }
        } catch (DataAccessException e) {
            lblLoginFailed.setVisible(true);
        }
//        throw new BlockedUserException();
    }


    private void setLabels() throws DataAccessException {
        Integer freeAttempts = userService.getLoginAttemptsLeft(txtUsername.getText());

        if (freeAttempts > 0) {
            lblLoginAttempts.setVisible(true);
            lblLoginFailed.setVisible(true);
            lblBlocked.setVisible(false);
            lblContactAdmin.setVisible(false);
            lblNumberFreeAttempts.setText(Integer.toString(freeAttempts));
        } else {
            lblLoginAttempts.setVisible(false);
            lblLoginFailed.setVisible(false);
            lblBlocked.setVisible(true);
            lblContactAdmin.setVisible(true);
            lblNumberFreeAttempts.setText("");
        }
    }


}