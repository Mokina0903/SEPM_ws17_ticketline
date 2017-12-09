package at.ac.tuwien.inso.sepm.ticketline.server.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;
import at.ac.tuwien.inso.sepm.ticketline.server.exception.NotFoundException;
import at.ac.tuwien.inso.sepm.ticketline.server.repository.NewsRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.server.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleNewsService implements NewsService {

    private final NewsRepository newsRepository;
    private UserService userService;

    public SimpleNewsService(NewsRepository newsRepository, UserService userService) {
        this.newsRepository = newsRepository;
        this.userService = userService;
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAllByOrderByPublishedAtDesc();
    }

    @Override
    public News findOne(Long id) {
        return newsRepository.findOneById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<News> findNotSeenByUser( Long userId ) {
        return newsRepository.findNotSeenByUser(userId);
    }

    @Override
    public List<News> findOldNewsByUser( Long userId ) {
        return newsRepository.findOldNewsByUser(userId);
    }

    @Override
    public News publishNews(News news) {
        news.setPublishedAt(LocalDateTime.now());
        News newsTMP =newsRepository.save(news);
        List<User> users = userService.findAll();
        if(!users.isEmpty()) {
            for (User user : users) {
                List<News> notSeen = user.getNotSeen();
                if(notSeen==null){notSeen=new ArrayList<>();}
                notSeen.add(newsTMP);
                user.setNotSeen(notSeen);
                //userService.updateNotSeen(user.getId(),user.getNotSeen());
                userService.save(user);
            }
        }
        return newsTMP;
    }

}
