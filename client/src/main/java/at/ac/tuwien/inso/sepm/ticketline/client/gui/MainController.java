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
public class MainController implements LocalizationObserver{

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);


    private static final int TAB_ICON_FONT_SIZE = 20;
    @FXML
    public Label generalErrors;

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

    private Tab newsTab;
    private Tab eventTab;
    private Tab ticketTab;
    private Tab userTab;
    private Tab customerTab;

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

       /* newsController = (NewsController) initTabPane(newsController, "news/newsComponent.fxml", newsTab, "NEWSPAPER_ALT");
        eventController = (EventController) initTabPane(eventController, "event/eventComponent.fxml", eventTab, "FILM");
        ticketController = (TicketController) initTabPane(ticketController, "ticket/ticketComponent.fxml", ticketTab, "TICKET");
        customerController = (CustomerController) initTabPane(customerController, "customer/customerComponent.fxml", customerTab, "USERS");
        userController = (UserController) initTabPane(userController, "user/userComponent.fxml", userTab, "USER");
   */ }

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

    private TabElement initTabPane(TabElement controller, String fxmlPath, Tab tab, String glyphSymbol) {
        SpringFxmlLoader.Wrapper<TabElement> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/"+ fxmlPath);
        controller = wrapper.getController();
        tab = new Tab(null, wrapper.getLoadedObject());
        controller.setTab(tab);
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setFontSize(TAB_ICON_FONT_SIZE);
        glyph.setColor(Color.WHITE);
        tab.setGraphic(glyph);
        tpContent.getTabs().add(tab);
        return controller;
    }

    private void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            if (spMainContent.getChildren().contains(login)) {
                spMainContent.getChildren().remove(login);
            }

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

            loadTabController();

            if(detailedUserDTO.getRole() == 2){
                newsController.addNewNews.setDisable(true);
                newsController.addNewNews.setVisible(false);
            } else {
                newsController.addNewNews.setDisable(false);
                newsController.addNewNews.setVisible(true);
            }

            customerController.preparePagination();

        } catch (DataAccessException e) {
            JavaFXUtils.createExceptionDialog(e, spMainContent.getScene().getWindow()).showAndWait();
            // e.printStackTrace();
        }
    }

    private void loadTabController() {
        tpContent.getTabs().clear();
        newsController = (NewsController) initTabPane(newsController, "news/newsComponent.fxml", newsTab, "NEWSPAPER_ALT");
        eventController = (EventController) initTabPane(eventController, "event/eventComponent.fxml", eventTab, "FILM");
        ticketController = (TicketController) initTabPane(ticketController, "ticket/ticketComponent.fxml", ticketTab, "TICKET");
        customerController = (CustomerController) initTabPane(customerController, "customer/customerComponent.fxml", customerTab, "USERS");
        if (detailedUserDTO.getRole() == 1) {
            userController = (UserController) initTabPane(userController, "user/userComponent.fxml", userTab, "USER");
            userController.loadUsers();
        }
        newsController.loadNews();
        eventController.loadEvents();
        initMenue();
    }


    public DetailedUserDTO getUser() {
        return this.detailedUserDTO;
    }

/* //to update only active frame labels for localization
    public MenueCategory getActiveMenueCategory() {
        Tab tab = tpContent.getSelectionModel().getSelectedItem();

        if (tab.equals(newsTab)) {
            return MenueCategory.NEWS;
        }
        else if (tab.equals(userTab)) {
            return MenueCategory.USER;
        }
        else if (tab.equals(customerTab)) {
            return MenueCategory.CUSTOMER;
        }
        else if (tab.equals(eventTab)) {
            return MenueCategory.EVENT;
        }
        else return MenueCategory.TICKET;
    }*/

    @Override
    public void update() {
        //reset labels for localization here
    }

}
