package at.ac.tuwien.inso.sepm.ticketline.client.service;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;


import java.util.List;

public interface NewsService {

    /**
     * Find all news entries.
     *
     * @return list of news entries
     * @throws DataAccessException in case something went wrong
     */
    List<SimpleNewsDTO> findAll() throws DataAccessException;

    /**
     * Find specific news entry by id
     *
     * @param id of news entry
     * @return detailed news
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO findById(long id) throws DataAccessException;

    /**
     * publishes new added News
     *
     * @param newNews to be published
     * @throws DataAccessException in case something went wrong
     */
    DetailedNewsDTO publishNews(DetailedNewsDTO newNews) throws DataAccessException;
}
