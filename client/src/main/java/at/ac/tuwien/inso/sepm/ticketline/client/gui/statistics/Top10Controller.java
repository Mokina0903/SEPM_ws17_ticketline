package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;


@Component
public class Top10Controller extends TabElement {

    @FXML
    public BarChart barChartTop10;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public ComboBox comBoxKategory;

    private MainController mainController;
    private SpringFxmlLoader loader;
    private TicketService ticketService;
    private EventService eventService;
    private Tab statisticsTab;
    private List<SimpleEventDTO> topTenEventsNow;

    public Top10Controller(MainController mainController, SpringFxmlLoader loader, EventService eventService, TicketService ticketService) {

        this.loader = loader;
        this.mainController = mainController;
    }

    public void initializeData() {

        xAxis.setLabel("Events");
        yAxis.setLabel("Sales");
        barChartTop10.setTitle("Top 10 Events");

        LocalDate now = LocalDate.now();
        LocalDateTime beginOfThisMonth= LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime endOfThisMonth= LocalDateTime.of(now.getYear(), now.getMonth(), now.getMonth().length(now.isLeapYear()), 0, 0);

        try {
            topTenEventsNow = eventService.getTop10EventsOfMonth(beginOfThisMonth, endOfThisMonth);
        } catch (DataAccessException e) {
            mainController.showGeneralError("Not able to load top10 Events right now. Server faild to access the data.");
            e.printStackTrace();
        }

    }



    public void applyStatsToChart(ActionEvent actionEvent) {



    }

    @Override
    protected void setTab(Tab tab) {
        statisticsTab = tab;
    }
}
