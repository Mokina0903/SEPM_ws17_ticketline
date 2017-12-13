package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDialogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @FXML
    public TextField usernaeTF;
    @FXML
    public TextField passwordTF;
    @FXML
    public TextField passwordConfirmTF;
    @FXML
    public ComboBox roleCombo;
    @FXML
    public Button saveBtn;



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

        userController.getUserTab().setContent(oldContent);
    }


}
