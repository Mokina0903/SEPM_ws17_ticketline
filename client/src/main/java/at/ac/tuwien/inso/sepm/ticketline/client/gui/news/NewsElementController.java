package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Iterator;
import java.util.List;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewsElementController {

    private static final DateTimeFormatter NEWS_DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);
    @FXML
    public VBox vbNewsElement;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    private SimpleNewsDTO simpleNewsDTO;
    private SpringFxmlLoader springFxmlLoader;
    private NewsService newsService;
    private MainController mainController;


    public void initializeData(SimpleNewsDTO simpleNewsDTO, SpringFxmlLoader springFxmlLoader, NewsService newsService,MainController mainController) {
        lblDate.setText(NEWS_DTF.format(simpleNewsDTO.getPublishedAt()));
        lblTitle.setText(simpleNewsDTO.getTitle());
        lblText.setText(simpleNewsDTO.getSummary());
        this.simpleNewsDTO=simpleNewsDTO;
        this.newsService=newsService;
        this.springFxmlLoader=springFxmlLoader;
        this.mainController=mainController;
    }

    public void detailedNews(MouseEvent mouseEvent) {

        Task<DetailedNewsDTO> task = new Task<>() {
            @Override
            protected DetailedNewsDTO call() throws DataAccessException {
                return newsService.findById(simpleNewsDTO.getId());
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                DetailedNewsDTO detailedNewsDTO= getValue();
                    SpringFxmlLoader.Wrapper<DetailedNewsController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/news/detailedNewsElement.fxml");
                    wrapper.getController().initializeData(detailedNewsDTO);
                vbNewsElement.getChildren().add(wrapper.getLoadedObject());


            }

            @Override
            protected void failed() {
                super.failed();
                JavaFXUtils.createExceptionDialog(getException(),
                    vbNewsElement.getScene().getWindow()).showAndWait();
            }
        };
        task.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );
        new Thread(task).start();
    }
}
