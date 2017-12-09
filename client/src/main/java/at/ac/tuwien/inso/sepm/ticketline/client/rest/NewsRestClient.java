package at.ac.tuwien.inso.sepm.ticketline.client.rest;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;

import java.util.List;

public interface NewsRestClient {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll() throws DataAccessException;

    /**
     * Find specific News by id
     *
     * @param id of the news searched for
     * @return Detailed news
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO findById(Long id) throws DataAccessException;


    /**
     * find new news for user
     *
     * @param userId of the user
     * @return list of new news
     * @throws DataAccessException
     */
    List<SimpleNewsDTO> findNotSeenByUser(Long userId) throws DataAccessException;

    /**
     * find old news for user
     *
     * @param userId of the user
     * @return list of old news
     * @throws DataAccessException
     */
    List<SimpleNewsDTO> findOldNewsByUser(Long userId) throws DataAccessException;

    /**
     * save new news and save news for all users
     *
     * @param newNews to publish
     * @return published news with date and id
     * @throws DataAccessException
     */
    DetailedNewsDTO publishNews(DetailedNewsDTO newNews) throws DataAccessException;
}
