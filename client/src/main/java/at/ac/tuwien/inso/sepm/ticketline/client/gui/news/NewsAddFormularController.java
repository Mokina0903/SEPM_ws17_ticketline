package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

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
    @FXML
    public VBox VBroot;

    private DetailedNewsDTO newNews;

    private String imgPath;

    private SpringFxmlLoader springFxmlLoader;

    private NewsService newsService;

    private VBox NewsOverview;

    public void setVBox (VBox box){
        NewsOverview = box;
    }
    public void setFxmlLoader(SpringFxmlLoader loader){
        this.springFxmlLoader = loader;
    }

    public void setNewsService (NewsService newsService){
        this.newsService = newsService;
    }



    @FXML
    public void saveNewNews(ActionEvent actionEvent) {

        DetailedNewsDTO.NewsDTOBuilder builder = new DetailedNewsDTO.NewsDTOBuilder();
        builder.publishedAt(LocalDateTime.now());
        builder.title(TitleTF.getText());
        builder.path(imgPath);
        builder.text(TextArea.getText());
        newNews = builder.build();
        try {
           newNews = newsService.publishNews(newNews);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        //TODO: publish news, add to new News by Users, go back to newsOverview
    }

    public void addImage(ActionEvent actionEvent) {

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:/Users/Verena/Pictures"));
        FileChooser.ExtensionFilter f1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fc.getExtensionFilters().addAll(f1);
        File file = fc.showOpenDialog(null);


        if (file == null) {
           imgPath = null;
            return;
        }
        if (file.length() > 5 * 1024 * 1024) {
           //TODO: add alert
            return;
        }
    }
}
