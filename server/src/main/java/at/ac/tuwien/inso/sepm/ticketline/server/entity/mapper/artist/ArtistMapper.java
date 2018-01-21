package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    Artist simpleArtistDTOToArtist(SimpleArtistDTO simpleArtistDTO);

    SimpleArtistDTO artistToSimpleArtistDTO(Artist artist);

    List<SimpleArtistDTO> artistToSimpleArtistDTO( List<Artist> all);
}
