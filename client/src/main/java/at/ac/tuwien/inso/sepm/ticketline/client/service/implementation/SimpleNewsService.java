package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.NewsRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.NewsService;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.DetailedNewsDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javafx.scene.control.TextField;
import java.awt.*;
import java.util.List;
import  javafx.scene.control.TextArea;

@Service
public class SimpleNewsService implements NewsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNewsService.class);

    private final NewsRestClient newsRestClient;

    public SimpleNewsService(NewsRestClient newsRestClient) {
        this.newsRestClient = newsRestClient;
    }

    @Override
    public List<SimpleNewsDTO> findAll() throws DataAccessException {
        return newsRestClient.findAll();
    }

    @Override
    public DetailedNewsDTO findById( long id ) throws DataAccessException {
        return newsRestClient.findById(id);
    }

    @Override
    public List<SimpleNewsDTO> findNotSeenByUser( long userId ) throws DataAccessException {
        return newsRestClient.findNotSeenByUser(userId);
    }

    @Override
    public List<SimpleNewsDTO> findOldNewsByUser( long userId ) throws DataAccessException {
        return newsRestClient.findOldNewsByUser(userId);
    }

    @Override
    public DetailedNewsDTO publishNews(DetailedNewsDTO newNews) throws DataAccessException {
       return newsRestClient.publishNews(newNews);
    }

    @Override
    public boolean validateTextField(TextField text) {
        if(text.getText().length()>100)
            return false;
        if(text.getText().isEmpty())
            return false;
        if(text.getText().equals(""))
            return false;

        return true;
    }

    @Override
    public boolean validateTextArea(TextArea text) {
        if(text.getText().length()>1000)
            return false;
        if(text.getText().isEmpty())
            return false;
        if(text.getText().equals(""))
            return false;

        return true;
    }

}
