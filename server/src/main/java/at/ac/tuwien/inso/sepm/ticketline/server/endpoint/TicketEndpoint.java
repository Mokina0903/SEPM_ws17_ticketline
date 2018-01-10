package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
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

    public TicketEndpoint( TicketService ticketService, TicketMapper ticketMapper, EventService eventService ) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
        this.eventService = eventService;
    }

    @RequestMapping(value = "/{ticketId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific ticket entry")
    public TicketDTO find( @PathVariable Long ticketId) {
        return ticketMapper.ticketToTicketDTO(ticketService.findOneById(ticketId));
    }

    @RequestMapping(value = "/event/{eventId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about ticket entries by event")
    public List<TicketDTO> findByEventId( @PathVariable Long eventId) {
        return ticketMapper.ticketToTicketDTO(ticketService.findByEventId(eventId));
    }

    @RequestMapping(value = "/event/{eventId}/{sector}", method = RequestMethod.GET)
    @ApiOperation(value = "Get number of ticket entries by event and sector")
    public int ticketCountForEventForSector( @PathVariable Long eventId, @PathVariable char sector) {
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

        /*if(ticketDTOS==null || ticketDTOS.isEmpty()){
            return ticketDTOS;
        }
        List<Ticket> tickets = new ArrayList<>();

        for (TicketDTO ticketDTO : ticketDTOS){
            Ticket ticket = ticketMapper.ticketDTOtoTicket(ticketDTO);
            ticket.setEvent(eventService.findOne(ticketDTO.getEvent().getId()));
            tickets.add(ticket);
        }*/

        List<Ticket> tickets = ticketService.save(ticketMapper.ticketDTOToTicket(ticketDTOS));
        return ticketMapper.ticketToTicketDTO(tickets);
    }

    @RequestMapping(value = "/isBooked/{eventId}/{seatId}", method = RequestMethod.GET)
    @ApiOperation(value = "Check if seat is booked for the event")
    public Boolean isBooked( @PathVariable Long eventId, @PathVariable Long seatId) {
        return ticketService.isBooked(eventId,seatId);
    }

    //todo: getFreeSeatsInSector(Event,Char), getTotalCountOfSeatsInSector(Hall,Char)

}
