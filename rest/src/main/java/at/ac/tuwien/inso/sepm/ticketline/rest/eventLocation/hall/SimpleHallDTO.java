package at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall;

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

}
