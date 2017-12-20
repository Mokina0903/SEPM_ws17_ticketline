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



    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeatDTO seatDTO = (SeatDTO) o;

        if (getNr() != seatDTO.getNr()) return false;
        if (getRow() != seatDTO.getRow()) return false;
        if (getSector() != seatDTO.getSector()) return false;
        return getId().equals(seatDTO.getId());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getNr();
        result = 31 * result + getRow();
        result = 31 * result + (int) getSector();
        return result;
    }

    public static final class SeatDTOBuilder{
        private Long id;
        private int nr;
        private int row;
        private char sector;

        public SeatDTOBuilder id(Long id){
            this.id=id;
            return this;
        }

        public SeatDTOBuilder nr(int nr){
            this.nr = nr;
            return this;
        }

        public SeatDTOBuilder row(int row){
            this.row = row;
            return this;
        }

        public SeatDTOBuilder sector(char sector){
            this.sector = sector;
            return this;
        }

        public SeatDTO build(){
            SeatDTO seat = new SeatDTO();

            seat.setId(id);
            seat.setNr(nr);
            seat.setRow(row);
            seat.setSector(sector);

            return seat;
        }
    }
}
