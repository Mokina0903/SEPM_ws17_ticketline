package at.ac.tuwien.inso.sepm.ticketline.client.service.implementation;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.rest.LocationRestClient;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.DetailedHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.hall.SimpleHallDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.DetailedLocationDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Service
public class SimpleLocationService implements LocationService{

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleLocationService.class);

    private final LocationRestClient locationRestClient;

    public SimpleLocationService( LocationRestClient locationRestClient ) {
        this.locationRestClient = locationRestClient;
    }

    @Override
    public DetailedLocationDTO findLocationById( Long locationId ) throws DataAccessException {
        return locationRestClient.findLocationById(locationId);
    }

    @Override
    public List<SimpleLocationDTO> findAllLocations() throws DataAccessException {
        return locationRestClient.findAllLocations();
    }

    @Override
    public DetailedHallDTO findHallById( Long hallId ) throws DataAccessException {
        return locationRestClient.findHallById(hallId);
    }

    @Override
    public List<SimpleHallDTO> findAllHalls() throws DataAccessException {
        return locationRestClient.findAllHalls();
    }

    @Override
    public Page<SimpleLocationDTO> find(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException{
        parameters = replaceSpaces(parameters);
        return locationRestClient.find(request, parameters);
    }

    @Override
    public Page<SimpleLocationDTO> findAdvanced(Pageable request, MultiValueMap<String, String> parameters) throws DataAccessException {
        parameters = replaceSpaces(parameters);
        return locationRestClient.findAdvanced(request, parameters);
    }

    private MultiValueMap<String, String> replaceSpaces(MultiValueMap<String, String> parameters) {
        for(String key : parameters.keySet()) {
            String replace = parameters.getFirst(key);
            replace = replace.replaceAll("[-+.^:,#%;{}()]","_");
            if (replace.contains(" ")) {
                replace = replace.replaceAll(" ", "_").toLowerCase();
            }
            parameters.set(key, replace);
        }
        return parameters;
    }
}
