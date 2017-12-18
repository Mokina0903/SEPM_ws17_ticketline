package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.io.IOException;
import java.nio.file.Files;

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
    @FXML
    public Label lblInvalidTitle;
    @FXML
    public Label lblInvalidText;

    private DetailedNewsDTO newNews;


    private SpringFxmlLoader springFxmlLoader;

    private NewsService newsService;

    private Node oldContent;

    private String picPath;


    private NewsController c;
    private TabHeaderController tabHeaderController;


    void initializeData(SpringFxmlLoader loader, NewsService service, NewsController controller, Node oldContent){
        springFxmlLoader = loader;
        newsService = service;
        c = controller;
        this.oldContent = oldContent;
        picPath = null;

        /*TitleTF.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
                if(newValue.length()>200){
                    TitleTF.setText(oldValue);
                }
            }
        });
        TextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
                if(newValue.length()>10000){
                    TextArea.setText(oldValue);
                }
            }
        });
        */
    }


    @FXML
    public void saveNewNews(ActionEvent actionEvent) {

        DetailedNewsDTO.NewsDTOBuilder builder = new DetailedNewsDTO.NewsDTOBuilder();

        if(TitleTF.getText().length()>100){
            JavaFXUtils.createErrorDialog(BundleManager.getBundle().getString("news.title.tooLong"),
                VBroot.getScene().getWindow()).showAndWait();
            return;
        }
        if(TextArea.getText().length()>10000){
            JavaFXUtils.createErrorDialog(BundleManager.getBundle().getString("news.text.tooLong"),
                VBroot.getScene().getWindow()).showAndWait();
            return;
        }

        builder.title(TitleTF.getText());
        if(picPath!= null){
            builder.picture(picPath);
        }

        builder.text(TextArea.getText());
        newNews = builder.build();
        try {
            newNews = newsService.publishNews(newNews);
            c.loadNews();
            c.getNewsTab().setContent(oldContent);
        } catch (DataAccessException e) {
            JavaFXUtils.createExceptionDialog(e,
                VBroot.getScene().getWindow()).showAndWait();
        }
    }

    public void addImage(ActionEvent actionEvent) {

        String home = System.getProperty("user.home");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(home));
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

        Image img = new Image(file.toURI().toString(),540 , 380, false, false);
        newsImage.setImage(img);
        new File(home +"/NewsPictures").mkdir();
        File destination = new File(home+"/NewsPictures/"+ file.getName());
        picPath = destination.toURI().toString();
        try {

            Files.copy(file.toPath(),destination.toPath());

        } catch (IOException e) {
            //TODO: add alert
            e.printStackTrace();
        }



    }

    @FXML
    public void goBackWithoutSave(ActionEvent actionEvent) {
        c.getNewsTab().setContent(oldContent);
    }
}
