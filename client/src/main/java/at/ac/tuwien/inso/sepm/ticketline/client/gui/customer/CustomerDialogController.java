package at.ac.tuwien.inso.sepm.ticketline.client.gui.customer;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.CustomerService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
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

import java.time.LocalDate;


@Component
public class CustomerDialogController implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDialogController.class);

    @FXML
    public TextField tfLname;
    @FXML
    public DatePicker dpBirthdate;
    @FXML
    public TextField tfEmail;
    @FXML
    public TextField tfFirstName;
    @FXML
    public Label lbInvalidCustomer;


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
    private final CustomerService customerService;

    private boolean isUpdate;

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");


    private CustomerDTO customer;
    private Node oldContent;

    public CustomerDialogController(MainController mainController, SpringFxmlLoader springFxmlLoader, CustomerController customerController, CustomerService customerService) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
        this.customerController = customerController;
        this.customerService = customerService;
    }


    @FXML
    void initialize() {
        //TODO: implement pre-filling of all Textfields/Boxes + visability issues of textfields
        localizationSubject.attach(this);
        lbInvalidName.setVisible(false);
        lbInvalidBirthdate.setVisible(false);
        lbInvalidEmail.setVisible(false);
        lbCustomerNumber.setVisible(false);
        lbCustomerNumberText.setVisible(false);
        lbInvalidCustomer.setVisible(false);

        setButtonGraphic(btOk, "CHECK", Color.OLIVE);
        setButtonGraphic(btCancel, "TIMES", Color.CRIMSON);

        addListeners();

        isUpdate = false;
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
                lbCustomerNumberText.setVisible(true);
                lbCustomerNumber.setText(String.valueOf(customer.getKnr()));

            }
            if (customer.getBirthDate() != null) {
                dpBirthdate.setValue(customer.getBirthDate());
            }
            if (customer.getEmail() != null) {
                tfEmail.setText(customer.getEmail());
            }
            if (customer.getName() != null) {
                tfFirstName.setText(customer.getName());
            }
            if (customer.getSurname() != null) {
                tfLname.setText(customer.getSurname());
            }
        }

    }

    @FXML
    public void handleCancel(ActionEvent actionEvent) {
        if (isUpdate) {
            setUpdate(false);
        }
        customerController.getCustomerTab().setContent(oldContent);
    }

    @FXML
    public void handleOk(ActionEvent actionEvent) {
        String mail = tfEmail.getText();
        String surname = tfLname.getText();
        LocalDate birthDate = dpBirthdate.getValue();
        String firstname = tfFirstName.getText();

        Long knr = lbCustomerNumber.getText().isEmpty()? 0: Long.valueOf(lbCustomerNumber.getText());


        CustomerDTO.CustomerDTOBuilder builder = new CustomerDTO.CustomerDTOBuilder();
        builder.birthDate(birthDate);
        builder.name(firstname);
        builder.surname(surname);
        builder.mail(mail);
        if(knr != null){
            builder.knr(knr);
        }
        CustomerDTO customer = builder.build();

        if (!customerService.checkIfCustomerValid(customer)){
            lbInvalidCustomer.setVisible(true);
            return;
        }
        lbInvalidCustomer.setVisible(false);


        try {
            if (!isUpdate) {
                customerService.saveCustomer(customer);
            } else {
                customerService.updateCustomer(customer);
                isUpdate = false;
            }

        } catch (DataAccessException e) {
            //TODO: add alert
            e.printStackTrace();
        }
        customerController.getCustomerTab().setContent(oldContent);
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

    private void addListeners() {

        lbInvalidName.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String text = tfFirstName.getText();
                if (text == null || text.matches(".*\\d+.*") || text.isEmpty()) {
                    btOk.setDisable(true);
                    lbInvalidName.setVisible(true);
                }
                text = tfLname.getText();
                if (text == null || text.matches(".*\\d+.*") || text.isEmpty()) {
                    btOk.setDisable(true);
                    lbInvalidName.setVisible(true);
                }
            }
        });
        lbInvalidEmail.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                String mail = tfEmail.getText();
                if (!(mail.contains("@")) || !mail.contains(".")) {
                    btOk.setDisable(true);
                    lbInvalidName.setVisible(true);
                }
            }
        });


    }
}
