package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
public class Event implements Predicatable{

    // TODO: David is correct?
    public enum EventCategory
    {
        Musical,
        Rock,
        Pop,
        Kabarett,
        Jazz,
        Kino
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_event_id")
    @SequenceGenerator(name = "seq_event_id", sequenceName = "seq_event_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 100)
    private String title;

    @Column(nullable = false)
    @Size(max = 10000)
    private String description;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private LocalDateTime startOfEvent;

    @Column(nullable = false)
    private LocalDateTime endOfEvent;

    @Column(nullable = false)
    private Long startOfEventTime;

    private Long duration;

    @Column(nullable = false)
    private Boolean seatSelection;

    @Column(nullable =  false)
    private EventCategory eventCategory;

    public Boolean getSeatSelection() {
        return seatSelection;
    }

    public void setSeatSelection( Boolean seatSelection ) {
        this.seatSelection = seatSelection;
    }

    @ManyToOne
    private Hall hall;

    @ManyToMany()
    @JoinTable(
        name = "artistsOfEvent",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "artist_id")) //toDo: make unique
    private List<Artist> artists = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice( long price ) {
        this.price = price;
    }

    public LocalDateTime getStartOfEvent() {
        return startOfEvent;
    }

    public void setStartOfEvent( LocalDateTime startOfEvent ) {
        this.startOfEvent = startOfEvent;
    }

    public LocalDateTime getEndOfEvent() {
        return endOfEvent;
    }

    public Long getStartOfEventTime() {
        return startOfEventTime;
    }

    public void setStartOfEventTime() {
        LocalTime time = startOfEvent.toLocalTime();
        this.startOfEventTime = Duration.between(LocalTime.MIN, startOfEvent).toMinutes();
    }

    public void setEndOfEvent(LocalDateTime endOfEvent ) {
        this.endOfEvent = endOfEvent;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration() {
        long minutes = Duration.between(startOfEvent, endOfEvent).toMinutes();
        this.duration = minutes;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall( Hall hall ) {
        this.hall = hall;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public EventCategory getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(EventCategory eventCategory) {
        this.eventCategory = eventCategory;
    }

    public static EventBuilder builder(){return new EventBuilder();}

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", startOfEvent=" + startOfEvent +
            ", endOfEvent=" + endOfEvent +
            ", seatSelection=" + seatSelection +
            ", hall=" + hall +
            ", artists=" + artists +
            ", eventCategory=" + eventCategory +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (getPrice() != event.getPrice()) return false;
        if (!getId().equals(event.getId())) return false;
        if (!getTitle().equals(event.getTitle())) return false;
        if (!getDescription().equals(event.getDescription())) return false;
        if (!getStartOfEvent().equals(event.getStartOfEvent())) return false;
        if (!getEndOfEvent().equals(event.getEndOfEvent())) return false;
        if (!getSeatSelection().equals(event.getSeatSelection())) return false;
        if (!getHall().equals(event.getHall())) return false;
        if (!getEventCategory().equals(event.getEventCategory())) return false;
        return getArtists().equals(event.getArtists());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + (int) (getPrice() ^ (getPrice() >>> 32));
        result = 31 * result + getStartOfEvent().hashCode();
        result = 31 * result + getEndOfEvent().hashCode();
        result = 31 * result + getSeatSelection().hashCode();
        result = 31 * result + getHall().hashCode();
        result = 31 * result + getArtists().hashCode();
        result = 31 * result + getEventCategory().hashCode();
        return result;
    }

    public static final class EventBuilder{
        private Long id;
        private String title;
        private String description;
        private Long price;
        private Boolean seatSelection;
        private LocalDateTime startOfEvent;
        private LocalDateTime endOfEvent;
        private Hall hall;
        private List<Artist> artists;
        private EventCategory eventCategory;

        public EventBuilder id(Long id){
            this.id = id;
            return this;
        }

        public EventBuilder title(String title){
            this.title = title;
            return this;
        }

        public EventBuilder description(String description){
            this.description = description;
            return this;
        }

        public EventBuilder price(Long price){
            this.price = price;
            return this;
        }

        public EventBuilder startOfEvent(LocalDateTime startOfEvent){
            this.startOfEvent = startOfEvent;
            return this;
        }

        public EventBuilder endOfEvent(LocalDateTime endOfEvent){
            this.endOfEvent = endOfEvent;
            return this;
        }

        public EventBuilder seatSelection(Boolean seatSelection){
            this.seatSelection = seatSelection;
            return this;
        }

        public EventBuilder hall(Hall hall){
            this.hall = hall;
            return this;
        }

        public EventBuilder artists(List<Artist> artists) {
            this.artists = artists;
            return this;
        }

        public EventBuilder category(EventCategory eventCategory){
            this.eventCategory = eventCategory;
            return this;
        }

        public Event build(){
            Event event = new Event();
            event.setId(id);
            event.setTitle(title);
            event.setDescription(description);
            event.setPrice(price);
            event.setStartOfEvent(startOfEvent);
            event.setEndOfEvent(endOfEvent);
            event.setHall(hall);
            event.setArtists(artists);
            event.setSeatSelection(seatSelection);
            event.setEventCategory(eventCategory);
            event.setStartOfEventTime();
            event.setDuration();
            return event;
        }
    }
}

