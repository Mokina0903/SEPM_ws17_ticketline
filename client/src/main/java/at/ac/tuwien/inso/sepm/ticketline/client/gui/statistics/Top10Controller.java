package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.CheckBox;

public class Top10Controller {

    @FXML
    public BarChart barChartTop10;
    @FXML
    public CategoryAxis xAxis;
    @FXML
    public NumberAxis yAxis;


    public void applyStatsToChart(ActionEvent actionEvent) {
    }
}
