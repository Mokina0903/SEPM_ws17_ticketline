package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationObserver;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.LocalizationSubject;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.ticket.HallplanController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;
import org.controlsfx.glyphfont.GlyphFontRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EventElementController implements LocalizationObserver {

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
    public Label lblPrice;
    @FXML
    public VBox vbElement;
    @FXML
    public Label lblPriceText;

    @FXML
    public Label lblArtist;
    @FXML
    public Label lblFreeTickets;

    private EventService eventService;
    private final TicketService ticketService;
    private SimpleEventDTO simpleEventDTO;
    private MainController mainController;
    private SpringFxmlLoader loader;

    Node myContainer;


    @Autowired
    private LocalizationSubject localizationSubject;

    public EventElementController( TicketService ticketService, MainController mainController, SpringFxmlLoader loader ) {
        this.ticketService = ticketService;
        this.mainController = mainController;
        this.loader = loader;
    }

    @FXML
    private void initialize() {
        localizationSubject.attach(this);
    }

    public void initializeData(EventService eventService, SimpleEventDTO simpleEventDTO, Node myContainer) {

        this.simpleEventDTO = simpleEventDTO;
        this.eventService = eventService;
        this.myContainer = myContainer;
        String artistString = "";

        if(simpleEventDTO.getArtists()!=null && !simpleEventDTO.getArtists().isEmpty()){
            for(SimpleArtistDTO artistDTO:simpleEventDTO.getArtists()){
                artistString+="#"+artistDTO.getArtistFirstName()+" "+artistDTO.getArtistLastName()+" ";
            }
        }

        lblStartDate.setText(BundleManager.getBundle().getString("event.from")+": "+EVENT_DTF.format(simpleEventDTO.getStartOfEvent())+" -->");
        lblEndDate.setText(BundleManager.getBundle().getString("event.to")+": "+EVENT_DTF.format(simpleEventDTO.getEndOfEvent()));
        lblTitle.setText(simpleEventDTO.getTitle());
        lblArtist.setText(artistString);
        lblPrice.setText(simpleEventDTO.getPriceInEuro()+"\u20ac");
        lblText.setMaxWidth(400);
        lblText.setText(simpleEventDTO.getDescription());

        lblPriceText.setText(BundleManager.getBundle().getString("events.price") + ": ");

        Glyph glyph =GlyphFontRegistry.font("FontAwesome").create(FontAwesome.Glyph.valueOf("TICKET"));
        glyph.setColor(Color.GRAY);
        //glyph.setFontSize(FONT_SIZE);
        ticketReservationButton.setGraphic(glyph);

        long ticketCountTotal = simpleEventDTO.getSeatCount();
        long ticketCount=0;
        try {
             ticketCount= ticketService.countByEvent_Id(simpleEventDTO.getId());
        } catch (DataAccessException e) {
            mainController.showGeneralError(BundleManager.getBundle().getString("exception.unexpected"));
        }
        lblFreeTickets.setText(ticketCount+"/"+ticketCountTotal);
        if(ticketCount==ticketCountTotal){
            lblFreeTickets.setStyle("-fx-text-fill : red");
        }else if((ticketCountTotal-ticketCount)<=15){
            lblFreeTickets.setStyle("-fx-text-fill:orange");
        }else{
            lblFreeTickets.setStyle("-fx-text-fill:green");

        }
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
        lblStartDate.setText(BundleManager.getBundle().getString("event.from")+": "+EVENT_DTF.format(simpleEventDTO.getStartOfEvent())+" -->");
        lblEndDate.setText(BundleManager.getBundle().getString("event.to")+": "+EVENT_DTF.format(simpleEventDTO.getEndOfEvent()));
        lblPriceText.setText(BundleManager.getBundle().getString("events.price") + ": ");
    }
}
