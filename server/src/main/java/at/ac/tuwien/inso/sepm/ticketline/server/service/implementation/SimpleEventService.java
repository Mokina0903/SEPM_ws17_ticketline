package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

}
