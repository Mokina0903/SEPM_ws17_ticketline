package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
public class NewsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @FXML
    private ListView<VBox> vbNewsElements;

    @FXML
    private TabHeaderController tabHeaderController;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final NewsService newsService;

    public NewsController(MainController mainController, SpringFxmlLoader springFxmlLoader, NewsService newsService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.newsService = newsService;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.NEWSPAPER_ALT);
        tabHeaderController.setTitle("News");
        vbNewsElements.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        vbNewsElements.setStyle("-fx-background-color: transparent");
    }

    public void loadNews() {
        ObservableList<VBox> vbNewsBoxChildren = vbNewsElements.getItems();
        vbNewsBoxChildren.clear();
        Task<List<SimpleNewsDTO>> task = new Task<>() {
            @Override
            protected List<SimpleNewsDTO> call() throws DataAccessException {
                return newsService.findAll();
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                for (Iterator<SimpleNewsDTO> iterator = getValue().iterator(); iterator.hasNext(); ) {
                    SimpleNewsDTO news = iterator.next();
                    SpringFxmlLoader.Wrapper<NewsElementController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/news/newsElement.fxml");
                    wrapper.getController().initializeData(news,springFxmlLoader,newsService,mainController,NewsController.this);
                    vbNewsBoxChildren.add(wrapper.getController().vbNewsElement);
                    /*if (iterator.hasNext()) {
                        Separator separator = new Separator();
                        vbNewsBoxChildren.add(separator);
                    }*/
                }
            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbNewsElements.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }


}
