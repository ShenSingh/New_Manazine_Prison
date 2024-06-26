package lk.ijse.gdse69.javafx.Controllers;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lk.ijse.gdse69.javafx.Alert.ShowAlert;
import lk.ijse.gdse69.javafx.Model.Visitor;
import lk.ijse.gdse69.javafx.Model.VisitorRecord;
import lk.ijse.gdse69.javafx.Repository.VisitorRecordRepo;
import lk.ijse.gdse69.javafx.Repository.VisitorRepo;
import lk.ijse.gdse69.javafx.Util.Util;
import org.controlsfx.control.textfield.TextFields;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class VisitorProfileController extends MainDashBoard implements Initializable {
    public AnchorPane MainAnchorPane;

    public TableColumn<VisitorRecord, String> tvRecordId;
    public TableColumn<VisitorRecord, String> tvVisitorId;
    public TableColumn<VisitorRecord, String> tvInmateId;
    public TableColumn<VisitorRecord, String> tvDate;
    public TableColumn<VisitorRecord, String> tvTime;
    public TextField status;
    public Text sideVisitorName;

    @FXML
    private ImageView visitorImg;

    @FXML
    private TextField searchVisitorField;

    @FXML
    private TextField visitorId;
    @FXML
    private TextField fName;
    @FXML
    private TextField lName;
    @FXML
    private DatePicker DOB;
    @FXML
    private TextField NIC;
    @FXML
    private ComboBox<String> gender;
    @FXML
    private TextField number;
    @FXML
    private TextField address;
    @FXML
    private ComboBox<String> visitorType;

    @FXML
    private AnchorPane line1;
    @FXML
    private AnchorPane line2;
    @FXML
    private AnchorPane line3;
    @FXML
    private AnchorPane line4;
    @FXML
    private AnchorPane line5;
    @FXML
    private AnchorPane line6;
    @FXML
    private AnchorPane line7;
    @FXML
    private AnchorPane line8;
    @FXML
    private AnchorPane line9;

    @FXML
    public Button inmateBtn;
    public Button officerBtn;
    public Button dashBoardBtn;
    public Button settingBtn;
    public Button manyBtn;
    public Button sectionBtn;
    public Button visitorBtn;

    private boolean isEditingEnabled = false;
    private boolean isIconNamesVisible = false;

    @FXML
    private AnchorPane vBoxAnchorPane;

    TranslateTransition slideTransition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setToolTip();

        try {
            visitorPageVisitorId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setSearchIds();
        setComboBoxValues();

        slideTransition = new TranslateTransition(Duration.seconds(0.3), vBoxAnchorPane);
        slideTransition.setToX(450);
        vBoxAnchorPane.setVisible(false);
        visitorId.setEditable(false);
    }

    private void setSearchIds() {
        List<String> visitorIds = new ArrayList<>();

        try {
            List<Visitor> allVisitors = VisitorRepo.getAllVisitors();
            for (Visitor visitor : allVisitors) {
                visitorIds.add(visitor.getVisitorID()+" - "+visitor.getVisitorFirstName()+" "+visitor.getVisitorLastName());
            }
            String[] possibleNames = visitorIds.toArray(new String[0]);

            TextFields.bindAutoCompletion(searchVisitorField, possibleNames);
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

    private void visitorPageVisitorId() throws SQLException {

        String id = SearchId.getVisitorId();
        if(id != null){}else{return;}

        Visitor visitor = VisitorRepo.search(id);

        if (visitor != null){
            sideVisitorName.setText(visitor.getVisitorFirstName() + " " + visitor.getVisitorLastName());
            setTableValues(visitor.getVisitorID());
            searchVisitorField.setText(visitor.getVisitorID());
            visitorId.setText(visitor.getVisitorID());
            fName.setText(visitor.getVisitorFirstName());
            lName.setText(visitor.getVisitorLastName());
            DOB.setValue(visitor.getVisitorDOB().toLocalDate());
            NIC.setText(visitor.getVisitorNIC());
            number.setText(String.valueOf(visitor.getVisitorNumber()));
            address.setText(visitor.getVisitorAddress());
            gender.getSelectionModel().select(visitor.getGender());
            visitorType.getSelectionModel().select(visitor.getVisitorType());

            visitorImg.setFitWidth(100);
            visitorImg.setFitHeight(100);
            Circle clip = new Circle(visitorImg.getFitWidth() / 2, visitorImg.getFitHeight() / 2, visitorImg.getFitWidth() / 2);
            visitorImg.setClip(clip);
            Image image = Util.showImage(visitor.getVisitorImage());
            visitorImg.setImage(image);
        }
    }

    private void setComboBoxValues() {
        gender.getItems().addAll("Male", "Female");
        visitorType.getItems().addAll("Immediate Family", "Legal Representative", "Officials", "Others");
    }

    public void searchVisitor(ActionEvent actionEvent) throws SQLException {

        if(!searchVisitorField.getText().isEmpty()){
            String searchVisitorId = searchVisitorField.getText().split(" - ")[0];;

            if (VisitorRepo.search(searchVisitorId) != null) {
                Visitor visitor = VisitorRepo.search(searchVisitorId);

                setTableValues(visitor.getVisitorID());
                visitorId.setText(visitor.getVisitorID());
                fName.setText(visitor.getVisitorFirstName());
                lName.setText(visitor.getVisitorLastName());
                DOB.setValue(visitor.getVisitorDOB().toLocalDate());
                NIC.setText(visitor.getVisitorNIC());
                number.setText(String.valueOf(visitor.getVisitorNumber()));
                address.setText(visitor.getVisitorAddress());
                gender.getSelectionModel().select(visitor.getGender());
                visitorType.getSelectionModel().select(visitor.getVisitorType());
            }
        }else {
            ShowAlert.showErrorNotify("Empty Field. Please Enter Visitor ID");
        }
    }

    private void setTableValues(String visitorID) {
        List<VisitorRecord> visitorRecords=new ArrayList<>();
        try {
            visitorRecords = VisitorRecordRepo.getVisitorRecords(visitorID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tvRecordId.setCellValueFactory(new PropertyValueFactory<>("visitorRecordId"));
        tvVisitorId.setCellValueFactory(new PropertyValueFactory<>("visitorId"));
        tvInmateId.setCellValueFactory(new PropertyValueFactory<>("inmateId"));
        tvDate.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
        tvTime.setCellValueFactory(new PropertyValueFactory<>("visitTime"));

        tvTime.getTableView().setItems(FXCollections.observableArrayList(visitorRecords));
    }

    public void deleteVisitor(ActionEvent actionEvent){
        if (searchVisitorField.getText().isEmpty()){
            ShowAlert.showErrorNotify("Please enter Visitor ID");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information");
        alert.setHeaderText("Delete Visitor");
        alert.setContentText("Are you sure you want to delete this Visitor?");

        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);

        okButton.setOnAction(e -> {
            try {
                goDeleteVisitor();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            alert.close();
        });
        cancelButton.setOnAction(e -> {
            alert.close();
        });

        alert.showAndWait();
    }
    private void goDeleteVisitor() throws SQLException {
        if (!searchVisitorField.getText().isEmpty()) {
            String visitorId = searchVisitorField.getText().split(" - ")[0];;

            if (VisitorRepo.delete(visitorId)) {
                ShowAlert.showSuccessNotify("Visitor Deleted Successfully");
            }else {
                ShowAlert.showErrorNotify("Visitor Delete Failed");
            }
        }else {
            ShowAlert.showErrorNotify("Empty Field. Please Enter Visitor ID");
        }
    }

    public void showRecordsBtn(ActionEvent actionEvent) {
        vBoxAnchorPane.setVisible(true);

        // Toggle visibility and animate iconsPane
        isIconNamesVisible = !isIconNamesVisible;
        if (isIconNamesVisible) {
            slideTransition.setToX(450); // Slide to reveal names
            centerContentHideShow(isIconNamesVisible);
        } else {
            slideTransition.setToX(0); // Slide to hide names
            centerContentHideShow(isIconNamesVisible);
        }
        slideTransition.play();

    }

    private void centerContentHideShow(boolean isEnebled) {
        visitorId.setVisible(isEnebled);
        fName.setVisible(isEnebled);
        lName.setVisible(isEnebled);
        DOB.setVisible(isEnebled);
        NIC.setVisible(isEnebled);
        gender.setVisible(isEnebled);
        number.setVisible(isEnebled);
        address.setVisible(isEnebled);
        visitorType.setVisible(isEnebled);

        line1.setVisible(isEnebled);
        line2.setVisible(isEnebled);
        line3.setVisible(isEnebled);
        line4.setVisible(isEnebled);
        line5.setVisible(isEnebled);
        line6.setVisible(isEnebled);
        line7.setVisible(isEnebled);
        line8.setVisible(isEnebled);
        line9.setVisible(isEnebled);
    }

    public void editProfileTogal(ActionEvent actionEvent) {
        isEditingEnabled = !isEditingEnabled;

        fName.setEditable(isEditingEnabled);
        lName.setEditable(isEditingEnabled);
        DOB.setDisable(!isEditingEnabled);
        NIC.setEditable(isEditingEnabled);
        number.setEditable(isEditingEnabled);
        address.setEditable(isEditingEnabled);

        for (String item : gender.getItems()){
            gender.setDisable(!isEditingEnabled);
        }

        for (String item : visitorType.getItems()){
            visitorType.setDisable(!isEditingEnabled);
        }
    }

    public void saveBtn(ActionEvent actionEvent) throws SQLException {

        byte[] image = VisitorRepo.search(searchVisitorField.getText()).getVisitorImage();

        if (checkEmptyFields()) {

            if(validInput()){}else{return;}

            Visitor visitor = new Visitor(visitorId.getText(), fName.getText(), lName.getText(), java.sql.Date.valueOf(DOB.getValue()), NIC.getText(), Integer.parseInt(number.getText()), address.getText(), visitorType.getSelectionModel().getSelectedItem(), gender.getSelectionModel().getSelectedItem(),image);

            if (VisitorRepo.update(visitor)) {
                ShowAlert.showSuccessNotify("Visitor Updated Successfully");
            }else {
                ShowAlert.showErrorNotify("Visitor Update Failed");
            }
        }else {
            ShowAlert.showErrorNotify("Empty Fields. Please Fill All Fields");
        }
    }

    private boolean validInput() {
        if (!fName.getText().matches("[a-zA-Z]+") && !lName.getText().matches("[a-zA-Z]+") ) {
            ShowAlert.showErrorNotify("Invalid Name. please Use Letters Only");
            return false;
        }
        if (!NIC.getText().matches("[0-9]{9}[vVxX]")) {
            ShowAlert.showErrorNotify("Invalid NIC Number EX: 123456789V");
            return false;
        }
        if (!number.getText().matches("[0-9]{10}")) {
            ShowAlert.showErrorNotify("Invalid Phone Number EX: 0712345678");
            return false;
        }
        return true;
    }

    private boolean checkEmptyFields() {
        if (visitorId.getText().isEmpty() || fName.getText().isEmpty() || lName.getText().isEmpty() || DOB.getValue() == null || NIC.getText().isEmpty() || number.getText().isEmpty() || address.getText().isEmpty() ||
                gender.getSelectionModel().getSelectedItem() == null || visitorType.getSelectionModel().getSelectedItem() == null) {

            return false;
        }else {
            return true;
        }
    }

    public void cancelBtn(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        visitorId.clear();
        fName.clear();
        lName.clear();
        DOB.getEditor().clear();
        NIC.clear();
        number.clear();
        address.clear();
        gender.getSelectionModel().clearSelection();
        visitorType.getSelectionModel().clearSelection();
        searchVisitorField.clear();
    }
}
