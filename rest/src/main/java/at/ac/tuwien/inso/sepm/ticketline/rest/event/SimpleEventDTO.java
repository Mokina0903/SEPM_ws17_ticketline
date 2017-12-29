package at.ac.tuwien.inso.sepm.ticketline.rest.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

@ApiModel(value = "SimpleEventDTO", description = "A simple DTO for event entries via rest")
public class SimpleEventDTO {
    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

/*    @ApiModelProperty(required = true, name = "The first name of the artist")
    private String artistFirstName;

    @ApiModelProperty(required = true, name = "The last name of the artist")
    private String artistLastName;*/


    @ApiModelProperty(required = true, name = "The title of the event")
    private String title;

    @ApiModelProperty(required = true, name = "The description of the event")
    private String descriptionSummary;

    @ApiModelProperty(required = true, name = "The price of one ticket for the event")
    private Long price;

    @ApiModelProperty(required = true, name = "The starting Time and Date of the event")
    private LocalDateTime startOfEvent;

    @ApiModelProperty(required = true, name = "The end Time and Date of the event")
    private LocalDateTime endOfEvent;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

/*    public String getArtistFirstName() {
        return artistFirstName;
    }

    public void setArtistFirstName( String artistFirstName ) {
        this.artistFirstName = artistFirstName;
    }

    public String getArtistLastName() {
        return artistLastName;
    }

    public void setArtistLastName( String artistLastName ) {
        this.artistLastName = artistLastName;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescriptionSummary() {
        return descriptionSummary;
    }

    public void setDescriptionSummary( String descriptionSummary ) {
        this.descriptionSummary = descriptionSummary;
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

    //todo artists

    @Override
    public boolean equals( Object o ) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleEventDTO that = (SimpleEventDTO) o;

        if (!getId().equals(that.getId())) return false;
/*        if (!getArtistFirstName().equals(that.getArtistFirstName())) return false;
        if (!getArtistLastName().equals(that.getArtistLastName())) return false;*/
        if (!getTitle().equals(that.getTitle())) return false;
        if (!getDescriptionSummary().equals(that.getDescriptionSummary())) return false;
        if (!getPrice().equals(that.getPrice())) return false;
        if (!getStartOfEvent().equals(that.getStartOfEvent())) return false;
        return getEndOfEvent().equals(that.getEndOfEvent());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
/*        result = 31 * result + getArtistFirstName().hashCode();
        result = 31 * result + getArtistLastName().hashCode();*/
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getDescriptionSummary().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + getStartOfEvent().hashCode();
        result = 31 * result + getEndOfEvent().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SimpleEventDTO{" +
            "id=" + id +
/*            ", artistFirstName='" + artistFirstName + '\'' +
            ", artistLastName='" + artistLastName + '\'' +*/
            ", title='" + title + '\'' +
            ", descriptionSummary='" + descriptionSummary + '\'' +
            ", price=" + price +
            ", startOfEvent=" + startOfEvent +
            ", endOfEvent=" + endOfEvent +
            '}';
    }

    public static final class SimpleEventDTOBuilder{
        private Long id;
        private String artistFirstName;
        private String artistLastName;
        private String title;
        private String descriptionSummary;
        private Long price;
        private LocalDateTime startOfEvent;
        private LocalDateTime endOfEvent;

        public SimpleEventDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public SimpleEventDTOBuilder artistFirstname(String artistFirstName){
            this.artistFirstName = artistFirstName;
            return this;
        }

        public SimpleEventDTOBuilder artistLastName(String artistLastName){
            this.artistLastName = artistLastName;
            return this;
        }

        public SimpleEventDTOBuilder title(String title){
            this.title = title;
            return this;
        }

        public SimpleEventDTOBuilder descriptionSummary(String description){
            this.descriptionSummary = description;
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

        public SimpleEventDTO build(){
            SimpleEventDTO event = new SimpleEventDTO();
            event.setId(id);
            event.setTitle(title);
/*            event.setArtistFirstName(artistFirstName);
            event.setArtistLastName(artistLastName);*/
            event.setDescriptionSummary(descriptionSummary);
            event.setPrice(price);
            event.setStartOfEvent(startOfEvent);
            event.setEndOfEvent(endOfEvent);

            return event;
        }
    }
}
