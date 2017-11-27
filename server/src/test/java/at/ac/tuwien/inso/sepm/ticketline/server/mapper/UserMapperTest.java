package at.ac.tuwien.inso.sepm.ticketline.server.mapper;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {

    @Configuration
    @ComponentScan(basePackages = "at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper")
    public static class UserMapperTestContextConfiguration {
    }

    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    // Suppress warning cause inspection does not know that the cdi annotations are added in the code generation step
    private UserMapper userMapper;

    private static final long USER_ID = 1L;
    private static final String USER_NAME = "Username";
    private static final String USER_PASSWORD = "User password.";
    private static final Integer USER_ROLE = 1;
    private static final boolean BLOCKED = false;


    //no summary of user needed

  /*  @Test
    public void shouldMapUserToSimpleUserDTOShorteningTextToSummary() {
        User user = User.builder()
            .id(USER_ID)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .blocked(BLOCKED)
            .build();
        SimpleUserDTO simpleUserDTO = userMapper.userToSimpleUserDTO(user);
        assertThat(simpleUserDTO).isNotNull();
        assertThat(simpleUserDTO.getId()).isEqualTo(1L);
        assertThat(simpleUserDTO.getUserName()).isEqualTo(USER_NAME);
        assertThat(simpleUserDTO.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(simpleUserDTO.getRole()).isEqualTo(USER_ROLE);
        assertThat(simpleUserDTO.isBlocked() == BLOCKED);
    }*/


/*    @Test
    public void shouldMapUserToSimpleUserDTONotShorteningTextToSummary() {
        User user = User.builder()
            .id(USER_ID)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .blocked(BLOCKED)
            .build();
        SimpleUserDTO simpleUserDTO = userMapper.userToSimpleUserDTO(user);
        assertThat(simpleUserDTO).isNotNull();
        assertThat(simpleUserDTO.getId()).isEqualTo(1L);
        assertThat(simpleUserDTO.getUserName()).isEqualTo(USER_NAME);
        assertThat(simpleUserDTO.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(simpleUserDTO.getRole()).isEqualTo(USER_ROLE);
        assertThat(simpleUserDTO.isBlocked() == BLOCKED);
    }*/

    @Test
    public void shouldMapUserToDetailedUserDTO() {
        User user = User.builder()
            .id(USER_ID)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .blocked(BLOCKED)
            .build();
        DetailedUserDTO detailedUserDTO = userMapper.userToDetailedUserDTO(user);
        assertThat(detailedUserDTO).isNotNull();
        assertThat(detailedUserDTO.getId()).isEqualTo(1L);
        assertThat(detailedUserDTO.getUserName()).isEqualTo(USER_NAME);
        assertThat(detailedUserDTO.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(detailedUserDTO.getRole()).isEqualTo(USER_ROLE);
        assertThat(detailedUserDTO.isBlocked() == BLOCKED);
    }

    @Test
    public void shouldMapDetailedUserDTOToUser() {
        DetailedUserDTO detailedUserDTO = DetailedUserDTO.builder()
            .id(USER_ID)
            .userName(USER_NAME)
            .password(USER_PASSWORD)
            .role(USER_ROLE)
            .blocked(BLOCKED)
            .build();
        User user = userMapper.detailedUserDTOToUser(detailedUserDTO);
        assertThat(user).isNotNull();
        assertThat(detailedUserDTO.getId()).isEqualTo(1L);
        assertThat(detailedUserDTO.getUserName()).isEqualTo(USER_NAME);
        assertThat(detailedUserDTO.getPassword()).isEqualTo(USER_PASSWORD);
        assertThat(detailedUserDTO.getRole()).isEqualTo(USER_ROLE);
        assertThat(detailedUserDTO.isBlocked() == BLOCKED);
    }
}
