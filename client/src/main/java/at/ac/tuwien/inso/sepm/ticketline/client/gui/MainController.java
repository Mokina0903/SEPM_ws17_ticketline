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
public class MainController implements LocalizationObserver {

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

    private Tab newsTab = new Tab();
    private Tab eventTab = new Tab();
    private Tab ticketTab = new Tab();
    private Tab userTab = new Tab();
    private Tab customerTab = new Tab();

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
    }

    @FXML
    private void initMenue() {
        Pane menuePane = null;
        menuePane = springFxmlLoader.load("/fxml/menuePane.fxml");
        spMenue.getChildren().add(menuePane);
        generalErrors.setVisible(false);
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

        } catch (DataAccessException e) {
            JavaFXUtils.createExceptionDialog(e, spMainContent.getScene().getWindow()).showAndWait();
            // e.printStackTrace();
        }
    }

    public void loadGuiComponentsOfUser() {
        initMenue();
        initTabs();
        initNews();
        setListenerForTabs();
    }

    private void initTabs() {
        tpContent.getTabs().clear();
        initTab(newsTab, "NEWSPAPER_ALT");
        initTab(eventTab, "FILM");
        initTab(ticketTab, "TICKET");
        initTab(customerTab, "USERS");
        if (detailedUserDTO.getRole() == 1) {
            initTab(userTab, "USER");
        }
    }

    private void initTab(Tab tab, String glyphSymbol) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setFontSize(TAB_ICON_FONT_SIZE);
        glyph.setColor(Color.WHITE);
        tab.setGraphic(glyph);
        tpContent.getTabs().add(tab);
    }

    private void initNews() {
        newsController = (NewsController) setTabContent(newsController, "news/newsComponent.fxml", newsTab);
        newsController.loadNews();

        if (detailedUserDTO.getRole() == 2) {
            newsController.addNewNews.setDisable(true);
            newsController.addNewNews.setVisible(false);
        } else {
            newsController.addNewNews.setDisable(false);
            newsController.addNewNews.setVisible(true);
        }
    }


    private void setListenerForTabs() {
        tpContent.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals(eventTab)) {
                if (eventController == null) {
                    eventController = (EventController) setTabContent(eventController, "event/eventComponent.fxml", eventTab);
                    eventController.loadEvents();
                    // eventController.preparePagination();
                }
            } else if (newValue.equals(ticketTab)) {
                if (ticketController == null) {
                    ticketController = (TicketController) setTabContent(ticketController, "ticket/ticketComponent.fxml", ticketTab);
                }
            } else if (newValue.equals(customerTab)) {
                if (customerController == null) {
                    customerController = (CustomerController) setTabContent(customerController, "customer/customerComponent.fxml", customerTab);
                    customerController.loadCustomer();
                    // customerController.preparePagination();
                }
            } else {
                if (userController == null) {
                    userController = (UserController) setTabContent(userController, "user/userComponent.fxml", userTab);
                    userController.loadUsers();
                }
            }
        });
    }

    private TabElement setTabContent(TabElement controller, String fxmlPath, Tab tab) {
        SpringFxmlLoader.Wrapper<TabElement> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/" + fxmlPath);
        controller = wrapper.getController();
        controller.setTab(tab);
        tab.setContent(wrapper.getLoadedObject());
        return controller;
    }

    public void showGeneralError(String text) {
        generalErrors.setText(text);
        generalErrors.setVisible(true);
        LOGGER.info(text);
    }

    public void setGeneralErrorUnvisable() {
        generalErrors.setVisible(false);
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
