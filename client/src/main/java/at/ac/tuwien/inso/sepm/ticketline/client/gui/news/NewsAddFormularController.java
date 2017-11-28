package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NewsAddFormularController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);
    @FXML
    public TextField TitleTF;

    @FXML
    public Button saveBtn;
    @FXML
    public Button addImgBtn;
    @FXML
    public ImageView newsImage;
    @FXML
    public javafx.scene.control.TextArea TextArea;

    private DetailedNewsDTO newNews;

    private VBox NewsOverview;

    public void setVBox (VBox box){
        NewsOverview = box;
    }

    @FXML
    public void saveNewNews(ActionEvent actionEvent) {
        //saves news, leads back to news overview
    }
}
