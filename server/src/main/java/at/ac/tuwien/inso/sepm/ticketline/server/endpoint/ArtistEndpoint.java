package at.ac.tuwien.inso.sepm.ticketline.server.endpoint;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.service.ArtistService;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.*;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/artist")
@Api(value = "artist")
public class ArtistEndpoint {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    public ArtistEndpoint(ArtistService artistService, ArtistMapper artistMapper ) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }

    @RequestMapping(value = "/search/{pageIndex}/{artistsPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple artist entries filtered by parameters")
    public Page<SimpleArtistDTO> find(@PathVariable("pageIndex")int pageIndex, @PathVariable("artistsPerPage")int artistsPerPage,
                                      @QuerydslPredicate(root = Artist.class)Predicate predicate,
                                      @RequestParam HashMap<String,String> parameters) {
        Pageable request = new PageRequest(pageIndex, artistsPerPage, Sort.Direction.ASC, "artistLastName");
        Page<Artist> artistPage = artistService.find(parameters, request);
        List<SimpleArtistDTO> dtos = artistMapper.artistToSimpleArtistDTO(artistPage.getContent());
        return new PageImpl<>(dtos, request, artistPage.getTotalElements());
    }

    @RequestMapping(value = "/advSearch/{pageIndex}/{artistsPerPage}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple artists entries filtered by parameters")
    public Page<SimpleArtistDTO> findAdvanced(@PathVariable("pageIndex")int pageIndex, @PathVariable("artistsPerPage")int artistsPerPage,
                                             @QuerydslPredicate(root = Artist.class)Predicate predicate,
                                             @RequestParam HashMap<String,String> parameters) {
        Pageable request = new PageRequest(pageIndex, artistsPerPage, Sort.Direction.ASC, "artistLastName");
        Page<Artist> artistPage = artistService.findByAdvancedSearch(parameters, request);
        List<SimpleArtistDTO> dtos = artistMapper.artistToSimpleArtistDTO(artistPage.getContent());
        return new PageImpl<>(dtos, request, artistPage.getTotalElements());
    }

}
