package at.ac.tuwien.inso.sepm.ticketline.server.service;

import at.ac.tuwien.inso.sepm.ticketline.server.entity.News;
import at.ac.tuwien.inso.sepm.ticketline.server.entity.User;

import java.util.List;

public interface NewsService {

    /**
     * Find all news entries ordered by published at date (descending).
     *
     * @return ordered list of al news entries
     */
    List<News> findAll();


    /**
     * Find a single news entry by id.
     *
     * @param id the is of the news entry
     * @return the news entry
     */
    News findOne(Long id);

    /**
     * find list of all news not seen by the user
     *
     * @param userId of the user
     * @return list of new news
     */
    List<News> findNotSeenByUser(Long userId);

    /**
     * find list of all news already seen by the user
     *
     * @param userId of the user
     * @return list of old news
     */
    List<News> findOldNewsByUser(Long userId);

    /**
     * Publish a single news entry
     *
     * @param news to publish
     * @return published news entry
     */
    News publishNews(News news);

}
