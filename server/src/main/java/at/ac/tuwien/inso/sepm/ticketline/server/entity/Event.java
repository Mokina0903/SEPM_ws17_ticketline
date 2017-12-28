package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_event_id")
    @SequenceGenerator(name = "seq_event_id", sequenceName = "seq_event_id")
    private Long id;

/*    @Column(nullable = false)
    @Size(max = 15)
    private String artistFirstName;

    @Column(nullable = false)
    @Size(max = 20)
    private String artistLastName;*/

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

    /*public String getArtistFirstName() {
        return artistFirstName;
    }

    public void setArtistFirstName( String artistFirstName ) {
        this.artistFirstName = artistFirstName;
    }

    public String getArtistLastName() {
        return artistLastName;
    }

    public void setArtistLastName( String artistLastName ) {
        this.artistLastName = artistLastName;
    }
*/
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

    public void setEndOfEvent( LocalDateTime endOfEvent ) {
        this.endOfEvent = endOfEvent;
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

    public static EventBuilder builder(){return new EventBuilder();}

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (getPrice() != event.getPrice()) return false;
        if (!getId().equals(event.getId())) return false;
/*        if (!getArtistFirstName().equals(event.getArtistFirstName())) return false;
        if (!getArtistLastName().equals(event.getArtistLastName())) return false;*/
        if (!getTitle().equals(event.getTitle())) return false;
        if (!getDescription().equals(event.getDescription())) return false;
        if (!getStartOfEvent().equals(event.getStartOfEvent())) return false;
        if (!getEndOfEvent().equals(event.getEndOfEvent())) return false;
        return getHall().equals(event.getHall());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        /*result = 31 * result + getArtistFirstName().hashCode();
        result = 31 * result + getArtistLastName().hashCode();*/
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + (int) (getPrice() ^ (getPrice() >>> 32));
        result = 31 * result + getStartOfEvent().hashCode();
        result = 31 * result + getEndOfEvent().hashCode();
        result = 31 * result + getHall().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
            "id=" + id +
          /*  ", artistFirstName='" + artistFirstName + '\'' +
            ", artistLastName='" + artistLastName + '\'' +*/
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", startOfEvent=" + startOfEvent +
            ", endOfEvent=" + endOfEvent +
            ", hall=" + hall +
            '}';
    }

    public static final class EventBuilder{
        private Long id;
       /* private String artistFirstName;
        private String artistLastName;*/
        private String title;
        private String description;
        private Long price;
        private LocalDateTime startOfEvent;
        private LocalDateTime endOfEvent;
        private Hall hall;
        private List<Artist> artists;

        public EventBuilder id(Long id){
            this.id = id;
            return this;
        }

       /* public EventBuilder artistFirstname(String artistFirstName){
            this.artistFirstName = artistFirstName;
            return this;
        }

        public EventBuilder artistLastName(String artistLastName){
            this.artistLastName = artistLastName;
            return this;
        }*/

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

        public EventBuilder hall(Hall hall){
            this.hall = hall;
            return this;
        }

        public EventBuilder artists(List<Artist> artists) {
            this.artists = artists;
            return this;
        }

        public Event build(){
            Event event = new Event();
            event.setId(id);
            event.setTitle(title);
          /*  event.setArtistFirstName(artistFirstName);
            event.setArtistLastName(artistLastName);*/
            event.setDescription(description);
            event.setPrice(price);
            event.setStartOfEvent(startOfEvent);
            event.setEndOfEvent(endOfEvent);
            event.setHall(hall);
            event.setArtists(artists);

            return event;
        }
    }
}

