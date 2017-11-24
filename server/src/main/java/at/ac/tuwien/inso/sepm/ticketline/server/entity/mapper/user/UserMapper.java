package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.user.DetailedUserDTO;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserSummaryMapper.class)
public interface UserMapper {

    User detailedUserDTOToNews(DetailedUserDTO detaileUserDTO);

    DetailedUserDTO userToDetailedUserDTO(User one);

    List<SimpleUserDTO> userToSimpleUserDTO(List<User> all);

/*    @Mapping(source = "text", target = "summary", qualifiedBy = UserSummaryMapper.UserSummary.class)
    SimpleUserDTO newsToSimpleUserDTO(User one);*/
}