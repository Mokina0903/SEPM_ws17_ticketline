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
}
