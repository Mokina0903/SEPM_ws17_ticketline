package at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "DetailedLocationDTO", description = "A detailed DTO for location entries via rest")
public class DetailedLocationDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, name = "The description of the location")
    private String description;

    @ApiModelProperty(required = true, name = "The country the location is placed in")
    private String country;

    @ApiModelProperty(required = true, name = "The city the location is placed in")
    private String city;

    @ApiModelProperty(required = true, name = "The zip-code of the location")
    private int zip;

    @ApiModelProperty(required = true, name = "The street the location is placed in")
    private String street;

    @ApiModelProperty(required = true, name = "The house number of the location")
    private int houseNr;

    @ApiModelProperty( name = "The list of halls the location contains")
    private List<SimpleHallDTO> eventHalls;

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

    public List<SimpleHallDTO> getEventHalls() {
        return eventHalls;
    }

    public void setEventHalls( List<SimpleHallDTO> eventHalls ) {
        this.eventHalls = eventHalls;
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailedLocationDTO that = (DetailedLocationDTO) o;

        if (getZip() != that.getZip()) return false;
        if (getHouseNr() != that.getHouseNr()) return false;
        if (!getId().equals(that.getId())) return false;
        if (!getDescription().equals(that.getDescription())) return false;
        if (!getCountry().equals(that.getCountry())) return false;
        if (!getCity().equals(that.getCity())) return false;
        if (!getStreet().equals(that.getStreet())) return false;
        return getEventHalls() != null ? getEventHalls().equals(that.getEventHalls()) : that.getEventHalls() == null;
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
        result = 31 * result + (getEventHalls() != null ? getEventHalls().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DetailedLocationDTO{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", country='" + country + '\'' +
            ", city='" + city + '\'' +
            ", zip=" + zip +
            ", street='" + street + '\'' +
            ", houseNr=" + houseNr +
            ", eventHalls=" + eventHalls +
            '}';
    }
    public static final class DetailedLocationDTOBuilder {
        private Long id;
        private String description;
        private String country;
        private String city;
        private int zip;
        private String street;
        private int houseNr;
        private List<SimpleHallDTO> eventHalls;

        public DetailedLocationDTOBuilder id( Long id ) {
            this.id = id;
            return this;
        }

        public DetailedLocationDTOBuilder description( String description ) {
            this.description = description;
            return this;
        }

        public DetailedLocationDTOBuilder country( String country ) {
            this.country = country;
            return this;
        }

        public DetailedLocationDTOBuilder city( String city ) {
            this.city = city;
            return this;
        }

        public DetailedLocationDTOBuilder zip( int zip ) {
            this.zip = zip;
            return this;
        }

        public DetailedLocationDTOBuilder street( String street ) {
            this.street = street;
            return this;
        }

        public DetailedLocationDTOBuilder houseNr( int houseNr ) {
            this.houseNr = houseNr;
            return this;
        }

        public DetailedLocationDTOBuilder eventHalls( List<SimpleHallDTO> eventHalls ) {
            this.eventHalls = eventHalls;
            return this;
        }

        public DetailedLocationDTO build() {
            DetailedLocationDTO location = new DetailedLocationDTO();
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
