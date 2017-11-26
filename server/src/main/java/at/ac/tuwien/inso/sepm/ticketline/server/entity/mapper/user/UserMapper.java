package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import at.ac.tuwien.inso.sepm.ticketline.rest.user2.DetailedUserDTO2;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserSummaryMapper.class)
public interface UserMapper {

    User detailedUserDTOToNews(DetailedUserDTO2 detaileUserDTO);

    DetailedUserDTO2 userToDetailedUserDTO(User one);

    //List<SimpleUserDTO2> userToSimpleUserDTO(List<User> all);

/*    @Mapping(source = "text", target = "summary", qualifiedBy = UserSummaryMapper.UserSummary.class)
    SimpleUserDTO2 newsToSimpleUserDTO(User one);*/
}