package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private Label lblLoginInfo;
    @FXML
    private Label lblLoginFaild;
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
        lblLoginInfo.setVisible(false);
        lblLoginFaild.setVisible(false);
        lblBlocked.setVisible(false);
        lblContactAdmin.setVisible(false);
    }

    @FXML
    private void handleAuthenticate(ActionEvent actionEvent) {
        Task<AuthenticationTokenInfo> task = new Task<>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                AuthenticationTokenInfo authenticationTokenInfo= authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(txtUsername.getText())
                        .password(txtPassword.getText())
                        .build());
                return authenticationTokenInfo;
            }

            @Override
            protected void succeeded() {
                try {
                    userService.resetLoginAttempts(txtUsername.getText());
                } catch (DataAccessException e) {
                    LOGGER.info("Faild login cause no valid username or password");
                }
                mainController.loadDetailedUserDTO(getValue().getUsername());
            }

            @Override
            protected void failed() {
                try {
                    userService.decreaseLoginAttempts(txtUsername.getText());
                    checkLeftAttempts();
                } catch (DataAccessException e) {
                    LOGGER.info("Faild login cause no valid username or password");
                }
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    ((Node) actionEvent.getTarget()).getScene().getWindow()).showAndWait();
/*
                try {

                } catch (DataAccessException e) {

                }
*/

            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }

    private void checkLeftAttempts() throws DataAccessException {
        Integer freeAttempts = userService.getLoginAttemptsLeft(txtUsername.getText());
        if(freeAttempts > 0) {
            setLabels(false);
        }
        else {
            setLabels(true);
            userService.blockUser(txtUsername.getText());

        }
    }


    private void setLabels(boolean isBlocked) throws DataAccessException {
        Integer freeAttempts = userService.getLoginAttemptsLeft(txtUsername.getText());
        lblLoginInfo.setVisible(!isBlocked);
        lblLoginFaild.setVisible(!isBlocked);
        lblBlocked.setVisible(isBlocked);
        lblContactAdmin.setVisible(isBlocked);

        if (isBlocked) {
            lblNumberFreeAttempts.setVisible(false);
        } else {
            lblNumberFreeAttempts.setVisible(true);
            lblNumberFreeAttempts.setText(Integer.toString(freeAttempts));
        }
    }



}