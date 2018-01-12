package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Location;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.AlreadyExistsException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.IllegalValueException;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.ArtistRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.HallRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SimpleEventService implements EventService {

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
    public Event findOne( Long id ) {
        return eventRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Page<Event> findAllUpcomingAsc(Pageable request ) {
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


    @Override
    public Event publishEvent(Event event) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (event.getStartOfEvent().isEqual(event.getEndOfEvent()) || event.getStartOfEvent().isAfter(event.getEndOfEvent())) {
            throw new IllegalValueException("Enddate before Startdate");
        }

        // Find Location
        Location location = locationRepository.findOneByDescription(event.getHall().getLocation().getDescription());
        if (location == null) {
            throw new NotFoundException("Location " + event.getHall().getLocation().getDescription());
        }

        // Find Hall
        Hall hall = hallRepository.findOneByDescriptionAndLocation(location.getId(),event.getHall().getDescription());

        if (hall == null) {
            throw new NotFoundException("Hall " + event.getHall().getDescription() + " " + location.getDescription());
        }

        hall.setLocation(location);
        event.setHall(hall);


        // Duplicates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String start = event.getStartOfEvent().format(formatter);
        String end = event.getEndOfEvent().format(formatter);

        List<Event> duplicates = eventRepository.findDuplicates(event.getTitle(), event.getDescription(), event.getHall().getId(), start, end);

        if (duplicates.size() > 0)
            throw new AlreadyExistsException("Event: " + event.getTitle());


        List<Artist> newartistList = new ArrayList<>();
        for (Artist artist : event.getArtists()) {
            Artist newArtist = artistRepository.findByArtistFirstNameAndArtistLastName(artist.getArtistFirstName(),artist.getArtistLastName());
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

}
