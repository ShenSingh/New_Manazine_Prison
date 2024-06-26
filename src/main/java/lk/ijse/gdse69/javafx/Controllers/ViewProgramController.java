package lk.ijse.gdse69.javafx.Controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import lk.ijse.gdse69.javafx.Model.Program;
import lk.ijse.gdse69.javafx.Repository.ProgramRepo;
import lk.ijse.gdse69.javafx.Repository.SectionRepo;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ViewProgramController extends MainDashBoard implements Initializable {
    public AnchorPane MainAnchorPane;


    public TableColumn<Program, String> TprogramId;
    public TableColumn<Program, String> Tname;
    public TableColumn<Program, String> TsectionId;
    public TableColumn<Program, String> Tdate;
    public TableColumn<Program, String> Ttime;
    public TableColumn<Program, String> Tdescription;
    public JFXComboBox viewOptionCombo;

    @FXML
    public Button inmateBtn;
    public Button officerBtn;
    public Button dashBoardBtn;
    public Button settingBtn;
    public Button manyBtn;
    public Button sectionBtn;
    public Button visitorBtn;

    public TextField searchId;
    public Text totalSectionCount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewOptionCombo.getItems().addAll("All Programs", "Today Programs","This Month Programs");
        viewOptionCombo.setVisible(false);//not needed in this controller

        try {
            setTableValues(ProgramRepo.getAllPrograms());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setToolTip();
        setSearchIds();

        try {
            totalSectionCount.setText(String.valueOf(SectionRepo.getAllSections().size())+" Sections");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void setSearchIds() {
        List<String> programIds = new ArrayList<>();

        try {
            List<Program> allPograms = ProgramRepo.getAllPrograms();
            for (Program program : allPograms) {
                programIds.add(program.getProgramId()+" - "+program.getProgramName());
            }
            String[] possibleNames = programIds.toArray(new String[0]);

            TextFields.bindAutoCompletion(searchId, possibleNames);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    private void setTableValues(List<Program> allPrograms) {

        TprogramId.setCellValueFactory(new PropertyValueFactory<>("programId"));
        Tname.setCellValueFactory(new PropertyValueFactory<>("programName"));
        TsectionId.setCellValueFactory(new PropertyValueFactory<>("sectionId"));
        Tdate.setCellValueFactory(new PropertyValueFactory<>("programDate"));
        Ttime.setCellValueFactory(new PropertyValueFactory<>("programTime"));
        Tdescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        Tdescription.getTableView().setItems(FXCollections.observableArrayList(allPrograms));

    }

    public void searchIdField(ActionEvent actionEvent) throws IOException {
        String id = searchId.getText().split(" - ")[0];
        SearchId.setProgramId(id);
        createStage("/View/ProgramProfile.fxml");
    }
}
