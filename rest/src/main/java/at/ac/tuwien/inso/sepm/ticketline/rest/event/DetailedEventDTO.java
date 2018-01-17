package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "DetailedEventDTO", description = "A detailed DTO for event entries via rest")
public class DetailedEventDTO{

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, name = "The List of Artists")
    private List<SimpleArtistDTO> artists;

    @ApiModelProperty(required = true, name = "The title of the event")
    private String title;

    @ApiModelProperty(required = true, name = "The description of the event")
    private String description;

    @ApiModelProperty(required = true, name = "The price of one ticket for the event")
    private Long price;

    @ApiModelProperty(required = true, name = "The starting Time and Date of the event")
    private LocalDateTime startOfEvent;

    @ApiModelProperty(required = true, name = "The end Time and Date of the event")
    private LocalDateTime endOfEvent;

    @ApiModelProperty(required =true, name = "Information about seat selection or sectors")
    private Boolean seatSelection;

    @ApiModelProperty(required = true, name = "The hall where the event takes place")
    private DetailedHallDTO hall;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public List<SimpleArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<SimpleArtistDTO> artists) {
        this.artists = artists;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public double getPriceInEuro(){return(double)price/(double)100;}

    public void setPrice( Long price ) {
        this.price = price;
    }

    public Boolean getSeatSelection() {
        return seatSelection;
    }

    public void setSeatSelection( Boolean seatSelection ) {
        this.seatSelection = seatSelection;
    }

    public LocalDateTime getStartOfEvent() {
        return startOfEvent;
    }

    public void setStartOfEvent( LocalDateTime startOfEvent ) {
        this.startOfEvent = startOfEvent;
    }

    public LocalDateTime getEndOfEvent() {
        return endOfEvent;
    }

    public void setEndOfEvent( LocalDateTime endOfEvent ) {
        this.endOfEvent = endOfEvent;
    }

    public DetailedHallDTO getHall() {
        return hall;
    }

    public void setHall( DetailedHallDTO hall ) {
        this.hall = hall;
    }

    @Override
    public String toString() {
        return "DetailedEventDTO{" +
            "id=" + id +
            ", artists=" + artists +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", startOfEvent=" + startOfEvent +
            ", endOfEvent=" + endOfEvent +
            ", seatSelection=" + seatSelection +
            ", hall=" + hall +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailedEventDTO eventDTO = (DetailedEventDTO) o;

        if (!getId().equals(eventDTO.getId())) return false;
        if (!getArtists().equals(eventDTO.getArtists())) return false;
        if (!getTitle().equals(eventDTO.getTitle())) return false;
        if (!getDescription().equals(eventDTO.getDescription())) return false;
        if (!getPrice().equals(eventDTO.getPrice())) return false;
        if (!getStartOfEvent().equals(eventDTO.getStartOfEvent())) return false;
        if (!getEndOfEvent().equals(eventDTO.getEndOfEvent())) return false;
        if (!getSeatSelection().equals(eventDTO.getSeatSelection())) return false;
        return getHall().equals(eventDTO.getHall());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getArtists().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + getStartOfEvent().hashCode();
        result = 31 * result + getEndOfEvent().hashCode();
        result = 31 * result + getSeatSelection().hashCode();
        result = 31 * result + getHall().hashCode();
        return result;
    }

    public static DetailedEventDTOBuilder builder() {
        return new DetailedEventDTOBuilder();
    }


    public static final class DetailedEventDTOBuilder{
        private Long id;
        private List<SimpleArtistDTO> artists;
        private String title;
        private String description;
        private Long price;
        private LocalDateTime startOfEvent;
        private LocalDateTime endOfEvent;
        private DetailedHallDTO hall;
        private Boolean seatSelection;


        public DetailedEventDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public DetailedEventDTOBuilder artists(List<SimpleArtistDTO> artists) {
            this.artists = artists;
            return this;
        }

        public DetailedEventDTOBuilder title(String title){
            this.title = title;
            return this;
        }

        public DetailedEventDTOBuilder description(String description){
            this.description = description;
            return this;
        }

        public DetailedEventDTOBuilder price(Long price){
            this.price = price;
            return this;
        }

        public DetailedEventDTOBuilder startOfEvent(LocalDateTime startOfEvent){
            this.startOfEvent = startOfEvent;
            return this;
        }

        public DetailedEventDTOBuilder endOfEvent(LocalDateTime endOfEvent){
            this.endOfEvent = endOfEvent;
            return this;
        }

        public DetailedEventDTOBuilder hall(DetailedHallDTO hall){
            this.hall = hall;
            return this;
        }

        public DetailedEventDTOBuilder seatSelection(Boolean seatSelection){
            this.seatSelection = seatSelection;
            return this;
        }

        public DetailedEventDTO build(){
            DetailedEventDTO event = new DetailedEventDTO();
            event.setId(id);
            event.setTitle(title);
            event.setArtists(artists);
            event.setDescription(description);
            event.setPrice(price);
            event.setStartOfEvent(startOfEvent);
            event.setEndOfEvent(endOfEvent);
            event.setHall(hall);
            event.setSeatSelection(seatSelection);

            return event;
        }
    }
}
