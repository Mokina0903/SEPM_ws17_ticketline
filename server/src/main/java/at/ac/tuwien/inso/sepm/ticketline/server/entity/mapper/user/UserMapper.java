package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserSummaryMapper.class)
public interface UserMapper {

    User detailedUserDTOToUser(DetailedUserDTO detaileUserDTO);

    DetailedUserDTO userToDetailedUserDTO(User one);

    List<SimpleUserDTO> userToSimpleUserDTO(List<User> all);

    SimpleUserDTO userToSimpleUserDTO(User one);
}