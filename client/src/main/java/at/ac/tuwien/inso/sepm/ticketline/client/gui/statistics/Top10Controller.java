package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Component
public class Top10Controller extends TabElement implements LocalizationObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsController.class);

    @FXML
    public BarChart<String, Number> barChartTop10;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public NumberAxis yAxis;
    @FXML
    public ComboBox<String> comBoxCategory;
    @FXML
    public Button applyFilter;
    @FXML
    public DatePicker fromDate;
    @FXML
    public DatePicker toDate;
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
        this.eventService = eventService;
        this.ticketService = ticketService;
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
        LocalDate beginOfThisMonth = LocalDateTime.of(now.getYear(), now.getMonth(), 1, 0, 0).toLocalDate();
        LocalDate endOfThisMonth = LocalDateTime.of(now.getYear(), now.getMonth(), now.getMonth().length(now.isLeapYear()), 0, 0).toLocalDate();

        try {
            topTenEventsNow = eventService.getTop10EventsOfMonth(beginOfThisMonth, endOfThisMonth);
        } catch (DataAccessException e) {
            mainController.showGeneralError("Not able to load top10 Events right now. Server faild to access the data.");
            e.printStackTrace();
        }

        applyStatsToChart(topTenEventsNow);

    }


    public void getTopTenEvents(ActionEvent actionEvent) {

        LocalDate beginDate = fromDate.getValue();
        LocalDate endDate = toDate.getValue();
        String category = comBoxCategory.getSelectionModel().getSelectedItem();
        if (!eventService.checkDates(beginDate, endDate)) {
            fromDate.getStyleClass().add("error");
            toDate.getStyleClass().add("error");
            return;
        } else {
            fromDate.getStyleClass().remove("error");
            toDate.getStyleClass().remove("error");
        }

        Task<List<SimpleEventDTO>> getTopTenEvents = new Task<>() {
            @Override
            protected List<SimpleEventDTO> call() throws DataAccessException, InterruptedException {

                if (category.equals("All")) {
                    return eventService.getTop10EventsOfMonth(beginDate, endDate);
                } else {
                    return eventService.getTop10EventsOfMonthFilteredbyCategory(beginDate, endDate, category);
                }
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                applyStatsToChart(getValue());
            }

            @Override
            protected void failed() {
                LOGGER.debug("Loading top ten events failed.");
                if (getValue() == null || getValue().isEmpty()) {
                    super.failed();
                    mainController.showGeneralError("Failed to load the top ten events");
                }
            }
        };
        getTopTenEvents.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0)
        );

        new Thread(getTopTenEvents).start();

    }

    public void applyStatsToChart(List<SimpleEventDTO> topTenEvents) {

        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        LOGGER.debug("This is the size of the top 10 event list: " + topTenEvents.size());
        for (SimpleEventDTO event : topTenEvents) {
            try {
                series.getData().add(new XYChart.Data<String, Number>(event.getTitle()+" "+event.getId().toString(), ticketService.countByEvent_Id(event.getId())));
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        barChartTop10.getData().clear();
        barChartTop10.getData().addAll(series);


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
