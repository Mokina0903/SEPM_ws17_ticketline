package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.ticket.TicketDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.ticket.TicketMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.TicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get information about a specific ticket entry")
    public List<TicketDTO> findByEvent( @PathVariable Long eventId) {
        return ticketMapper.ticketToTicketDTO(ticketService.findByEventId(eventId));
    }

}
