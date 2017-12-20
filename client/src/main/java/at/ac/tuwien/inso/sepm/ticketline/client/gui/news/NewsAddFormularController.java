package at.ac.tuwien.inso.sepm.ticketline.client.gui.news;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

@Component
public class NewsAddFormularController implements LocalizationObserver {

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
    @FXML
    public Label lblTitle;
    @FXML
    public Label lblAddImage;

    private DetailedNewsDTO newNews;


    private SpringFxmlLoader springFxmlLoader;

    private NewsService newsService;

    private Node oldContent;

    private String picPath;
    private NewsController c;
    private TabHeaderController tabHeaderController;

    @Autowired
    private MainController mainController;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;


    @Autowired
    private LocalizationSubject localizationSubject;

    @FXML
    void initialize(){
        localizationSubject.attach(this);

        lblInvalidTitle.setVisible(false);
        lblInvalidText.setVisible(false);

        setButtonGraphic(saveBtn, "CHECK", Color.OLIVE);
        setButtonGraphic(backWithoutSaveBtn, "TIMES", Color.CRIMSON);
    }

    void initializeData(SpringFxmlLoader loader, NewsService service, NewsController controller, Node oldContent){
        springFxmlLoader = loader;
        newsService = service;
        c = controller;
        this.oldContent = oldContent;
        picPath = null;
    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        glyph.setFontSize(FONT_SIZE);
        button.setGraphic(glyph);
    }

    @FXML
    public void saveNewNews(ActionEvent actionEvent) {
        LOGGER.info("Saving new news.");

        mainController.setGeneralErrorUnvisable();
        lblInvalidTitle.setVisible(false);
        lblInvalidText.setVisible(false);

        DetailedNewsDTO.NewsDTOBuilder builder = new DetailedNewsDTO.NewsDTOBuilder();

        if(!newsService.validateTextField(TitleTF)){
            LOGGER.warn("Invalid title was typed in!");
            lblInvalidTitle.setVisible(true);
            return;
        }

        if(!newsService.validateTextArea(TextArea)){
            LOGGER.warn("Invalid text was typed in!");
            lblInvalidText.setVisible(true);
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
            LOGGER.warn("Could not save news. Data AccessException");
            mainController.showGeneralError("Not able to save the News because of technical issues.");
        }
    }

    public void addImage(ActionEvent actionEvent) {
        LOGGER.info("Adding Image to new news.");
        mainController.setGeneralErrorUnvisable();

        String home = System.getProperty("user.home");
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(home));
        FileChooser.ExtensionFilter f1 = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fc.getExtensionFilters().addAll(f1);
        File file = fc.showOpenDialog(null);


        if (file == null) {
            LOGGER.warn("Tried to upload empty image.");
            mainController.showGeneralError("File was empty. Choose a file before upload.");
            return;
        }
        if (file.length() > 5 * 1024 * 1024) {
            LOGGER.warn("Tried to upload image bigger than 5MB.");
            mainController.showGeneralError("Image was too large! Image has to be smaler than 5MB.");
            return;
        }

        Image img = new Image(file.toURI().toString(),540 , 380, false, false);
        newsImage.setImage(img);
        new File(home +"/NewsPictures").mkdir();
        File destination = new File(home+"/NewsPictures/"+ file.getName());
        picPath = destination.toURI().toString();
        try {

            Files.copy(file.toPath(),destination.toPath());

        } catch (FileAlreadyExistsException e) {
            LOGGER.warn("File already exists");

           // e.printStackTrace();
        } catch (IOException e) {
            LOGGER.warn("Loading image failed.");
           // e.printStackTrace();
            mainController.showGeneralError("Loading image failed because of technical issues.");
        }


    }

    @FXML

    public void goBackWithoutSave(ActionEvent actionEvent) {
        c.getNewsTab().setContent(oldContent);
    }

    @Override
    public void update() {
        lblAddImage.setText(BundleManager.getBundle().getString("news.addimage"));
        lblTitle.setText(BundleManager.getBundle().getString("news.title"));
        lblInvalidText.setText(BundleManager.getBundle().getString("news.text.tooLong"));
        lblInvalidTitle.setText(BundleManager.getBundle().getString("news.title.tooLong"));
    }
}