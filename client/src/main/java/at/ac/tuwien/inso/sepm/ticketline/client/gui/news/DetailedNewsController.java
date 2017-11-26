package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class DetailedNewsController {

    private static final DateTimeFormatter NEWS_DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);


    @FXML
    public Label lblText;
    @FXML
    public ImageView newsImageViews;
    @FXML
    public Button backButton;
    @FXML
    public VBox vbDetailedNewsBox;

    private NewsController newsController;

    public void initializeData( DetailedNewsDTO detailedNewsDTO, NewsController newsController){

        lblText.setText(detailedNewsDTO.getText());

        this.newsController=newsController;
    }

    public void backToSimpleNewsView(ActionEvent actionEvent) {
        newsController.loadNews();
    }
}
