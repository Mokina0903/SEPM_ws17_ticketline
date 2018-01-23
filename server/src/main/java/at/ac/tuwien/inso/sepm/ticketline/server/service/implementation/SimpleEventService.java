package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.AlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.EmptyFieldException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalValueException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.EventFilter;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.SimpleArtistFilterBuilder;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.SimpleEventFilterBuilder;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import com.google.common.collect.Lists;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SimpleEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleEventService.class);


    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final HallRepository hallRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    SimpleEventFilterBuilder filterBuilder;
    @Autowired
    SimpleArtistFilterBuilder artistFilterBuilder;

    public SimpleEventService(EventRepository eventRepository, LocationRepository locationRepository, HallRepository hallRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.hallRepository = hallRepository;
        this.artistRepository = artistRepository;
    }


    @Override
    public List<Event> findAll() {
        return eventRepository.findAllByOrderByStartOfEventDesc();
    }

    @Override
    public Event findOne(Long id) {
        return eventRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<Event> findAllUpcomingAsc(Pageable request) {
        List<Event> events = eventRepository.findAllUpcoming();
        int start = request.getOffset();
        int end = (start + request.getPageSize()) > events.size() ? events.size() : (start + request.getPageSize());
        return new PageImpl<>(events.subList(start, end), request, events.size());
    }


    @Override
    public Page<Event> findByAdvancedSearch(HashMap<String, String> parameters, Pageable request) {
        parameters = replaceUnderscores(parameters);
        Predicate predicate = filterBuilder.buildAnd(new EventFilter(parameters));
        System.out.println("PRED::::::::::::::::::::::::::. " +predicate);
        return eventRepository.findAll(predicate, request);
    }

    @Override
    public Page<Event> findAllByArtistId( Long artistId, Pageable request ) {
        return eventRepository.findAllByArtistId(artistId,request);
    }

    @Override
    public Page<Event> findAllByLocationId(Long locationId, Pageable request) {
        return eventRepository.findAllByLocationId(locationId, request);
    }

    @Override
    public Page<Event> find(HashMap<String, String> parameters, Pageable request) {
        parameters = replaceUnderscores(parameters);
        Predicate predicate = filterBuilder.buildOr(new EventFilter(parameters));
        return eventRepository.findAll(predicate, request);
    }


    private HashMap<String, String> replaceUnderscores(HashMap<String, String> parameters) {
        for(String key : parameters.keySet()) {
            String replace = parameters.get(key);
            if (replace.contains("_")) {
                replace = replace.replaceAll("_", " ").toLowerCase();
                parameters.put(key, replace);
                System.out.println("Replacing _");
            }
        }
        return parameters;
    }

    @Override
    public Event publishEvent(Event event) {
        if (event.getStartOfEvent().isEqual(event.getEndOfEvent()) || event.getEndOfEvent().isBefore(event.getStartOfEvent()))
            throw new IllegalValueException("end before start");

        if (event.getDescription().isEmpty() || event.getTitle().isEmpty())
            throw new EmptyFieldException("");

        if (event.getPrice() <= 0)
            throw new IllegalValueException("Price <= 0");

        // Find Location
        Location location = locationRepository.findOneByDescription(event.getHall().getLocation().getDescription());
        if (location == null) {
            throw new NotFoundException("Location " + event.getHall().getLocation().getDescription());
        }

        // Find Hall
        Hall hall = hallRepository.findOneByDescriptionAndLocation(location.getId(), event.getHall().getDescription());

        if (hall == null) {
            throw new NotFoundException("Hall " + event.getHall().getDescription() + " " + location.getDescription());
        }

        hall.setLocation(location);
        event.setHall(hall);

        if (event.getEventCategory() == null)
            throw new EmptyFieldException("Category");


        // Duplicates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String start = event.getStartOfEvent().format(formatter);
        String end = event.getEndOfEvent().format(formatter);

        List<Event> duplicates = eventRepository.findDuplicates(event.getTitle(), event.getDescription(), event.getHall().getId(), start, end);

        if (duplicates.size() > 0)
            throw new AlreadyExistsException("Event: " + event.getTitle());


        List<Artist> newartistList = new ArrayList<>();
        if (event.getArtists().size() == 0) {
            throw new EmptyFieldException("Artist list empty");
        }

        for (Artist artist : event.getArtists()) {
            Artist newArtist = artistRepository.findByArtistFirstNameAndArtistLastName(artist.getArtistFirstName(), artist.getArtistLastName());
            if (newArtist == null) {
                artist.setId(0L);
                newArtist = artistRepository.save(artist);
            }
            newartistList.add(newArtist);
        }

        event.setArtists(newartistList);


        Event rEvent = eventRepository.save(event);

        return rEvent;
    }

    @Override
    public List<Event> getTop10EventsOfMonth(LocalDateTime beginOfMonth, LocalDateTime endOfMonth) {
        return eventRepository.findTopTenEventsOfMonth( Timestamp.valueOf(beginOfMonth), Timestamp.valueOf(endOfMonth));
    }

    @Override
    public List<Event> getTop10EventsOfMonthFilteredByCategory(LocalDateTime beginOfMonth, LocalDateTime endOfMonth, String category) {
        return eventRepository.findTopTenEventsOfMonthFilteredByCategory(Timestamp.valueOf(beginOfMonth), Timestamp.valueOf(endOfMonth), category);
    }



}
