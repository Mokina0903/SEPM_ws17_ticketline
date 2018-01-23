package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.TicketAlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SimpleTicketService implements TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketService.class);

    private final TicketRestClient ticketRestClient;

    public SimpleTicketService( TicketRestClient ticketRestClient ) {
        this.ticketRestClient = ticketRestClient;
    }

    @Override
    public TicketDTO findOneById( Long id ) throws DataAccessException {
        return ticketRestClient.findOneById(id);
    }

    @Override
    public Page<TicketDTO> findAll(Pageable request) throws DataAccessException, SearchNoMatchException {
        return ticketRestClient.findAll(request);
    }

    @Override
    public List<TicketDTO> findByEventId( Long eventId ) throws DataAccessException {
        return ticketRestClient.findByEventId(eventId);
    }

    @Override
    public List<TicketDTO> findByCustomerId( Long customerId ) throws DataAccessException {
        return ticketRestClient.findByCustomerId(customerId);
    }

    @Override
    public Boolean isBooked( Long eventId, Long seatId ) throws DataAccessException {
        return ticketRestClient.isBooked(eventId,seatId);
    }

    @Override
    public List<TicketDTO> save( List<TicketDTO> ticketDTOS ) throws DataAccessException, TicketAlreadyExistsException, EmptyValueException {
        return ticketRestClient.save(ticketDTOS);
    }

    @Override
    public int ticketCountForEventForSector( Long event_id, char sector ) throws DataAccessException {
        return ticketRestClient.ticketCountForEventForSector(event_id,sector);
    }

    @Override
    public List<SeatDTO> findFreeSeatsForEventInSector( Long event_id, char sector ) throws DataAccessException {
        return ticketRestClient.findFreeSeatsForEventInSector(event_id,sector);
    }

    @Override
    public Page<TicketDTO> findByCustomerName(String name, Pageable request) throws DataAccessException, SearchNoMatchException {
        return ticketRestClient.findByCustomerName(name,request);
    }

    public Page<TicketDTO> findByReservationNumber(Long reservationNumber, Pageable request) throws DataAccessException, SearchNoMatchException {
        return ticketRestClient.findByReservationNumber(reservationNumber, request);
    }

    @Override
    public void deleteTicketByTicket_Id(Long ticket_Id) throws DataAccessException {
        ticketRestClient.deleteTicketByTicket_Id(ticket_Id);
    }

    @Override
    public void payTicketByReservation_Id(Long reservation_Id) throws DataAccessException {
        ticketRestClient.payTicketByReservation_Id(reservation_Id);
    }



    @Override
    public void reservationPdf( List<TicketDTO> tickets, Window window) {

        File f;
        try {
            f = new File(getClass().getResource("/invoice_template/Reservation_Template.pdf").toURI());
        } catch(URISyntaxException e) {
            f = new File(getClass().getResource("/invoice_template/Reservation_Template.pdf").getPath());
        }

        try (PDDocument pdfDocument = PDDocument.load(f)) {

            PDAcroForm acroForm = pdfDocument.getDocumentCatalog().getAcroForm();

            if (acroForm != null && !tickets.isEmpty()) {
                // Retrieve an individual field and set its value.

                SimpleEventDTO event= tickets.get(0).getEvent();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                TicketDTO ticketDTO = tickets.get(0);

                PDTextField field = (PDTextField) acroForm.getFields().get(0);
                field.setValue("Ticketline 3 - Reservation");


                field = (PDTextField) acroForm.getFields().get(1);
                field.setValue("Ticketline 3");
                field = (PDTextField) acroForm.getFields().get(2);
                field.setValue("");
                field = (PDTextField) acroForm.getFields().get(3);
                field.setValue("");
                field = (PDTextField) acroForm.getFields().get(4);
                field.setValue("");

                field = (PDTextField) acroForm.getFields().get(5);
                field.setValue(ticketDTO.getCustomer().getName()+" "+ ticketDTO.getCustomer().getSurname());

                field = (PDTextField) acroForm.getFields().get(7);
                field.setValue("");
                field = (PDTextField) acroForm.getFields().get(9);
                field.setValue("");
                field = (PDTextField) acroForm.getFields().get(11);
                field.setValue("");

                field = (PDTextField) acroForm.getFields().get(6);
                field.setValue(ticketDTO.getReservationNumber()+"");
                field = (PDTextField) acroForm.getFields().get(8);
                field.setValue(ticketDTO.getReservationDate().format(formatter));
                field = (PDTextField) acroForm.getFields().get(10);
                field.setValue(ticketDTO.getReservationNumber()+"");
                field = (PDTextField) acroForm.getFields().get(12);
                field.setValue((event.getStartOfEvent().minus(Duration.ofMinutes(30))).format(formatter));

                field = (PDTextField) acroForm.getFields().get(13);
                field.setValue("Tickets reserved for  "+event.getTitle());

                List<TicketDTO> secA=new ArrayList<>();
                List<TicketDTO> secB=new ArrayList<>();
                List<TicketDTO> secC=new ArrayList<>();
                List<TicketDTO> secD=new ArrayList<>();
                List<TicketDTO> secE=new ArrayList<>();

                List<List<TicketDTO>> allSectors = new ArrayList<>();
                allSectors.add(secA);
                allSectors.add(secB);
                allSectors.add(secC);
                allSectors.add(secD);
                allSectors.add(secE);

                for(TicketDTO ticket:tickets){
                    if(!ticket.isDeleted()) {
                        switch (ticket.getSeat().getSector()) {
                            case 'a':
                                secA.add(ticket);
                                break;
                            case 'b':
                                secB.add(ticket);
                                break;
                            case 'c':
                                secC.add(ticket);
                                break;
                            case 'd':
                                secD.add(ticket);
                                break;
                            case 'e':
                                secE.add(ticket);
                                break;
                        }
                    }
                }

                int line=0;
                for(List<TicketDTO> sec:allSectors) {

                    if (!sec.isEmpty()) {
                        field = (PDTextField) acroForm.getFields().get(14+(line*4));
                        field.setValue(event.getTitle() + " Ticket(s) in Sector \""+sec.get(0).getSeat().getSector()+"\"");
                        field = (PDTextField) acroForm.getFields().get(15+(line*4));
                        field.setValue(sec.size() + "");
                        field = (PDTextField) acroForm.getFields().get(16+(line*4));
                        field.setValue(sec.get(0).getPriceInEuro() + " \u20ac");
                        field = (PDTextField) acroForm.getFields().get(17+(line*4));
                        field.setValue(getTotalPriceForTickets(sec.get(0).getPrice(),sec.size()) + " \u20ac");
                        if(line==3 || line==4){
                            field = (PDTextField) acroForm.getFields().get(17+(line*4));
                            field.setValue(event.getTitle() + " Ticket(s) in Sector \""+sec.get(0).getSeat().getSector()+"\"");
                            field = (PDTextField) acroForm.getFields().get(14+(line*4));
                            field.setValue(sec.size() + "");
                            field = (PDTextField) acroForm.getFields().get(15+(line*4));
                            field.setValue(sec.get(0).getPriceInEuro() + " \u20ac");
                            field = (PDTextField) acroForm.getFields().get(16+(line*4));
                            field.setValue(getTotalPriceForTickets(sec.get(0).getPrice(),sec.size()) + " \u20ac");
                        }
                        line++;
                    }
                }

                field = (PDTextField) acroForm.getFields().get(38);
                field.setValue(getTotalPriceWithoutTaxInEuro(tickets));
                field = (PDTextField) acroForm.getFields().get(39);
                field.setValue(getTotalTaxInEuro(tickets));
                field = (PDTextField) acroForm.getFields().get(40);
                field.setValue(getTotalPriceInEuro(tickets)+"\u20ac");

                for(PDField pdField :acroForm.getFields()){

                    pdField.setReadOnly(true);
                }
            }


            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialFileName("Reservation"+tickets.get(0).getReservationNumber()+".pdf");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("pdf File","*.pdf"));
            File file= fileChooser.showSaveDialog(window);
            if(file!=null) {
                pdfDocument.save(file);
            }

        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private String getTotalPriceInEuro(List<TicketDTO> tickets){
        long sum=0;
        for (TicketDTO ticketDTO:tickets){
            sum+=ticketDTO.getPrice();
        }
        return String.format("%.2f",((double)sum)/100d);
    }
    private String getTotalPriceForTickets(long price, int count){
        double total = price*count;
        total=total/100;
        return String.format("%.2f",total);
    }
    private String getTotalTaxInEuro(List<TicketDTO> tickets){
        long sum=0;
        for (TicketDTO ticketDTO:tickets){
            sum+=ticketDTO.getPrice();
        }
        Double tax = (double)sum*(1d-(100d/113d));
        return String.format("%.2f",tax/100d);
    }
    private String getTotalPriceWithoutTaxInEuro(List<TicketDTO> tickets){
        long sum=0;
        for (TicketDTO ticketDTO:tickets){
            sum+=ticketDTO.getPrice();
        }
        Double tax = (double)sum*(100d/113d);
        return String.format("%.2f",tax/100d);
    }

    @Override
    public Long countByEvent_Id(Long event_Id) throws DataAccessException {
        return ticketRestClient.countByEvent_Id(event_Id);
    }
}
