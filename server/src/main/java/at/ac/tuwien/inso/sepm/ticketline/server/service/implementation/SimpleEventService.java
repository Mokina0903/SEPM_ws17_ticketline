package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
public class SimpleEventService implements EventService {

    private final EventRepository eventRepository;

    public SimpleEventService( EventRepository eventRepository ) {
        this.eventRepository = eventRepository;
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
    public List<Event> findAllUpcomingAsc( Pageable request ) {
        Page<Event> page = eventRepository.findAllUpcoming(request);
        return page.getContent();
    }

    @Override
    public Event publishEvent(Event event) {
        // TODO: David Implement here
        return null;
    }

}
