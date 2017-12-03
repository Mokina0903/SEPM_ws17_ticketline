package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
    @FXML
    public Button backWithoutSaveBtn;

    private DetailedNewsDTO newNews;


    private SpringFxmlLoader springFxmlLoader;

    private NewsService newsService;

    private Node oldContent;

    private FileInputStream inputStream;


    private NewsController c;
    private TabHeaderController tabHeaderController;


     void initializeData(SpringFxmlLoader loader, NewsService service, NewsController controller, Node oldContent){
        springFxmlLoader = loader;
        newsService = service;
        c = controller;
        this.oldContent = oldContent;
    }


    @FXML
    public void saveNewNews(ActionEvent actionEvent) {

        DetailedNewsDTO.NewsDTOBuilder builder = new DetailedNewsDTO.NewsDTOBuilder();
        builder.title(TitleTF.getText());
        if(inputStream!= null){
            builder.picture(inputStream);
        }
        builder.text(TextArea.getText());
        newNews = builder.build();
        try {
        newNews = newsService.publishNews(newNews);
        c.loadNews();
        c.getNewsTab().setContent(oldContent);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void addImage(ActionEvent actionEvent) {

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:/Users/"));
        FileChooser.ExtensionFilter f1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fc.getExtensionFilters().addAll(f1);
        File file = fc.showOpenDialog(null);


        if (file == null) {
            //TODO: add alert
            return;
        }
        if (file.length() > 5 * 1024 * 1024) {
           //TODO: add alert
            return;
        }

        Image img = new Image(file.toURI().toString());
        newsImage.setImage(img);
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            //TODO: add alert
            e.printStackTrace();
        }



    }

    @FXML
    public void goBackWithoutSave(ActionEvent actionEvent) {
        c.getNewsTab().setContent(oldContent);
    }
}
