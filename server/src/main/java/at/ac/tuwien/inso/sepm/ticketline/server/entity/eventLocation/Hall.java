package at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "hall")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_id")
    @SequenceGenerator(name = "seq_hall_id", sequenceName = "seq_hall_id")
    private Long id;

    @Column(nullable = false)
    @Size(max = 200)
    private String description;

    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "hall")
    private List<Seat> seats;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation( Location location ) {
        this.location = location;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats( List<Seat> seats ) {
        this.seats = seats;
    }

    public static HallBuilder builder(){return new HallBuilder();}


    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hall hall = (Hall) o;

        if (!getId().equals(hall.getId())) return false;
        if (getDescription() != null ? !getDescription().equals(hall.getDescription()) : hall.getDescription() != null)
            return false;
        if (!getLocation().equals(hall.getLocation())) return false;
        return seats != null ? seats.equals(hall.seats) : hall.seats == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (seats != null ? seats.hashCode() : 0);
        return result;
    }

    public static final class HallBuilder{
        private Long id;
        private String description;
        private Location location;
        private List<Seat> seats;

        public HallBuilder id( Long id ) {
            this.id = id;
            return this;
        }

        public HallBuilder description( String description) {
            this.description = description;
            return this;
        }

        public HallBuilder location( Location location) {
            this.location = location;
            return this;
        }

        public HallBuilder seats( List<Seat> seats) {
            this.seats = seats;
            return this;
        }

        public Hall build(){
            Hall hall = new Hall();
            hall.setId(id);
            hall.setDescription(description);
            hall.setLocation(location);
            hall.setSeats(seats);

            return hall;
        }
    }
}
