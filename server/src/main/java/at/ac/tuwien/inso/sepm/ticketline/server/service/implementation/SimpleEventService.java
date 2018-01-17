package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.QEvent;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.*;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter.EventFilter;
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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Page<Event> findAllUpcomingByTitle(Pageable request, String title) {
        //todo
        return null;
    }


    // https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/
    // http://www.baeldung.com/rest-api-search-language-spring-data-specifications
    private QEvent event = QEvent.event;
    private BooleanExpression eventHasTitle = event.title.eq("test");
    private BooleanExpression eventInFuture = event.startOfEvent.after(LocalDateTime.now());

/*    private Page<Event> findByTitleInFuture() {
        Iterable<Event> list = eventRepository.findAll(eventHasTitle.and(eventInFuture));
        List<Event> eventList = Lists.newArrayList(events);
        return new PageImpl<>(eventList, request, eventList.size());
    }*/

 /* join:
 * https://stackoverflow.com/questions/23837988/querydsl-jpql-how-to-build-a-join-query
 * */

    //todo implement search with or

    @Override
    public Page<Event> findByAdvancedSearch(HashMap<String, String> parameters, Pageable request) {
        Predicate predicate = filterBuilder.build(new EventFilter(parameters));
        System.out.println("Service: Predicate *******" + predicate.toString() );
        Iterable<Event> events = eventRepository.findAll(predicate);
        List<Event> eventList = Lists.newArrayList(events);
        System.out.println("ListSIze: :::::::::::::" + eventList.size());
        return new PageImpl<>(eventList, request, eventList.size());
    }

   /* @Override
    public Page<Event> findByAdvancedSearch(String search, Pageable request) {
        MyPredicatesBuilder builder = new MyPredicatesBuilder("event");
        if (search != null) {
            try {
                search = URLDecoder.decode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn("Error while encoding search path");
            }

            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)([a-zA-Z0-9_.\\s]+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        Iterable<Event> events = eventRepository.findAll(builder.build());

        List<Event> eventList = Lists.newArrayList(events);
        return new PageImpl<>(eventList, request, eventList.size());
    }
*/

    @Override
    public Event publishEvent(Event event) {
        // TODO; David Implent here asdf

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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

        event.setHall(hall);

        List<Artist> newArtistsList = new ArrayList<>();

        for (Artist artist : event.getArtists()) {
            Artist newArtist = artistRepository.findByArtistFirstNameAndArtistLastName(artist.getArtistFirstName(), artist.getArtistLastName());
            if (newArtist == null) {
                throw new NotFoundException("Artist " + artist.getArtistFirstName() + " " + artist.getArtistLastName());
            }
            newArtistsList.add(newArtist);
        }

        event.setArtists(newArtistsList);


        // TODO: Implement here verification if necessary (two events same Time)

        Event rEvent = eventRepository.save(event);

        return rEvent;
    }

}
