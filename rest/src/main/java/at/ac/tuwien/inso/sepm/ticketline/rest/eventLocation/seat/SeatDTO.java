package at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "SeatDTO", description = "A DTO for seat entries via rest")
public class SeatDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(readOnly = true, name = "The number of the seat")
    private int nr;

    @ApiModelProperty(name = "The row of the seat")
    private int row;

    @ApiModelProperty(name = "The sector of the seat")
    private char sector;

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
}
