package at.ac.tuwien.inso.sepm.ticketline.client.gui;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.event.EventElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.location.LocationElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.LocationService;
import at.ac.tuwien.inso.sepm.ticketline.rest.PageableDAO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.eventLocation.location.SimpleLocationDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Component;

@Component
public class PaginationHelper {


    private Pagination pagination;
    private BorderPane bPContainer;
    private SpringFxmlLoader springFxmlLoader;
    private EventController  controller;
    private EventService eventService;
    private LocationService locationService;

    private final int ENTRIES_PER_PAGE = 7;


    public void initData(Pagination pagination, SpringFxmlLoader springFxmlLoader,
                         EventService eventService, LocationService locationService) {
        this.pagination = pagination;
        this.springFxmlLoader = springFxmlLoader;
        this.eventService = eventService;
        this.locationService = locationService;
    }

    public <T extends PageableDAO> Node setUpPagination(Page<T> page) {
        pagination.setPageCount(page.getTotalPages());
        pagination.setCurrentPageIndex(0);
        pagination.setPageFactory(new Callback<Integer, Node>() {

            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex, page);
            }
        });
        return null;
    }

    private <T extends PageableDAO> Node createPage(int pageIndex, Page<T> page) {
        pagination.setCurrentPageIndex(pageIndex);
        ListView<VBox> lvElements = new ListView<>();
        lvElements.setStyle("-fx-background-color: transparent;");

        if (!page.getContent().isEmpty()) {
            if (page.getContent().get(0).getClass().equals(SimpleEventDTO.class)) {
                for (PageableDAO element : page.getContent()) {
                    SpringFxmlLoader.Wrapper<EventElementController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/event/eventElement.fxml");
                    wrapper.getController().initializeData(eventService, (SimpleEventDTO) element, bPContainer);
                    lvElements.getItems().add(wrapper.getController().vbElement);
                }

            } else if (page.getContent().get(0).getClass().equals(SimpleLocationDTO.class)) {
                for (PageableDAO element : page.getContent()) {
                    SpringFxmlLoader.Wrapper<LocationElementController> wrapper =
                        springFxmlLoader.loadAndWrap("/fxml/location/locationElement.fxml");
                    wrapper.getController().initializeData(locationService, (SimpleLocationDTO) element, bPContainer);
                    lvElements.getItems().add(wrapper.getController().vbElement);
                }
            }
        }
        return lvElements;
    }


    public EventController getController() {
        return controller;
    }

    public void setController(EventController controller) {
        this.controller = controller;
    }
}
