package at.ac.tuwien.inso.sepm.ticketline.server.tests.integrationtest;

import at.ac.tuwien.inso.sepm.ticketline.server.repository.Location.LocationRepository;
import at.ac.tuwien.inso.sepm.ticketline.server.tests.base.BaseIntegrationTest;
import org.springframework.boot.test.mock.mockito.MockBean;

public class LocationEndpointTest extends BaseIntegrationTest {

    @MockBean
    private LocationRepository locationRepository;

    // TODO: get /eventlocation/hall        Get simple information about a all hall entries
    // TODO: get /eventlocation/hall/{hallId}        Get detailed information about a specific hall entry
    // TODO: get /eventlocation/location        Get simple information about a all location entries
    // TODO: get /eventlocation/location/locationSearch/{pageIndex}/{locationsPerPage}        Get list of simple location entries filtered by parameters
    // TODO: get /eventlocation/location/{locationId}        Get detailed information about a specific location entry

}
