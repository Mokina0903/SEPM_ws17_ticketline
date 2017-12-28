package at.ac.tuwien.inso.sepm.ticketline.server.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_artist_id")
    @SequenceGenerator(name = "seq_artist_id", sequenceName = "seq_artist_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 50)
    private String artistFirstName;

    @Column(nullable = false)
    @Size(max = 50)
    private String artistLastName;

/*    @ManyToMany()
    @JoinTable(
        name = "eventsOfArtist",
        joinColumns = @JoinColumn(name = "artist_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")) //toDo: make unique
    private List<Event> eventsOfArtist = new ArrayList<>();*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtistFirstName() {
        return artistFirstName;
    }

    public void setArtistFirstName(String artistFirstName) {
        this.artistFirstName = artistFirstName;
    }

    public String getArtistLastName() {
        return artistLastName;
    }

    public void setArtistLastName(String artistLastName) {
        this.artistLastName = artistLastName;
    }

/*    public List<Event> getEventsOfArtist() {
        return eventsOfArtist;
    }

    public void setEventsOfArtist(List<Event> eventsOfArtist) {
        this.eventsOfArtist = eventsOfArtist;
    }*/

    public static ArtistBuilder builder(){
        return new ArtistBuilder();
    }

    public static final class ArtistBuilder{

        private Long id;
        private String artistFirstName;
        private String artistLastName;

        public ArtistBuilder id(Long id){
            this.id = id;
            return this;
        }

        public ArtistBuilder artistFirstname(String artistFirstName){
            this.artistFirstName = artistFirstName;
            return this;
        }

        public ArtistBuilder artistLastName(String artistLastName){
            this.artistLastName = artistLastName;
            return this;
        }

        public Artist build() {
            Artist artist = new Artist();
            artist.setId(id);
            artist.setArtistFirstName(artistFirstName);
            artist.setArtistLastName(artistLastName);

            return artist;
        }
    }
}
