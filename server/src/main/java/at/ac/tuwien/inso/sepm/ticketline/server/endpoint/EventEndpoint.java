package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.binding.MultiValueBinding;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

    @RequestMapping(value = "/{pageIndex}/{eventsPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple upcoming event entries")
    public Page<SimpleEventDTO> findAllUpcomingAsc(@PathVariable("pageIndex")int pageIndex, @PathVariable("eventsPerPage")int eventsPerPage) {
        //mapping of Event to EventDTO
        Pageable request = new PageRequest(pageIndex, eventsPerPage, Sort.Direction.ASC, "start_of_event");
        Page<Event> eventPage = eventService.findAllUpcomingAsc(request);
        List<SimpleEventDTO> dtos = eventMapper.eventToSimpleEventDTO(eventPage.getContent());
        return new PageImpl<>(dtos, request, eventPage.getTotalElements());
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

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Publish a new Event entry")
    public DetailedEventDTO publishEvent(@RequestBody DetailedEventDTO detailedEventDTO) {
        Event event = eventMapper.detailedEventDTOToEvent(detailedEventDTO);
        event = eventService.publishEvent(event);
        return  eventMapper.eventToDetailedEventDTO(event);
    }

/*    @RequestMapping(method = RequestMethod.GET, value = "/advSearch/{pageIndex}/{eventsPerPage}\"")
    @ResponseBody
    public Page<Event> search(@RequestParam(value = "search") String search,@PathVariable("pageIndex")int pageIndex, @PathVariable("eventsPerPage")int eventsPerPage) {
        Pageable request = new PageRequest(pageIndex, eventsPerPage, Sort.Direction.ASC, "start_of_event");
        return eventService.findByAdvancedSearch(search, request);
    }*/

    @RequestMapping(value = "advSearch/{pageIndex}/{eventsPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple upcoming event entries")
    public Page<SimpleEventDTO> findAdvanced(@PathVariable("pageIndex")int pageIndex, @PathVariable("eventsPerPage")int eventsPerPage,
                                             @QuerydslPredicate(root = Event.class)Predicate predicate,
                                             @RequestParam HashMap<String,String> parameters) {
        Pageable request = new PageRequest(pageIndex, eventsPerPage, Sort.Direction.ASC, "start_of_event");
        System.out.println("PARAMS: " + parameters.toString());
        Page<Event> eventPage = eventService.findByAdvancedSearch(parameters, request);
        List<SimpleEventDTO> dtos = eventMapper.eventToSimpleEventDTO(eventPage.getContent());
        System.out.println("LIST::: " +dtos.size());
        return new PageImpl<>(dtos, request, eventPage.getTotalElements());
    }

}
