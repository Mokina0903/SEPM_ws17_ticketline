package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import org.controlsfx.glyphfont.FontAwesome;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class Top10Controller extends TabElement implements LocalizationObserver {

    @FXML
    public BarChart<String, Number> barChartTop10;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public ComboBox comBoxCategory;
    @FXML
    public Button applyFilter;
    @FXML
    private TabHeaderController tabHeaderController;

    private MainController mainController;
    private SpringFxmlLoader loader;
    private TicketService ticketService;
    private EventService eventService;
    private Tab statisticsTab;
    private List<SimpleEventDTO> topTenEventsNow;

    @Autowired
    private LocalizationSubject localizationSubject;

    public Top10Controller(MainController mainController, SpringFxmlLoader loader, EventService eventService, TicketService ticketService) {

        this.loader = loader;
        this.mainController = mainController;
        this.eventService =eventService;
        this.ticketService =ticketService;
    }

    public void initializeData() {

        xAxis.setLabel("Events");
        yAxis.setLabel("Sales");
        barChartTop10.setTitle("Top 10 Events");
        comBoxCategory.getSelectionModel().select(0);

        localizationSubject.attach(this);

        tabHeaderController.setIcon(FontAwesome.Glyph.BAR_CHART);
        tabHeaderController.setTitle(BundleManager.getBundle().getString("statistics.top10Statistics"));

        LocalDate now = LocalDate.now();
        LocalDateTime beginOfThisMonth= LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0);
        LocalDateTime endOfThisMonth= LocalDateTime.of(now.getYear(), now.getMonth(), now.getMonth().length(now.isLeapYear()), 0, 0);

        try {
            topTenEventsNow = eventService.getTop10EventsOfMonth(beginOfThisMonth, endOfThisMonth);
        } catch (DataAccessException e) {
            mainController.showGeneralError("Not able to load top10 Events right now. Server faild to access the data.");
            e.printStackTrace();
        }

        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        series.setName("Top 10");
        for (SimpleEventDTO event : topTenEventsNow) {
            try {
                series.getData().add(new XYChart.Data<String, Number>(event.getTitle(), ticketService.findByEventId(event.getId()).size()));
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        barChartTop10.getData().clear();
        barChartTop10.getData().addAll(series);

    }



    public void applyStatsToChart(ActionEvent actionEvent) {



    }

    @Override
    protected void setTab(Tab tab) {
        statisticsTab = tab;
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("statistics.top10Statistics"));

    }
}
