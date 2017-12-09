package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
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
import javafx.scene.text.Font;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class NewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @FXML
    public Button addNewNews;
    @FXML
    public VBox vBContainer;

    @FXML
    private ListView<VBox> vbNewsElements;

    @FXML
    private TabHeaderController tabHeaderController;

    private Tab newsTab;


    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final NewsService newsService;
    private final UserService userService;
    private List<SimpleNewsDTO> newNews;
    private List<SimpleNewsDTO> oldNews;

    public Tab getNewsTab() {
        return newsTab;
    }

    public void setNewsTab(Tab newsTab) {
        this.newsTab = newsTab;
    }

    public NewsController(MainController mainController, SpringFxmlLoader springFxmlLoader, NewsService newsService, UserService userService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.newsService = newsService;
        this.userService = userService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        tabHeaderController.setTitle("News");
        vbNewsElements.getSelectionModel()
            .selectedIndexProperty()
            .addListener((observable, oldvalue, newValue) -> {

                Platform.runLater(() -> {
                    vbNewsElements.getSelectionModel().clearSelection();
                });

            });
    }


    public void loadNews() {
        ObservableList<VBox> vbNewsBoxChildren = vbNewsElements.getItems();
        vbNewsBoxChildren.clear();

        /*
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Long userID = mainController.getUser().getId();

        try {
            this.oldNews = newsService.findOldNewsByUser(userID);
            this.newNews = newsService.findNotSeenByUser(userID);

        } catch (DataAccessException e) {

            JavaFXUtils.createExceptionDialog(e,
                vbNewsElements.getScene().getWindow()).showAndWait();
            //e.printStackTrace();
        }


        if (NewsController.this.newNews != null && !NewsController.this.newNews.isEmpty() ) {
            for (SimpleNewsDTO newsDTO : NewsController.this.newNews) {

                SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                wrapper.getController().initializeData(newsDTO, newsService, mainController, NewsController.this, userService);
                Label title = wrapper.getController().getLblTitle();
                title.setText("(NEW)" + title.getText());
                wrapper.getLoadedObject().setStyle("-fx-background-color:rgba(220, 229, 244, .7)");

                vbNewsBoxChildren.add(wrapper.getController().vbNewsElement);
            }

        }
        if (NewsController.this.oldNews != null && !NewsController.this.oldNews.isEmpty() ) {
            for (SimpleNewsDTO oldNewsDTO : NewsController.this.oldNews) {

                SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                    springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                wrapper.getController().initializeData(oldNewsDTO, newsService, mainController, NewsController.this, userService);

                vbNewsBoxChildren.add(wrapper.getController().vbNewsElement);
            }

        }
*/


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
                        Label title = wrapper.getController().getLblTitle();
                        title.setText("(NEW)" + title.getText());
                        wrapper.getLoadedObject().setStyle("-fx-background-color:rgba(220, 229, 244, .7)");

                        vbNewsBoxChildren.add(wrapper.getController().vbNewsElement);
                    }

                }
                if (NewsController.this.oldNews != null && !NewsController.this.oldNews.isEmpty() ) {
                    for (SimpleNewsDTO oldNewsDTO : NewsController.this.oldNews) {

                        SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                            springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                        wrapper.getController().initializeData(oldNewsDTO, newsService, mainController, NewsController.this, userService);

                        vbNewsBoxChildren.add(wrapper.getController().vbNewsElement);
                    }

                }

            }

            @Override
            protected void failed() {
                if(getValue()==null || getValue().isEmpty()) {
                    super.failed();
                    JavaFXUtils.createExceptionDialog(getException(),
                        vbNewsElements.getScene().getWindow()).showAndWait();
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

        SpringFxmlLoader.Wrapper<NewsAddFormularController> wrapper =
            springFxmlLoader.loadAndWrap("/fxml/news/addNewsFormular.fxml");
        wrapper.getController().initializeData(springFxmlLoader, newsService, NewsController.this, vBContainer);
        VBox addNewsRoot = springFxmlLoader.load("/fxml/news/addNewsFormular.fxml");
        newsTab.setContent(addNewsRoot);

    }
}
