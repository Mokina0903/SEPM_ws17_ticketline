package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import at.ac.tuwien.inso.sepm.ticketline.rest.PageableDAO;
import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "SimpleEventDTO", description = "A simple DTO for event entries via rest")
public class SimpleEventDTO implements PageableDAO{
    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, name = "The title of the event")
    private String title;

    @ApiModelProperty(required = true, name = "List of artists")
    private List<SimpleArtistDTO> artists;

    @ApiModelProperty(required = true, name = "The description of the event")
    private String description;

    @ApiModelProperty(required = true, name = "The price of one ticket for the event")
    private Long price;

    @ApiModelProperty(required = true, name = "The starting Time and Date of the event")
    private LocalDateTime startOfEvent;

    @ApiModelProperty(required = true, name = "The end Time and Date of the event")
    private LocalDateTime endOfEvent;

    @ApiModelProperty(required =true, name = "Info about seat selection or sectors")
    private Boolean seatSelection;

    @ApiModelProperty(required = true, name = "The Event category")
    private String eventCategory;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public List<SimpleArtistDTO> getArtists() {
        return artists;
    }

    public void setArtists(List<SimpleArtistDTO> artists) {
        this.artists = artists;
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

    public String getPriceInEuroString(){

        double priced = (double)price/100d;
        return String.format("%.2f",priced);}

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

    public Boolean getSeatSelection() {
        return seatSelection;
    }

    public void setSeatSelection( Boolean seatSelection ) {
        this.seatSelection = seatSelection;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    @Override
    public String toString() {
        return "SimpleEventDTO{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", artists=" + artists +
            ", descriptionSummary='" + description + '\'' +
            ", price=" + price +
            ", startOfEvent=" + startOfEvent +
            ", endOfEvent=" + endOfEvent +
            ", seatSelection=" + seatSelection +
            ", eventCategory=" + eventCategory +
            '}';
    }

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleEventDTO that = (SimpleEventDTO) o;

        if (!getId().equals(that.getId())) return false;
        if (!getTitle().equals(that.getTitle())) return false;
        if (!getArtists().equals(that.getArtists())) return false;
        if (!getPrice().equals(that.getPrice())) return false;
        if (!getStartOfEvent().equals(that.getStartOfEvent())) return false;
        if (!getEndOfEvent().equals(that.getEndOfEvent())) return false;
        if (!getEventCategory().equals(that.getEventCategory())) return false;
        return getSeatSelection().equals(that.getSeatSelection());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getArtists().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + getStartOfEvent().hashCode();
        result = 31 * result + getEndOfEvent().hashCode();
        result = 31 * result + getSeatSelection().hashCode();
        result = 31 * result + getEventCategory().hashCode();
        return result;
    }

    public static SimpleEventDTOBuilder builder() {
        return new SimpleEventDTOBuilder();
    }

    public static final class SimpleEventDTOBuilder{
        private Long id;
        private String title;
        private List<SimpleArtistDTO> artists;
        private String description;
        private Long price;
        private LocalDateTime startOfEvent;
        private LocalDateTime endOfEvent;
        private Boolean seatSelection;
        private String eventCategory;

        public SimpleEventDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public SimpleEventDTOBuilder title(String title){
            this.title = title;
            return this;
        }

        public SimpleEventDTOBuilder artists(List<SimpleArtistDTO> artists) {
            this.artists = artists;
            return this;
        }

        public SimpleEventDTOBuilder description(String description){
            this.description = description;
            return this;
        }

        public SimpleEventDTOBuilder price(Long price){
            this.price = price;
            return this;
        }

        public SimpleEventDTOBuilder startOfEvent(LocalDateTime startOfEvent){
            this.startOfEvent = startOfEvent;
            return this;
        }

        public SimpleEventDTOBuilder endOfEvent(LocalDateTime endOfEvent){
            this.endOfEvent = endOfEvent;
            return this;
        }

        public SimpleEventDTOBuilder seatSelection(Boolean seatSelection){
            this.seatSelection = seatSelection;
            return this;
        }

        public SimpleEventDTOBuilder category(String eventCategory){
            this.eventCategory = eventCategory;
            return this;
        }

        public SimpleEventDTO build(){
            SimpleEventDTO event = new SimpleEventDTO();
            event.setId(id);
            event.setTitle(title);
            event.setArtists(artists);
            event.setDescription(description);
            event.setPrice(price);
            event.setStartOfEvent(startOfEvent);
            event.setEndOfEvent(endOfEvent);
            event.setEventCategory(eventCategory);
            return event;
        }
    }
}
