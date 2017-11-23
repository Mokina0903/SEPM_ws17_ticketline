package at.ac.tuwien.inso.sepm.ticketline.server.entity.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = UserSummaryMapper.class)
public interface UserMapper {


    // TODO: What todo here?
    //News detailedNewsDTOToNews(DetailedNewsDTO detailedNewsDTO);

    //DetailedNewsDTO newsToDetailedNewsDTO(News one);

    //List<SimpleNewsDTO> newsToSimpleNewsDTO(List<News> all);

    //@Mapping(source = "text", target = "summary", qualifiedBy = UserSummaryMapper.UserSummary.class)
    //SimpleNewsDTO newsToSimpleNewsDTO(News one);

}