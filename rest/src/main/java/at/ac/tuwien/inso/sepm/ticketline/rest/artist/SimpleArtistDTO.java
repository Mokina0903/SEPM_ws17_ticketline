package at.ac.tuwien.inso.sepm.ticketline.rest.artist;

import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

public class SimpleArtistDTO {

    @ApiModelProperty(readOnly = true, name = "The automatically generated database id")
    private Long id;

    @ApiModelProperty(required = true, name = "Firstname of Artist")
    private String artistFirstName;

    @ApiModelProperty(required = true, name = "Lastname of Artist")
    private String artistLastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtistFirstName() {
        return artistFirstName;
    }

    public void setArtistFirstName(String artistFirstName) {
        this.artistFirstName = artistFirstName;
    }

    public String getArtistLastName() {
        return artistLastName;
    }

    public void setArtistLastName(String artistLastName) {
        this.artistLastName = artistLastName;
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getArtistFirstName().hashCode();
        result = 31 * result + getArtistLastName().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleArtistDTO artist = (SimpleArtistDTO) o;
        return Objects.equals(id, artist.id) &&
            Objects.equals(artistFirstName, artist.artistFirstName) &&
            Objects.equals(artistLastName, artist.artistLastName);
    }

    @Override
    public String toString() {
        return "SimpleArtistDTO{" +
            "id=" + id +
            ", artistFirstName='" + artistFirstName + '\'' +
            ", artistLastName='" + artistLastName + '\'' +
            ", hash='" + hashCode() + '\'' +
            '}';
    }

    public static SimpleArtistDTOBuilder builder() {
        return new SimpleArtistDTOBuilder();
    }


    public static final class SimpleArtistDTOBuilder {
        private Long id;
        private String artistFirstName;
        private String artistLastName;

        public SimpleArtistDTOBuilder id(Long id){
            this.id = id;
            return this;
        }

        public SimpleArtistDTOBuilder artistFirstname(String artistFirstName){
            this.artistFirstName = artistFirstName;
            return this;
        }

        public SimpleArtistDTOBuilder artistLastName(String artistLastName){
            this.artistLastName = artistLastName;
            return this;
        }

        public SimpleArtistDTO build(){
            SimpleArtistDTO artist = new SimpleArtistDTO();
            artist.setId(id);
            artist.setArtistFirstName(artistFirstName);
            artist.setArtistLastName(artistLastName);
            return artist;
        }

    }
}
