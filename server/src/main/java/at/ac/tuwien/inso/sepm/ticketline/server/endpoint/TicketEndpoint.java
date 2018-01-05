package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Ticket;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tickets")
@Api(value = "tickets")
public class TicketEndpoint {

    private final TicketService ticketService;
    private final TicketMapper ticketMapper;

    public TicketEndpoint( TicketService ticketService, TicketMapper ticketMapper ) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
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

    @RequestMapping(value = "/customer/{customerId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about ticket entries by customer")
    public List<TicketDTO> findByCustomerId( @PathVariable Long customerId) {
        return ticketMapper.ticketToTicketDTO(ticketService.findByCustomerId(customerId));
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('USER')")
    @ApiOperation(value = "create ticket entry")
    public TicketDTO create(@RequestBody TicketDTO ticketDTO) {
        Ticket ticket = ticketMapper.ticketDTOtoTicket(ticketDTO);
        ticket = ticketService.save(ticket);
        return ticketMapper.ticketToTicketDTO(ticket);
    }
}
