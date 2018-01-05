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

    public void setPrice( Long price ) {
        this.price = price;
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
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DetailedEventDTO that = (DetailedEventDTO) o;

        if (!getId().equals(that.getId())) return false;
        if (!getArtists().equals(that.getArtists())) return false;
        if (!getTitle().equals(that.getTitle())) return false;
        if (!getDescription().equals(that.getDescription())) return false;
        if (!getPrice().equals(that.getPrice())) return false;
        if (!getStartOfEvent().equals(that.getStartOfEvent())) return false;
        if (!getEndOfEvent().equals(that.getEndOfEvent())) return false;
        return getHall().equals(that.getHall());
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
        result = 31 * result + getHall().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DetailedEventDTO{" +
            "id=" + id +
            ", artist=" + artists +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            ", price=" + price +
            ", startOfEvent=" + startOfEvent +
            ", endOfEvent=" + endOfEvent +
            ", hall=" + hall +
            '}';
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


        public DetailedEventDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public List<SimpleArtistDTO> getArtists() {
            return artists;
        }

        public void setArtists(List<SimpleArtistDTO> artists) {
            this.artists = artists;
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

            return event;
        }
    }
}
