package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.service.implementation.SimpleAuthenticationService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MenueController implements LocalizationObserver {


    @FXML
    private Button btLogout;

    @FXML
    private ToggleButton tbtEn;
    @FXML
    private ToggleButton tbtDe;

    @FXML
    private Label lbLanguage;

    @Autowired
    private SimpleAuthenticationService authenticationService;

    @Autowired
    private MainController mainController;

    @Autowired
    private LocalizationSubject localizationSubject;


    @FXML
    private void initialize(){
        localizationSubject.attach(this);
        lbLanguage.setText(BundleManager.getBundle().getString("test.language"));
    }

    @FXML
    private void setLanguageToGerman() {
        BundleManager.changeLocale(Locale.GERMAN);
    }


    @FXML
    private void setLanguageToEnglish() {
        BundleManager.changeLocale(Locale.ENGLISH);
    }

    @FXML
    private void handleLogout() {
        //authenticationService.deAuthenticate();
        mainController.setAuthenticated(false);
    }

    @Override
    public void update() {
        lbLanguage.setText(BundleManager.getBundle().getString("test.language"));
    }


}
