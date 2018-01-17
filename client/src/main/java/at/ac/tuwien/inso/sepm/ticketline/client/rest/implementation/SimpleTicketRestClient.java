package at.ac.tuwien.inso.sepm.ticketline.client.rest.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.EmptyValueException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.SearchNoMatchException;
import at.ac.tuwien.inso.sepm.ticketline.client.exception.TicketAlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.TicketRestClient;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.List;

@Component
public class SimpleTicketRestClient implements TicketRestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleTicketRestClient.class);
    private static final String TICKET_URL = "/tickets";

    private final RestClient restClient;

    public SimpleTicketRestClient( RestClient restClient ) {
        this.restClient = restClient;
    }

    @Override
    public TicketDTO findOneById( Long id ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving ticket by id from {}", restClient.getServiceURI(TICKET_URL));
            ResponseEntity<TicketDTO> ticket =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/"+id),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<TicketDTO>() {}
                );
            LOGGER.debug("Result status was {} with content {}", ticket.getStatusCode(), ticket.getBody());
            return ticket.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve ticket with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<TicketDTO> findByEventId( Long eventId ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving ticket by eventId from {}", restClient.getServiceURI(TICKET_URL));
            ResponseEntity<List<TicketDTO>> tickets =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/event/"+eventId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TicketDTO>>() {}
                );
            LOGGER.debug("Result status was {} with content {}", tickets.getStatusCode(), tickets.getBody());
            return tickets.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve tickets with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<TicketDTO> findByCustomerId(Long customerId) throws DataAccessException {
        return null;
    }


    public List<TicketDTO> findByTicketId( Long customerId ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving ticket by customerId from {}", restClient.getServiceURI(TICKET_URL));
            ResponseEntity<List<TicketDTO>> tickets =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/customer/"+customerId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<TicketDTO>>() {}
                );
            LOGGER.debug("Result status was {} with content {}", tickets.getStatusCode(), tickets.getBody());
            return tickets.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve tickets with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }    }



    @Override
    public Boolean isBooked( Long eventId, Long seatId ) throws DataAccessException {
        try {
            LOGGER.debug("Check if seat is already booked for event from {}", restClient.getServiceURI(TICKET_URL));
            ResponseEntity<Boolean> isBooked =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/isBooked/"+eventId+"/"+seatId),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Boolean>() {}
                );
            LOGGER.debug("Result status was {} with content {}", isBooked.getStatusCode(), isBooked.getBody());
            return isBooked.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve information with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<TicketDTO> save( List<TicketDTO> ticketDTOS ) throws DataAccessException, TicketAlreadyExistsException, EmptyValueException {
        try {
            LOGGER.debug("Save tickets");
            HttpEntity<List<TicketDTO>> httpEntity = new HttpEntity<>(ticketDTOS);
            ResponseEntity<List<TicketDTO>> tickets = restClient.exchange(
                restClient.getServiceURI(TICKET_URL),
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<List<TicketDTO>>() {});
            return tickets.getBody();
        } catch (HttpStatusCodeException e) {
            if(e.getStatusCode()== HttpStatus.CONFLICT){
                throw new TicketAlreadyExistsException("The ticket is already occupied." + e.getStatusCode().toString());
            }else if(e.getStatusCode()==HttpStatus.NOT_ACCEPTABLE){
                throw new EmptyValueException();
            }
            else {
                throw new DataAccessException("Failed save ticket with status code " + e.getStatusCode().toString());
            }
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public int ticketCountForEventForSector( Long event_id, char sector ) throws DataAccessException {
        try {
            LOGGER.debug("Get the number of tickets booked in sector for event from {}", restClient.getServiceURI(TICKET_URL));
            ResponseEntity<Integer> count =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/event/"+event_id+"/"+sector),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Integer>() {}
                );
            LOGGER.debug("Result status was {} with content {}", count.getStatusCode(), count.getBody());
            return count.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve count of tickets with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<SeatDTO> findFreeSeatsForEventInSector( Long event_id, char sector ) throws DataAccessException {
        try {
            LOGGER.debug("Retrieving free seats for event in sector from {}", restClient.getServiceURI(TICKET_URL));
            ResponseEntity<List<SeatDTO>> seats =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/isFree/"+event_id+"/"+sector),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<SeatDTO>>() {}
                );
            LOGGER.debug("Result status was {} with content {}", seats.getStatusCode(), seats.getBody());
            return seats.getBody();
        } catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed retrieve seats with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Page<TicketDTO> findByCustomerName(String name, Pageable request) throws DataAccessException, SearchNoMatchException {
        try {
            LOGGER.debug("Retrieving all tickets with a certen customer name from {}", restClient.getServiceURI(TICKET_URL+"/"+request.getPageNumber()+"/"+request.getPageSize() ));
            ResponseEntity<RestResponsePage<TicketDTO>> customer =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/"+name+"/"+request.getPageNumber()+"/"+request.getPageSize()),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<TicketDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) {
                throw new SearchNoMatchException();
            }
            throw new DataAccessException("Failed retrieve tickets with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public Page<TicketDTO> findByReservationNumber(Long reservationNumber, Pageable request) throws DataAccessException, SearchNoMatchException {
        try {
            LOGGER.debug("Retrieving all tickets from {}", restClient.getServiceURI(TICKET_URL+"/"+request.getPageNumber()+"/"+request.getPageSize() ));
            ResponseEntity<RestResponsePage<TicketDTO>> customer =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/searchResNr"+"/"+reservationNumber+"/"+request.getPageNumber()+"/"+request.getPageSize()),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<TicketDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) {
                throw new SearchNoMatchException();
            }
            throw new DataAccessException("Failed retrieve tickets with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteTicketByTicket_Id(Long ticket_Id) throws DataAccessException {
        //try {
            LOGGER.debug("removing news from users notSeen from {}", restClient.getServiceURI(TICKET_URL));
            ResponseEntity ticket =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL + "/" + "deleteTicket" + "/" + ticket_Id),
                    HttpMethod.POST,
                    null,
                    new ParameterizedTypeReference<Long>() {
                    }
                );
            LOGGER.debug("Result status was {} with content {}", ticket.getStatusCode(), ticket.getBody());
        /*} catch (HttpStatusCodeException e) {
            throw new DataAccessException("Failed to update user with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }*/
    }

    @Override
    public Page<TicketDTO> findAll(Pageable request) throws DataAccessException, SearchNoMatchException {
        try {
            LOGGER.debug("Retrieving all tickets from {}", restClient.getServiceURI(TICKET_URL+"/"+request.getPageNumber()+"/"+request.getPageSize() ));
            ResponseEntity<RestResponsePage<TicketDTO>> customer =
                restClient.exchange(
                    restClient.getServiceURI(TICKET_URL+"/"+request.getPageNumber()+"/"+request.getPageSize()),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<RestResponsePage<TicketDTO>>() {
                    });
            LOGGER.debug("Result status was {} with content {}", customer.getStatusCode(), customer.getBody());
            return customer.getBody();
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().value() == 404) {
                throw new SearchNoMatchException();
            }
            throw new DataAccessException("Failed retrieve tickets with status code " + e.getStatusCode().toString());
        } catch (RestClientException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
