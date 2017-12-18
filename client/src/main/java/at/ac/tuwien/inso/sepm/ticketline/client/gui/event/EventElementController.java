package at.ac.tuwien.inso.sepm.ticketline.client.gui.event;

import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EventElementController {

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
    public VBox vbEventElement;
    @FXML
    public Label lblPriceText;
    @FXML
    public Label lblArtistName;
    @FXML
    public Label lblArtist;

    private EventService eventService;
    private SimpleEventDTO simpleEventDTO;

    public void initializeData( EventService eventService, SimpleEventDTO simpleEventDTO) {

        this.simpleEventDTO = simpleEventDTO;
        this.eventService = eventService;

        lblStartDate.setText(EVENT_DTF.format(simpleEventDTO.getStartOfEvent()));
        lblEndDate.setText(EVENT_DTF.format(simpleEventDTO.getEndOfEvent()));
        lblTitle.setText(simpleEventDTO.getTitle());
        lblArtistName.setText(simpleEventDTO.getArtistFirstName()+" "+simpleEventDTO.getArtistLastName());
        lblArtist.setText(BundleManager.getBundle().getString("events.artist"));
        lblPrice.setText(String.valueOf(simpleEventDTO.getPrice()));
        lblText.setMaxWidth(500);
        lblText.setText(simpleEventDTO.getDescriptionSummary());

        lblPriceText.setText(BundleManager.getBundle().getString("events.price")+": ");

        eventImageView.setVisible(false);
    }

    public void detailedEventInfo( MouseEvent mouseEvent ) {
    }

    public void ticketReservationForEvent( ActionEvent actionEvent ) {
    }
}
