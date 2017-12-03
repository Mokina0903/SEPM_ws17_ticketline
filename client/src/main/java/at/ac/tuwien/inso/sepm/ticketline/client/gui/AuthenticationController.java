package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationRequest;
import at.ac.tuwien.inso.sepm.ticketline.rest.authentication.AuthenticationTokenInfo;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
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
    private Label noAttempts;

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
    private void handleAuthenticate(ActionEvent actionEvent) {
        Task<AuthenticationTokenInfo> task = new Task<>() {
            @Override
            protected AuthenticationTokenInfo call() throws DataAccessException {
                return authenticationService.authenticate(
                    AuthenticationRequest.builder()
                        .username(txtUsername.getText())
                        .password(txtPassword.getText())
                        .build());

            }

            @Override
            protected void failed() {
                try {
                    setAttemptsLabel();
                } catch (DataAccessException e) {
                    LOGGER.info("Faild login cause no valid username");
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

    private void setAttemptsLabel() throws DataAccessException {
        String name = txtUsername.getText();
        noAttempts.setText(Integer.toString(userService.getLoginAttemptsLeft(name)));
    }

}