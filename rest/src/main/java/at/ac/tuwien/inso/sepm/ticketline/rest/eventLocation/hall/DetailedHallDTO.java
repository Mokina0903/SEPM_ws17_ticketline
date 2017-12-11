package at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "DetailedHallDTO", description = "A detailed DTO for hall entries via rest")
public class DetailedHallDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;


    @ApiModelProperty(required = true, name = "The description of the hall")
    private String description;


    @ApiModelProperty(readOnly = true, name = "The location the hall is part of")
    private DetailedLocationDTO location;


    @ApiModelProperty( name = "The list of seats the hall contains")
    private List<SeatDTO> seats;

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

    public DetailedLocationDTO getLocation() {
        return location;
    }

    public void setLocation( DetailedLocationDTO location ) {
        this.location = location;
    }

    public List<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats( List<SeatDTO> seats ) {
        this.seats = seats;
    }
}
