package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.TicketController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.user.UserController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.AuthenticationInformationService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);


    private static final int TAB_ICON_FONT_SIZE = 20;

    @FXML
    private StackPane spMainContent;

    @FXML
    private ProgressBar pbLoadingProgress;

    @FXML
    private TabPane tpContent;

    @FXML
    private MenuBar mbMain;

    @FXML
    private StackPane spMenue;

    private Node login;

    private final SpringFxmlLoader springFxmlLoader;
    private final FontAwesome fontAwesome;

    private NewsController newsController;
    private CustomerController customerController;
    private UserController userController;
    private EventController eventController;
    private TicketController ticketController;

    private UserService userService;
    private DetailedUserDTO detailedUserDTO;

    public Tab getNewsTab() {
        return newsTab;
    }

    public MainController(
        SpringFxmlLoader springFxmlLoader,
        FontAwesome fontAwesome,
        AuthenticationInformationService authenticationInformationService,
        UserService userService
    ) {
        this.springFxmlLoader = springFxmlLoader;
        this.fontAwesome = fontAwesome;
        authenticationInformationService.addAuthenticationChangeListener(
            authenticationTokenInfo -> setAuthenticated(null != authenticationTokenInfo));
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        Platform.runLater(() -> mbMain.setUseSystemMenuBar(true));
        pbLoadingProgress.setProgress(0);
        login = springFxmlLoader.load("/fxml/authenticationComponent.fxml");
        spMainContent.getChildren().add(login);
        initNewsTabPane();
        initEventTabPane();
        initTicketTabPane();
        initCustomerTabPane();
        initUserTabPane();

    }

    @FXML
    private void initMenue() {
        Pane menuePane = null;
        menuePane = springFxmlLoader.load("/fxml/menuePane.fxml");
        spMenue.getChildren().add(menuePane);
    }

    @FXML
    private void exitApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }


    @FXML
    private void aboutApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) spMainContent.getScene().getWindow();
        Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setScene(new Scene(springFxmlLoader.load("/fxml/aboutDialog.fxml")));
        dialog.setTitle(BundleManager.getBundle().getString("dialog.about.title"));
        dialog.showAndWait();
    }

    Tab newsTab;

    private void initNewsTabPane() {
        SpringFxmlLoader.Wrapper<NewsController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/news/newsComponent.fxml");
        newsController = wrapper.getController();
        newsTab = new Tab(null, wrapper.getLoadedObject());
        newsController.setNewsTab(newsTab);
        Glyph newsGlyph = fontAwesome.create(FontAwesome.Glyph.NEWSPAPER_ALT);
        newsGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        newsGlyph.setColor(Color.WHITE);
        newsTab.setGraphic(newsGlyph);
        tpContent.getTabs().add(newsTab);

    }

    Tab customerTab;

    private void initCustomerTabPane(){
        SpringFxmlLoader.Wrapper<CustomerController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/customer/customerComponent.fxml");
        customerController = wrapper.getController();
        customerTab = new Tab(null, wrapper.getLoadedObject());
        customerController.setCustomerTab(customerTab);
        Glyph customerGlyph = fontAwesome.create(FontAwesome.Glyph.USERS);
        customerGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        customerGlyph.setColor(Color.WHITE);
        customerTab.setGraphic(customerGlyph);
        tpContent.getTabs().add(customerTab);
    }

    Tab userTab;

    private void initUserTabPane(){
        SpringFxmlLoader.Wrapper<UserController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/user/userComponent.fxml");
        userController = wrapper.getController();
        userTab = new Tab(null, wrapper.getLoadedObject());
        userController.setUserTab(userTab);
        Glyph userGlyph = fontAwesome.create(FontAwesome.Glyph.USER);
        userGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        userGlyph.setColor(Color.WHITE);
        userTab.setGraphic(userGlyph);
        tpContent.getTabs().add(userTab);
    }

    Tab eventTab;

    private void initEventTabPane(){
        SpringFxmlLoader.Wrapper<EventController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/event/eventComponent.fxml");
        eventController = wrapper.getController();
        eventTab = new Tab(null, wrapper.getLoadedObject());
        eventController.setEventTab(eventTab);
        Glyph eventGlyph = fontAwesome.create(FontAwesome.Glyph.FILM);
        eventGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        eventGlyph.setColor(Color.WHITE);
        eventTab.setGraphic(eventGlyph);
        tpContent.getTabs().add(eventTab);
    }

    Tab ticketTab;

    private void initTicketTabPane(){
        SpringFxmlLoader.Wrapper<TicketController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/ticket/ticketComponent.fxml");
        ticketController = wrapper.getController();
        ticketTab = new Tab(null, wrapper.getLoadedObject());
        ticketController.setTicketTab(ticketTab);
        Glyph ticketGlyph = fontAwesome.create(FontAwesome.Glyph.TICKET);
        ticketGlyph.setFontSize(TAB_ICON_FONT_SIZE);
        ticketGlyph.setColor(Color.WHITE);
        ticketTab.setGraphic(ticketGlyph);
        tpContent.getTabs().add(ticketTab);
    }

    private void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            if (spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().remove(login);
            }
            newsController.loadNews();
            initMenue();
        } else {
            if (!spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().add(login);
                spMenue.getChildren().clear();
            }
        }
    }

    public void setProgressbarProgress(double progress) {
        pbLoadingProgress.setProgress(progress);
    }

    public void loadDetailedUserDTO(String name) {
        try {
            this.detailedUserDTO = userService.findByName(name);
        } catch (DataAccessException e) {
            JavaFXUtils.createExceptionDialog(e, spMainContent.getScene().getWindow()).showAndWait();
            // e.printStackTrace();
        }
    }

    public DetailedUserDTO getUser() {
        return this.detailedUserDTO;
    }
}
