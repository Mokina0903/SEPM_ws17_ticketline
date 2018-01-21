package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics;

import at.ac.tuwien.inso.sepm.ticketline.client.exception.DataAccessException;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.*;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.customer.CustomerController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.news.NewsElementController;
import at.ac.tuwien.inso.sepm.ticketline.client.service.EventService;
import at.ac.tuwien.inso.sepm.ticketline.client.service.TicketService;
import at.ac.tuwien.inso.sepm.ticketline.client.util.BundleManager;
import at.ac.tuwien.inso.sepm.ticketline.client.util.JavaFXUtils;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.DetailedEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.event.SimpleEventDTO;
import at.ac.tuwien.inso.sepm.ticketline.rest.news.SimpleNewsDTO;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;
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
    public BarChart<Number, String> barChartTop10;
    @FXML
    public CategoryAxis yAxis;
    @FXML
    public NumberAxis xAxis;
    @FXML
    public ComboBox<String> comBoxCategory;
    @FXML
    public Button applyFilter;
    @FXML
    public DatePicker fromDate;
    @FXML
    public DatePicker toDate;
    @FXML
    public Label lblFromDate;
    @FXML
    public Label lblToDate;
    @FXML
    public Button btnGoToBuying;
    @FXML
    public BorderPane myContainer;
    @FXML
    public Label lblNoEventChoosen;
    @FXML
    private TabHeaderController tabHeaderController;

    private MainController mainController;
    private SpringFxmlLoader loader;
    private TicketService ticketService;
    private EventService eventService;
    private Tab statisticsTab;
    private List<SimpleEventDTO> topTenEventsNow;
    private GlyphFont fontAwesome = GlyphFontRegistry.font("FontAwesome");
    private final int FONT_SIZE = 16;

    private long selectedID = -1;
    Node beforeN = null;

    @Autowired
    private LocalizationSubject localizationSubject;

    public Top10Controller(MainController mainController, SpringFxmlLoader loader, EventService eventService, TicketService ticketService) {

        this.loader = loader;
        this.mainController = mainController;
        this.eventService = eventService;
        this.ticketService = ticketService;
    }

    public void initializeData() {
        mainController.setGeneralErrorUnvisable();
        xAxis.setLabel("Events");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Sales");
        yAxis.setTickLabelRotation(90);
        barChartTop10.setTitle("Top 10 Events");
        comBoxCategory.getSelectionModel().select(0);
        btnGoToBuying.setGraphic(fontAwesome.create("TICKET").size(FONT_SIZE));
        applyFilter.setGraphic(fontAwesome.create("REFRESH").size(FONT_SIZE));

        lblNoEventChoosen.setVisible(false);
        lblNoEventChoosen.setWrapText(true);
        lblNoEventChoosen.setMaxWidth(100);
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

        mainController.setGeneralErrorUnvisable();
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
                LOGGER.info("Task succeeded");
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

    private void applyStatsToChart(List<SimpleEventDTO> topTenEvents) {

        fromDate.getStyleClass().remove("error");
        toDate.getStyleClass().remove("error");
        if(btnGoToBuying.getStyleClass().contains("buyBtn")){
            btnGoToBuying.getStyleClass().remove("buyBtn");
        }
        selectedID = -1;

        XYChart.Series<Number, String> series = new XYChart.Series<Number, String>();
        LOGGER.info("Applying stats to char, top 10 event list size: " + topTenEvents.size());

        for (SimpleEventDTO event : topTenEvents) {

            try {
                series.getData().add(new XYChart.Data<Number, String>(ticketService.countByEvent_Id(event.getId()), event.getTitle() + " " + event.getId().toString()));
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        barChartTop10.getData().clear();
        barChartTop10.getData().addAll(series);
        setUpEventHandler(series);

    }

    private void setUpEventHandler(XYChart.Series<Number, String> series) {

        for (final XYChart.Data<Number, String> col : series.getData()) {
            final Node n = col.getNode();
            n.setEffect(null);


            n.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    n.getStyleClass().add("hover");
                }
            });
            n.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    n.getStyleClass().remove("hover");
                }
            });
            n.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    lblNoEventChoosen.setVisible(false);
                    if (n.getStyleClass().contains("selected")) {
                        n.getStyleClass().remove("selected");
                        btnGoToBuying.getStyleClass().remove("buyBtn");
                        selectedID = -1;
                        return;
                    }

                    if (!(beforeN == null)) {
                        beforeN.getStyleClass().remove("selected");
                        btnGoToBuying.getStyleClass().remove("buyBtn");
                    }

                    n.getStyleClass().add("selected");
                    btnGoToBuying.getStyleClass().add("buyBtn");

                    String event = col.getYValue();
                    String[] subStrings = event.split(" ");
                    selectedID = Long.valueOf(subStrings[subStrings.length - 1]);
                    beforeN = n;
                }
            });

        }
    }

    @FXML
    public void goToTicketProzess(ActionEvent actionEvent) {
        mainController.setGeneralErrorUnvisable();
        if (selectedID == -1) {
            lblNoEventChoosen.setVisible(true);
            return;
        }
        lblNoEventChoosen.setVisible(false);


        Task<DetailedEventDTO> eventDTOTask = new Task<DetailedEventDTO>() {
            @Override
            protected DetailedEventDTO call() throws Exception {
                return eventService.findById(selectedID);
            }

            @Override
            protected void succeeded() {
                LOGGER.info("Task succeeded");
                super.succeeded();
                mainController.setEvent(getValue());
            }

            @Override
            protected void failed() {
                LOGGER.debug("Loading top ten events failed.");
                if (getValue() == null) {
                    super.failed();
                    mainController.showGeneralError("Failed to load the top ten events");
                }
            }
        };

        eventDTOTask.runningProperty().addListener((observable, oldValue, running) ->
            mainController.setProgressbarProgress(
                running ? ProgressBar.INDETERMINATE_PROGRESS : 0));

        new Thread(eventDTOTask).start();

        SpringFxmlLoader.Wrapper<CustomerController> wrapper =
            loader.loadAndWrap("/fxml/customer/customerComponent.fxml");
        Node root = loader.load("/fxml/customer/customerComponent.fxml");
        CustomerController c = wrapper.getController();
        c.preparePagination();
        c.setTicketProzessView(statisticsTab);
        c.setOldContent(myContainer);


        statisticsTab.setContent(root);

    }


    @Override
    protected void setTab(Tab tab) {
        statisticsTab = tab;
    }

    @Override
    public void update() {
        tabHeaderController.setTitle(BundleManager.getBundle().getString("statistics.top10Statistics"));
        lblFromDate.setText(BundleManager.getBundle().getString("statistics.fromDate"));
        lblToDate.setText(BundleManager.getBundle().getString("statistics.toDate"));

        if (lblNoEventChoosen.isVisible()) {
            lblNoEventChoosen.setText(BundleManager.getBundle().getString("statistics.noEvetChoosen"));
        }

    }


}
