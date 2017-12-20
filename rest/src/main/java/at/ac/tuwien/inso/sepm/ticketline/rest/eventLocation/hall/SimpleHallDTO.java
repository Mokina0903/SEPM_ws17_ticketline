package at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall;

import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SimpleHallDTO", description = "A simple DTO for hall entries via rest")
public class SimpleHallDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, name = "The description of the hall")
    private String description;


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

    @Override
    public String toString() {
        return "SimpleHallDTO{" +
            "id=" + id +
            ", description='" + description + '\'' +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleHallDTO that = (SimpleHallDTO) o;

        if (!getId().equals(that.getId())) return false;
        return getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }

    public static final class SimpleHallDTOBuilder{
        private Long id;
        private String description;

        public SimpleHallDTOBuilder id( Long id ) {
            this.id = id;
            return this;
        }

        public SimpleHallDTOBuilder description( String description) {
            this.description = description;
            return this;
        }

        public SimpleHallDTO build(){
            SimpleHallDTO hall = new SimpleHallDTO();
            hall.setId(id);
            hall.setDescription(description);

            return hall;
        }
    }
}
