package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DetailedNewsController {

    private static final DateTimeFormatter NEWS_DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

    @FXML
    public Label lblDate;
    @FXML
    public Label lblTitle;
    @FXML
    public Label lblText;
    @FXML
    public ImageView newsImageViews;
    @FXML
    public Button backButton;

    public void initializeData( DetailedNewsDTO detailedNewsDTO){
        lblDate.setText(NEWS_DTF.format(detailedNewsDTO.getPublishedAt()));
        lblTitle.setText(detailedNewsDTO.getTitle());
        lblText.setText(detailedNewsDTO.getText());

    }

    public void backToSimpleNewsView(ActionEvent actionEvent) {
    }
}
