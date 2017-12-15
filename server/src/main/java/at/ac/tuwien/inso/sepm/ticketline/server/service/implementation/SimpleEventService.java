package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Event;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.EventRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.EventService;

import java.util.List;

public class SimpleEventService implements EventService {

    private final EventRepository eventRepository;

    public SimpleEventService( EventRepository eventRepository ) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAllOrderByStartOfEventDesc();
    }

    @Override
    public Event findOne( Long id ) {
        return eventRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }
}
