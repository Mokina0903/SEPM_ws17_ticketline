package at.ac.tuwien.inso.sepm.ticketline.client.gui.customer;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomerDialogController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDialogController.class);

    @FXML
    public TextField tfLname;
    @FXML
    public TextField tfBirthDate;
    @FXML
    public TextField tfEmail;

    @FXML
    private Label lbCustomerNumber;
    @FXML
    private Label lbCustomerNumberText;
    @FXML
    private Label lbCustomerName;
    @FXML
    private Label lbCustomerBirthdate;


    @FXML
    private Label lbInvalidName;
    @FXML
    private Label lbInvalidBirthdate;
    @FXML
    private Label lbInvalidEmail;

    @FXML
    private Button btOk;
    @FXML
    private Button btCancel;

    @Autowired
    private LocalizationSubject localizationSubject;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;
    private final CustomerController customerController;

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");


    private CustomerDTO customer;
    private Node oldContent;

    public CustomerDialogController(MainController mainController, SpringFxmlLoader springFxmlLoader, CustomerController customerController) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerController = customerController;
    }


    @FXML
    void initialize() {
        //TODO: implement pre-filling of all Textfields/Boxes + visability issues of textfields
        localizationSubject.attach(this);
        //   lbCustomerNumber = (customer == null? "" : customer.getcustomerNumber);

        lbInvalidName.setVisible(false);
        lbInvalidBirthdate.setVisible(false);
        lbInvalidEmail.setVisible(false);
        lbCustomerNumber.setVisible(false);
        lbCustomerNumberText.setVisible(false);


        setButtonGraphic(btOk, "CHECK", Color.OLIVE);
        setButtonGraphic(btCancel, "TIMES", Color.CRIMSON);
    }

    private void setButtonGraphic(Button button, String glyphSymbol, Color color) {
        Glyph glyph = fontAwesome.create(FontAwesome.Glyph.valueOf(glyphSymbol));
        glyph.setColor(color);
        button.setGraphic(glyph);
    }

    void initializeData(CustomerDTO customer, Node oldContent) {
        this.customer = customer;
        this.oldContent = oldContent;

        if (customer != null) {
            if (customer.getKnr() != null) {
                lbCustomerNumber.setVisible(true);
                lbCustomerNumberText.setText(String.valueOf(customer.getKnr()));
                lbCustomerNumberText.setVisible(true);
            }
        }

    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        customerController.getCustomerTab().setContent(oldContent);
    }

    @FXML
    public void handleOk(ActionEvent actionEvent) {
        //todo save customer

    }

    @Override
    public void update() {
        lbCustomerNumberText.setText(BundleManager.getBundle().getString("customer.number"));
        lbCustomerName.setText(BundleManager.getBundle().getString("customer.lname"));
        lbCustomerBirthdate.setText(BundleManager.getBundle().getString("customer.birthdate"));

        lbInvalidName.setText(BundleManager.getBundle().getString("customer.invalidName"));
        lbInvalidBirthdate.setText(BundleManager.getBundle().getString("customer.invalidBirthdate"));
        lbInvalidEmail.setText(BundleManager.getBundle().getString("customer.invalidEmail"));
    }
}
