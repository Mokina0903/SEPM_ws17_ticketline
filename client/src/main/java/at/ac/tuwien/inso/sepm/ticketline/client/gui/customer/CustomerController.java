package at.ac.tuwien.inso.sepm.ticketline.client.gui.customer;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabHeaderController;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController.class);


    @FXML
    private TabHeaderController tabHeaderController;

    private Tab customerTab;

    private final MainController mainController;
    private final SpringFxmlLoader springFxmlLoader;


    public Tab getCustomerTab() {
        return customerTab;
    }

    public void setCustomerTab(Tab customerTab) {
        this.customerTab = customerTab;
    }


    public CustomerController(MainController mainController, SpringFxmlLoader springFxmlLoader) {
        this.mainController = mainController;
        this.springFxmlLoader = springFxmlLoader;
    }

    @FXML
    private void initialize() {
        tabHeaderController.setIcon(FontAwesome.Glyph.USERS);
        tabHeaderController.setTitle("Customer");

    }


}
