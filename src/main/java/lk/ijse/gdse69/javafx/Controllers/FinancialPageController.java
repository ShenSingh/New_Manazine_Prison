package lk.ijse.gdse69.javafx.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.gdse69.javafx.Repository.ExpencesRepo;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class FinancialPageController extends MainDashBoard implements Initializable {
    public AnchorPane MainAnchorPane;
    public LineChart<String, Number> expencesLineChart;

    public Text iyHelthExpen;
    public Text lyEquipmentExpen;
    public Text lyElectricExpen;
    public Text lyStaffExpen;
    public Text lyWaterExpen;
    public Text lyFoodExpen;

    @FXML
    public Button inmateBtn;
    public Button officerBtn;
    public Button dashBoardBtn;
    public Button settingBtn;
    public Button manyBtn;
    public Button sectionBtn;
    public Button visitorBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLyValues();
        Map<String, Double> totalCost = ExpencesRepo.getTotalCostByType();

        for (Map.Entry<String, Double> entry : totalCost.entrySet()) {
            System.out.println("Expense Type: " + entry.getKey() + ", Total Cost: " + entry.getValue());

            if (entry.getKey().equals("Health")) {
                iyHelthExpen.setText(String.valueOf(entry.getValue()));
            }else if (entry.getKey().equals("Equipment")){
                lyEquipmentExpen.setText(String.valueOf(entry.getValue()));
            }else if (entry.getKey().equals("Electric")){
                lyElectricExpen.setText(String.valueOf(entry.getValue()));
            }else if (entry.getKey().equals("Staff")){
                lyStaffExpen.setText(String.valueOf(entry.getValue()));
            }else if (entry.getKey().equals("Water")){
                lyWaterExpen.setText(String.valueOf(entry.getValue()));
            }else if (entry.getKey().equals("Food")){
                lyFoodExpen.setText(String.valueOf(entry.getValue()));
            }
        }
        setToolTip();
        setValuesLineChart();
    }

    private void setValuesLineChart() {
        try {
            Map<String, XYChart.Series<String, Number>> expensesData = ExpencesRepo.getExpensesDataForLineChart();

            expencesLineChart.getData().clear();

            for (Map.Entry<String, XYChart.Series<String, Number>> entry : expensesData.entrySet()) {
                expencesLineChart.getData().add(entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setToolTip() {
        Tooltip.install(inmateBtn, new Tooltip("Inmate Management"));
        Tooltip.install(officerBtn, new Tooltip("Officer Management"));
        Tooltip.install(dashBoardBtn, new Tooltip("DashBoard"));
        Tooltip.install(settingBtn, new Tooltip("Setting"));
        Tooltip.install(manyBtn, new Tooltip("Financial Management"));
        Tooltip.install(sectionBtn, new Tooltip("Section Management"));
        Tooltip.install(visitorBtn, new Tooltip("Visitor Management"));
    }

    private void setLyValues() {

    }

    public void staffBtn(ActionEvent actionEvent) {
    }

    public void healthBtn(ActionEvent actionEvent) {
    }

    public void equipmentBtn(ActionEvent actionEvent) {
    }

}
