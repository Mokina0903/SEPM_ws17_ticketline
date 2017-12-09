package at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_location_id")
    @SequenceGenerator(name = "seq_location_id", sequenceName = "seq_location_id")
    private Long id;

    @Column(nullable = false)
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
        if (!getId().equals(location.getId())) return false;
        if (!getDescription().equals(location.getDescription())) return false;
        if (!getCountry().equals(location.getCountry())) return false;
        if (!getCity().equals(location.getCity())) return false;
        return getStreet().equals(location.getStreet());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getCountry().hashCode();
        result = 31 * result + getCity().hashCode();
        result = 31 * result + getZip();
        result = 31 * result + getStreet().hashCode();
        result = 31 * result + getHouseNr();
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

        public Location build(){
            Location location = new Location();
            location.setCity(city);
            location.setId(id);
            location.setDescription(description);
            location.setCountry(country);
            location.setStreet(street);
            location.setZip(zip);
            location.setHouseNr(houseNr);

            return location;
        }
    }
}
