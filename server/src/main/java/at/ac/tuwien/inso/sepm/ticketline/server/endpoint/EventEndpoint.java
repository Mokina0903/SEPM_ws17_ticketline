package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.StatistikRequest;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.event.EventMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        Event event = null;
        try {
            event = eventMapper.detailedEventDTOToEvent(detailedEventDTO);
        } catch (NullPointerException e) {
            throw new EmptyFieldException(e.getMessage());
        }

        event = eventService.publishEvent(event);
        return  eventMapper.eventToDetailedEventDTO(event);
    }

    @RequestMapping(value ="/getTopTen/{start}/{end}" ,method = RequestMethod.GET)
    @ApiOperation(value = "Get the top ten events from the given month")
    public List<SimpleEventDTO> getTop10EventsOfMonth(@PathVariable("start") Long start, @PathVariable("end")Long end){

        LocalDateTime startOfMonth = LocalDateTime.ofInstant(Instant.ofEpochSecond(start), ZoneId.of("Europe/Paris"));
        LocalDateTime endOfMonth = LocalDateTime.ofInstant(Instant.ofEpochSecond(end), ZoneId.of("Europe/Paris"));

        return eventMapper.eventToSimpleEventDTO(eventService.getTop10EventsOfMonth(startOfMonth, endOfMonth));
    }

}
