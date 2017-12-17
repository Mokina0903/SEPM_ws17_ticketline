package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/event")
@Api(value = "event")
public class EventEndpoint {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventEndpoint( EventService eventService, EventMapper eventMapper ) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple event entries")
    public List<SimpleEventDTO> findAll() {
        return eventMapper.eventToSimpleEventDTO(eventService.findAll());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific event entry")
    public DetailedEventDTO findOneById( @PathVariable Long id) {
        return eventMapper.eventToDetailedEventDTO(eventService.findOne(id));
    }
}
