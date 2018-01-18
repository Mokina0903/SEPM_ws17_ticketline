package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.customer.CustomerDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Customer;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat.SeatMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.InvalidIdException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.OldVersionException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/tickets")
@Api(value = "tickets")
public class TicketEndpoint {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final EventService eventService;
    private final SeatMapper seatMapper;
    private final LocationService locationService;

    public TicketEndpoint( TicketService ticketService, TicketMapper ticketMapper, EventService eventService, SeatMapper seatMapper, LocationService locationService ) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.eventService = eventService;
        this.seatMapper = seatMapper;
        this.locationService = locationService;
    }

    @RequestMapping(value = "/{ticketId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific ticket entry")
    public TicketDTO find( @PathVariable Long ticketId) {
        return ticketMapper.ticketToTicketDTO(ticketService.findOneById(ticketId));
    }

    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about ticket entries by event")
    public List<TicketDTO> findByEventId( @PathVariable Long eventId) {
        ticketService.setTicketsFreeIf30MinsBeforEvent();
        return ticketMapper.ticketToTicketDTO(ticketService.findByEventId(eventId));
    }

    @RequestMapping(value = "/event/{eventId}/{sector}", method = RequestMethod.GET)
    @ApiOperation(value = "Get number of ticket entries by event and sector")
    public int ticketCountForEventForSector( @PathVariable Long eventId, @PathVariable char sector)
    {
        ticketService.setTicketsFreeIf30MinsBeforEvent();
        return ticketService.ticketCountForEventForSector(eventId,sector);
    }

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about ticket entries by customer")
    public List<TicketDTO> findByCustomerId( @PathVariable Long customerId) {
        return ticketMapper.ticketToTicketDTO(ticketService.findByCustomerId(customerId));
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation(value = "create ticket entry")
    public List<TicketDTO> create(@RequestBody List<TicketDTO> ticketDTOS) {
        if(ticketDTOS==null || ticketDTOS.isEmpty()){
            throw new EmptyFieldException();
        }
        List<Ticket> tickets = new ArrayList<>();

        for (TicketDTO ticketDTO : ticketDTOS){
            Ticket ticket = ticketMapper.ticketDTOtoTicket(ticketDTO);
            ticket.setEvent(eventService.findOne(ticketDTO.getEvent().getId()));
            ticket.calculatePrice();
            tickets.add(ticket);
        }

        tickets =ticketService.save(tickets);
        return ticketMapper.ticketToTicketDTO(tickets);
    }

    @RequestMapping(value = "/isBooked/{eventId}/{seatId}", method = RequestMethod.GET)
    @ApiOperation(value = "Check if seat is booked for the event")
    public Boolean isBooked( @PathVariable Long eventId, @PathVariable Long seatId) {
        ticketService.setTicketsFreeIf30MinsBeforEvent();
        return ticketService.isBooked(eventId,seatId);
    }

    @RequestMapping(value = "/isFree/{eventId}/{sector}", method = RequestMethod.GET)
    @ApiOperation(value = "Search for free seats for event in sector")
    public List<SeatDTO> freeSeatsForEventInSector( @PathVariable Long eventId, @PathVariable char sector) {
        ticketService.setTicketsFreeIf30MinsBeforEvent();
        return seatMapper.seatToSeatDTO(locationService.findFreeSeatsForEventInSector(eventId,sector));
    }
    //todo: getFreeSeatsInSector(Event,Char), getTotalCountOfSeatsInSector(Hall,Char)

    @RequestMapping(value= "/{pageIndex}/{ticketsPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of ticket entries")
    public Page<TicketDTO> findAll(@PathVariable("pageIndex")int pageIndex, @PathVariable("ticketsPerPage")int ticketsPerPage){
        ticketService.setTicketsFreeIf30MinsBeforEvent();
        Pageable request = new PageRequest(pageIndex, ticketsPerPage, Sort.Direction.ASC, "id");
        Page<Ticket> customerPage = ticketService.findAll(request);
        List<TicketDTO> dtos = ticketMapper.ticketToTicketDTO(customerPage.getContent());
        return new PageImpl<>(dtos, request, customerPage.getTotalElements());
    }

    @RequestMapping(value= "/{customerName}/{pageIndex}/{ticketsPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of ticket entries")
    public Page<TicketDTO> findAllByCustomerName(@PathVariable("customerName") String customerName, @PathVariable("pageIndex")int pageIndex, @PathVariable("ticketsPerPage")int ticketsPerPage){
        ticketService.setTicketsFreeIf30MinsBeforEvent();
        Pageable request = new PageRequest(pageIndex, ticketsPerPage);
        Page<Ticket> tickets = ticketService.findAllByCustomerName(customerName, request);
        List<TicketDTO> dtos = ticketMapper.ticketToTicketDTO(tickets.getContent());
        return new PageImpl<>(dtos, request, tickets.getTotalElements());
    }

    @RequestMapping(value= "/searchResNr/{reservationNumber}/{pageIndex}/{ticketsPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of ticket entries")
    public Page<TicketDTO> findAllByReservationNumber(@PathVariable("reservationNumber") Long reservationNumber, @PathVariable("pageIndex")int pageIndex, @PathVariable("ticketsPerPage")int ticketsPerPage){
        ticketService.setTicketsFreeIf30MinsBeforEvent();
        Pageable request = new PageRequest(pageIndex, ticketsPerPage);
        Page<Ticket> tickets = ticketService.findAllByReservationNumber(reservationNumber, request);
        List<TicketDTO> dtos = ticketMapper.ticketToTicketDTO(tickets.getContent());
        return new PageImpl<>(dtos, request, tickets.getTotalElements());
    }


    @RequestMapping(value= "/deleteTicket/{ticketID}", method = RequestMethod.POST)
    @ApiOperation(value = "Delete one ticket with a certain ID")
    public void deleteTicket(@PathVariable Long ticketID) throws OldVersionException, NotFoundException {
        ticketService.deleteTicketByTicket_Id(ticketID);
    }

    @RequestMapping(value= "/payTickets/{reservationID}", method = RequestMethod.POST)
    @ApiOperation(value = "pay all tickets with a certain reservation number")
    public void payTickets(@PathVariable Long reservationID) throws OldVersionException, NotFoundException {
        ticketService.payTicketByReservation_Id(reservationID);
    }



}
