package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class NewsController extends TabElement implements LocalizationObserver{

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @FXML
    public Button addNewNews;
    @FXML
    public VBox vBContainer;
    @FXML
    public Tab newNewsTab;
    @FXML
    public Tab oldNewsTab;
    @FXML
    public ListView vbNewsElementsNew;

    @FXML
    private ListView<VBox> vbNewsElementsOld;

    @FXML
    private TabHeaderController tabHeaderController;

    private Tab newsTab;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    @Autowired
    private LocalizationSubject localizationSubject;


    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final NewsService newsService;
    private final UserService userService;
    private List<SimpleNewsDTO> newNews;
    private List<SimpleNewsDTO> oldNews;

    public Tab getNewsTab() {
        return newsTab;
    }

/*    public void setNewsTab(Tab newsTab) {
        this.newsTab = newsTab;
    }*/

    public NewsController(MainController mainController, SpringFxmlLoader springFxmlLoader, NewsService newsService, UserService userService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.newsService = newsService;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("news.news"));

        newNewsTab.setClosable(false);
        oldNewsTab.setClosable(false);
        localizationSubject.attach(this);

        addNewNews.setGraphic(fontAwesome.create("PLUS").size(FONT_SIZE));
        vbNewsElementsNew.getSelectionModel()
            .selectedIndexProperty()
            .addListener((observable, oldvalue, newValue) -> {

                Platform.runLater(() -> {
                    vbNewsElementsNew.getSelectionModel().clearSelection();
                });

            });
    }


    public void loadNews() {
       LOGGER.info("Loading News");
        ObservableList<VBox> vbNewsBoxChildrenNew = vbNewsElementsNew.getItems();
        ObservableList<VBox> vbNewsBoxChildrenOld = vbNewsElementsOld.getItems();
        vbNewsBoxChildrenNew.clear();
        vbNewsBoxChildrenOld.clear();



        Task<List<SimpleNewsDTO>> taskNewNews = new Task<>() {
            @Override
            protected List<SimpleNewsDTO> call() throws DataAccessException, InterruptedException {

                Long userID;
                try {
                    userID = mainController.getUser().getId();
                }catch(NullPointerException e){
                    TimeUnit.MILLISECONDS.sleep(300);
                    userID = mainController.getUser().getId();
                }

                List<SimpleNewsDTO> news = new ArrayList<>();
                NewsController.this.oldNews = newsService.findOldNewsByUser(userID);
                NewsController.this.newNews = newsService.findNotSeenByUser(userID);
                return news;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                if (NewsController.this.newNews != null && !NewsController.this.newNews.isEmpty() ) {
                    for (SimpleNewsDTO newsDTO : NewsController.this.newNews) {

                        SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                            springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                        wrapper.getController().initializeData(newsDTO, newsService, mainController, NewsController.this, userService);
                        //Label title = wrapper.getController().getLblTitle();
                        //title.setText("("+BundleManager.getBundle().getString("news.new")+")" + title.getText());
                        wrapper.getLoadedObject().setStyle("-fx-background-color:rgba(220, 229, 244, .7)");

                        vbNewsBoxChildrenNew.add(wrapper.getController().vbNewsElement);
                    }

                }
                if (NewsController.this.oldNews != null && !NewsController.this.oldNews.isEmpty() ) {
                    for (SimpleNewsDTO oldNewsDTO : NewsController.this.oldNews) {

                        SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                            springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                        wrapper.getController().initializeData(oldNewsDTO, newsService, mainController, NewsController.this, userService);

                        vbNewsBoxChildrenOld.add(wrapper.getController().vbNewsElement);
                    }

                }

            }

            @Override
            protected void failed() {
                LOGGER.debug("Loading news failed.");
                if(getValue()==null || getValue().isEmpty()) {
                    super.failed();
                    JavaFXUtils.createExceptionDialog(getException(),
                        vbNewsElementsNew.getScene().getWindow()).showAndWait();
                }
            }
        };
        taskNewNews.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );


        new Thread(taskNewNews).start();

    }


    public void addNewNews(ActionEvent actionEvent) {
        LOGGER.info("Opening new dialog to add news.");

        SpringFxmlLoader.Wrapper<NewsAddFormularController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/news/addNewsFormular.fxml");
        wrapper.getController().initializeData(springFxmlLoader, newsService, NewsController.this, vBContainer);
        VBox addNewsRoot = springFxmlLoader.load("/fxml/news/addNewsFormular.fxml");
        newsTab.setContent(addNewsRoot);

    }

    @Override
    public void update() {

        tabHeaderController.setTitle(BundleManager.getBundle().getString("news.news"));
        newNewsTab.setText(BundleManager.getBundle().getString("news.newnews"));
        oldNewsTab.setText(BundleManager.getBundle().getString("news.oldnews"));
       // loadNews();
    }

    @Override
    protected void setTab(Tab tab) {
        newsTab = tab;
    }
}
