package at.ac.tuwien.inso.sepm.ticketline.client.gui.statistics;

import at.ac.tuwien.inso.sepm.ticketline.client.gui.TabElement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
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

    public Tab statisticsTab;


    public void applyStatsToChart(ActionEvent actionEvent) {
    }

    @Override
    protected void setTab(Tab tab) {
        statisticsTab = tab;
    }
}
