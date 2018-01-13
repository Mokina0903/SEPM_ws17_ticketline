package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.artist.SimpleArtistDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.seat.SeatDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.Artist;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Hall;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.eventLocation.Seat;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.artist.ArtistMapper;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.eventLocation.seat.SeatMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ArtistMapperTest {


    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class SeatMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private ArtistMapper artistMapper;


    private static final String FIRST = "Ash";
    private static final String LAST = "Ketchum";
    private static final Long ID = 1L;




    @Test
    public void shouldMapArtistToArtistDTO() {
        Artist artist = Artist.builder()
            .artistFirstname(FIRST)
            .artistLastName(LAST)
            .id(ID)
            .build();

        SimpleArtistDTO artistDTO = artistMapper.artistToSimpleArtistDTO(artist);


        assertThat(artistDTO).isNotNull();
        assertThat(artistDTO.getId()).isEqualTo(ID);
        assertThat(artistDTO.getArtistFirstName()).isEqualTo(FIRST);
        assertThat(artistDTO.getArtistLastName()).isEqualTo(LAST);

    }

    @Test
    public void shouldMapSeatSTOToSeat() {
        SimpleArtistDTO artistDTO = SimpleArtistDTO.builder()
            .artistFirstname(FIRST)
            .artistLastName(LAST)
            .id(ID)
            .build();

        Artist artist = artistMapper.simpleArtistDTOToArtist(artistDTO);


        assertThat(artist).isNotNull();
        assertThat(artist.getId()).isEqualTo(ID);
        assertThat(artist.getArtistFirstName()).isEqualTo(FIRST);
        assertThat(artist.getArtistLastName()).isEqualTo(LAST);
    }


}

