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



    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailedHallDTO that = (DetailedHallDTO) o;

        if (!getId().equals(that.getId())) return false;
        if (!getDescription().equals(that.getDescription())) return false;
        if (!getLocation().equals(that.getLocation())) return false;
        return getSeats() != null ? getSeats().equals(that.getSeats()) : that.getSeats() == null;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + (getSeats() != null ? getSeats().hashCode() : 0);
        return result;
    }

    public static DetailedHallDTOBuilder builder() {
        return new DetailedHallDTOBuilder();
    }


    public static final class DetailedHallDTOBuilder{
        private Long id;
        private String description;
        private DetailedLocationDTO location;
        private List<SeatDTO> seats;

        public DetailedHallDTOBuilder id( Long id ) {
            this.id = id;
            return this;
        }

        public DetailedHallDTOBuilder description( String description) {
            this.description = description;
            return this;
        }

        public DetailedHallDTOBuilder location( DetailedLocationDTO location) {
            this.location = location;
            return this;
        }

        public DetailedHallDTOBuilder seats( List<SeatDTO> seats) {
            this.seats = seats;
            return this;
        }

        public DetailedHallDTO build(){
            DetailedHallDTO hall = new DetailedHallDTO();
            hall.setId(id);
            hall.setDescription(description);
            hall.setLocation(location);
            hall.setSeats(seats);

            return hall;
        }
    }
}
