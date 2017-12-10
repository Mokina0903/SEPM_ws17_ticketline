package at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation;

import javax.persistence.*;

@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_hall_id")
    @SequenceGenerator(name = "seq_hall_id", sequenceName = "seq_hall_id")
    private Long id;

    private int nr;

    private int row;

    private char sector;

    @ManyToOne
    private Hall hall;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public int getNr() {
        return nr;
    }

    public void setNr( int nr ) {
        this.nr = nr;
    }

    public int getRow() {
        return row;
    }

    public void setRow( int row ) {
        this.row = row;
    }

    public char getSector() {
        return sector;
    }

    public void setSector( char sector ) {
        this.sector = sector;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall( Hall hall ) {
        this.hall = hall;
    }

    public static SeatBuilder builder(){return new SeatBuilder();}

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        if (getNr() != seat.getNr()) return false;
        if (getRow() != seat.getRow()) return false;
        if (getSector() != seat.getSector()) return false;
        if (!getId().equals(seat.getId())) return false;
        return getHall().equals(seat.getHall());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getNr();
        result = 31 * result + getRow();
        result = 31 * result + (int) getSector();
        result = 31 * result + getHall().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Seat{" +
            "id=" + id +
            ", nr=" + nr +
            ", row=" + row +
            ", sector=" + sector +
            ", hall=" + hall +
            '}';
    }

    public static final class SeatBuilder{
        private Long id;
        private int nr;
        private int row;
        private char sector;
        private Hall hall;

        public SeatBuilder id(Long id){
            this.id=id;
            return this;
        }

        public SeatBuilder nr(int nr){
            this.nr = nr;
            return this;
        }

        public SeatBuilder row(int row){
            this.row = row;
            return this;
        }

        public SeatBuilder sector(char sector){
            this.sector = sector;
            return this;
        }

        public SeatBuilder hall(Hall hall){
            this.hall = hall;
            return this;
        }

        public Seat build(){
            Seat seat = new Seat();

            seat.setId(id);
            seat.setNr(nr);
            seat.setRow(row);
            seat.setSector(sector);
            seat.setHall(hall);

            return seat;
        }
    }
}

