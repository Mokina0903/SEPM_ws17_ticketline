package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.UserService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    private static final DateTimeFormatter NEWS_DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);
    @FXML
    public VBox vbNewsElement;
    @FXML
    public Button backButton;
    @FXML
    public ImageView newsImageView;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblText;

    private SimpleNewsDTO simpleNewsDTO;
    private NewsService newsService;
    private UserService userService;
    private MainController mainController;
    private NewsController newsController;

    public Label getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle( Label lblTitle ) {
        this.lblTitle = lblTitle;
    }

    public void initializeData( SimpleNewsDTO simpleNewsDTO, NewsService newsService,
                                MainController mainController, NewsController newsController, UserService userService) {
        lblDate.setText(NEWS_DTF.format(simpleNewsDTO.getPublishedAt()));
        lblTitle.setText(simpleNewsDTO.getTitle());
        lblText.setText(simpleNewsDTO.getSummary());
        lblText.setMaxWidth(500);
        this.simpleNewsDTO=simpleNewsDTO;
        this.newsService=newsService;
        this.mainController=mainController;
        this.newsController=newsController;
        this.userService=userService;
        backButton.setVisible(false);
        newsImageView.setVisible(false);
    }

    public void backToSimpleNewsView(ActionEvent actionEvent) {
        LOGGER.info("Closing the detailed view of the news element.");
        mainController.setGeneralErrorUnvisable();
        lblText.setText(simpleNewsDTO.getSummary());
        newsImageView.setImage(null);
        backButton.setVisible(false);
        backButton.setDisable(true);

        try {
            userService.removeFromUserNotSeen(mainController.getUser().getId(),simpleNewsDTO.getId());
        } catch (DataAccessException e) {
            LOGGER.debug("Not possible to load the simple news!");
            mainController.showGeneralError("Can not close the detailed view of this news.");
            //e.printStackTrace();
        }

    }

    public void detailedNews(MouseEvent mouseEvent) {
        LOGGER.info("Loading the detail view of this news element");
        mainController.setGeneralErrorUnvisable();
        Task<DetailedNewsDTO> task = new Task<>() {
            @Override
            protected DetailedNewsDTO call() throws DataAccessException {

                return newsService.findById(simpleNewsDTO.getId());
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                DetailedNewsDTO detailedNewsDTO= getValue();
                lblText.setText(detailedNewsDTO.getText());
                if(detailedNewsDTO.getPicPath() != null && !detailedNewsDTO.getPicPath().isEmpty()){
                    Image img = new Image(detailedNewsDTO.getPicPath(),540 , 380, false, false);
                    newsImageView.setImage(img);
                    newsImageView.setVisible(true);
                }

                backButton.setVisible(true);
                backButton.setDisable(false);
                vbNewsElement.setStyle("-fx-background-color:rgba(245, 245, 245,0)");
            }

            @Override
            protected void failed() {
                LOGGER.debug("failed to load detailed news.");
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
