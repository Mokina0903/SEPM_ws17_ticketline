package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.MyEventPredicatesBuilder;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SimpleEventService implements EventService {

    private static final Logger LOGGER = LoggerFactory.getLogger(at.ac.tuwien.inso.sepm.ticketline.server.service.implementation.SimpleEventService.class);


    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final HallRepository hallRepository;
    private final ArtistRepository artistRepository;

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
        //get whole list of repository and create a page
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

    //QueryDsl
    public Page<Event> findAllEventsByName(Pageable request, String title) {
        MyEventPredicatesBuilder builder = new MyEventPredicatesBuilder().with("title", ":", title);
        Iterable<Event> events = eventRepository.findAll(builder.build());
        List<Event> eventList = Lists.newArrayList(events);
        int start = request.getOffset();
        int end = (start + request.getPageSize()) > eventList.size() ? eventList.size() : (start + request.getPageSize());
        return new PageImpl<>(eventList.subList(start, end), request, eventList.size());
    }

    @Override
    public Page<Event> findByAdvancedSearch(String search, Pageable request) {
        MyEventPredicatesBuilder builder = new MyEventPredicatesBuilder();
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

        for (Event e : events) {
            System.out.println(e.getTitle() + "," + e.getPrice() + "; ");
        }

        List<Event> eventList = Lists.newArrayList(events);
        return new PageImpl<>(eventList, request, eventList.size());
    }


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
