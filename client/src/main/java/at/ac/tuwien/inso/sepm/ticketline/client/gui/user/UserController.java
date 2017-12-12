package at.ac.tuwien.inso.sepm.ticketline.client.gui.user;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserController implements LocalizationObserver{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @FXML
    private TabHeaderController tabHeaderController;

    private Tab userTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;

    @Autowired
    private LocalizationSubject localizationSubject;


    public Tab getUserTab() {
        return userTab;
    }

    public void setUserTab(Tab userTab) {
        this.userTab = userTab;
    }


    public UserController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USER);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.user"));
        localizationSubject.attach(this);

    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("user.user"));
    }
}
