package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.MainController;
import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import at.ac.tuwien.inso.springfx.SpringFxmlLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import org.springframework.stereotype.Component;


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
    private Tab statisticsTab;

    public Top10Controller(MainController mainController, SpringFxmlLoader loader) {

        this.loader = loader;
        this.mainController = mainController;
    }

    public void initializeData() {

        xAxis.setLabel("Events");
        yAxis.setLabel("Sales");
        barChartTop10.setTitle("Top 10 Events");

    }

    public void applyStatsToChart(ActionEvent actionEvent) {



    }

    @Override
    protected void setTab(Tab tab) {
        statisticsTab = tab;
    }
}
