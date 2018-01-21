package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.HallplanController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EventElementController implements LocalizationObserver{

    private static final DateTimeFormatter EVENT_DTF =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

    @FXML
    public Label lblStartDate;
    @FXML
    public Label lblEndDate;
    @FXML
    public Label lblTitle;
    @FXML
    public Label lblText;
    @FXML
    public Button ticketReservationButton;
    @FXML
    public ImageView eventImageView;
    @FXML
    public Label lblPrice;
    @FXML
    public VBox vbElement;
    @FXML
    public Label lblPriceText;
    @FXML
    public Label lblArtistName;
    @FXML
    public Label lblArtist;


    private MainController mainController;
    private SpringFxmlLoader loader;

    Node myContainer;


    @Autowired
    private LocalizationSubject localizationSubject;
    private EventService eventService;
    private SimpleEventDTO simpleEventDTO;

    //removed eventservice from constr to autowired
    public EventElementController(MainController mainController, SpringFxmlLoader loader) {
        this.mainController = mainController;
        this.loader = loader;
    }

    @FXML
    private void initialize() {
        localizationSubject.attach(this);
    }

    public void initializeData(EventService eventService, SimpleEventDTO simpleEventDTO, Node myContainer) {

        this.eventService = eventService;
        this.simpleEventDTO = simpleEventDTO;
        this.myContainer = myContainer;

        lblStartDate.setText(EVENT_DTF.format(simpleEventDTO.getStartOfEvent()));
        lblEndDate.setText(EVENT_DTF.format(simpleEventDTO.getEndOfEvent()));
        lblTitle.setText(simpleEventDTO.getTitle());
        // lblArtistName.setText(simpleEventDTO.getArtistFirstName()+" "+simpleEventDTO.getArtistLastName());
        lblArtist.setText(BundleManager.getBundle().getString("events.artist"));
        lblPrice.setText(String.valueOf(simpleEventDTO.getPrice()));
        lblText.setMaxWidth(500);
        lblText.setText(simpleEventDTO.getDescriptionSummary());

        lblPriceText.setText(BundleManager.getBundle().getString("events.price") + ": ");

        eventImageView.setVisible(false);
    }

    public void ticketReservationForEvent(ActionEvent actionEvent) {
        DetailedEventDTO event  = null;
        try {
            event = eventService.findById(simpleEventDTO.getId());

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if(event  == null){
            //TODO: add alert/warning
            return;
        }
        mainController.setEvent(event);

        if (mainController.getCutsomer() == null) {
            SpringFxmlLoader.Wrapper<CustomerController> wrapper =
                loader.loadAndWrap("/fxml/customer/customerComponent.fxml");
            Node root = loader.load("/fxml/customer/customerComponent.fxml");
            CustomerController c = wrapper.getController();
            c.preparePagination();
            c.setTicketProzessView(mainController.getEventTab());
            c.setOldContent(myContainer);
            mainController.getEventTab().setContent(root);
        } else {
            SpringFxmlLoader.Wrapper<HallplanController> wrapper =
                loader.loadAndWrap("/fxml/ticket/hallplan.fxml");
            Node root = loader.load("/fxml/ticket/hallplan.fxml");
            HallplanController c = wrapper.getController();
            c.initializeData(event, mainController.getCutsomer(), myContainer, mainController.getEventTab() );
            mainController.getEventTab().setContent(root);
        }
    }

    @Override
    public void update() {
        lblArtist.setText(BundleManager.getBundle().getString("events.artist"));
        lblPriceText.setText(BundleManager.getBundle().getString("events.price") + ": ");
    }
}
