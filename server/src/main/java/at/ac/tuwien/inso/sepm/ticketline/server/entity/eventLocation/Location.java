package at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.Predicatable;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "location")
public class Location implements Predicatable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_location_id")
    @SequenceGenerator(name = "seq_location_id", sequenceName = "seq_location_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Size(max = 200)
    private String description;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private int zip;

    @Column(nullable = false)
    private String street;

    private int houseNr;

    @OneToMany(mappedBy = "location")
    private List<Hall> eventHalls;

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

    public String getCountry() {
        return country;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public int getZip() {
        return zip;
    }

    public void setZip( int zip ) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet( String street ) {
        this.street = street;
    }

    public int getHouseNr() {
        return houseNr;
    }

    public void setHouseNr( int houseNr ) {
        this.houseNr = houseNr;
    }

    public List<Hall> getEventHalls() {
        return eventHalls;
    }

    public void setEventHalls( List<Hall> eventHalls ) {
        this.eventHalls = eventHalls;
    }

    public static LocationBuilder builder(){return new LocationBuilder();}

    public Location() {
    }

    public Location(String description, String country, String city, int zip, String street, int houseNr) {
        this.description = description;
        this.country = country;
        this.city = city;
        this.zip = zip;
        this.street = street;
        this.houseNr = houseNr;
    }

    @Override
    public String toString() {
        return "Location{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", zip=" + zip +
            ", street='" + street + '\'' +
            ", houseNr=" + houseNr +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (getZip() != location.getZip()) return false;
        if (getHouseNr() != location.getHouseNr()) return false;
        if (getId() != null ? !getId().equals(location.getId()) : location.getId() != null) return false;
        if (getDescription() != null ? !getDescription().equals(location.getDescription()) : location.getDescription() != null)
            return false;
        if (getCountry() != null ? !getCountry().equals(location.getCountry()) : location.getCountry() != null)
            return false;
        if (getCity() != null ? !getCity().equals(location.getCity()) : location.getCity() != null) return false;
        if (getStreet() != null ? !getStreet().equals(location.getStreet()) : location.getStreet() != null)
            return false;
        return eventHalls != null ? eventHalls.equals(location.eventHalls) : location.eventHalls == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getCountry() != null ? getCountry().hashCode() : 0);
        result = 31 * result + (getCity() != null ? getCity().hashCode() : 0);
        result = 31 * result + getZip();
        result = 31 * result + (getStreet() != null ? getStreet().hashCode() : 0);
        result = 31 * result + getHouseNr();
        result = 31 * result + (eventHalls != null ? eventHalls.hashCode() : 0);
        return result;
    }

    public static final class LocationBuilder{
        private Long id;
        private String description;
        private String country;
        private String city;
        private int zip;
        private String street;
        private int houseNr;
        private List<Hall> eventHalls;

        public LocationBuilder id( Long id ) {
            this.id = id;
            return this;
        }

        public LocationBuilder description( String description) {
            this.description = description;
            return this;
        }

        public LocationBuilder country( String country) {
            this.country = country;
            return this;
        }

        public LocationBuilder city( String city) {
            this.city = city;
            return this;
        }

        public LocationBuilder zip( int zip) {
            this.zip = zip;
            return this;
        }

        public LocationBuilder street( String street) {
            this.street = street;
            return this;
        }

        public LocationBuilder houseNr( int houseNr) {
            this.houseNr = houseNr;
            return this;
        }

        public LocationBuilder eventHalls(List<Hall> eventHalls){
            this.eventHalls = eventHalls;
            return this;
        }
        public Location build(){
            Location location = new Location();
            location.setCity(city);
            location.setId(id);
            location.setDescription(description);
            location.setCountry(country);
            location.setStreet(street);
            location.setZip(zip);
            location.setHouseNr(houseNr);
            location.setEventHalls(eventHalls);

            return location;
        }
    }
}
