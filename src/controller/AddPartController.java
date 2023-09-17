package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

/**
 * Controller for Add Part
 */
public class AddPartController implements Initializable {

    private String exceptionMessage = "";

    public ToggleGroup inHouseOrOutsourcedAddPart;

    boolean isOutsourced;

    private static int partId = 7;

    @FXML
    private Label radioLabelAddPart;

    @FXML
    private TextField addPartIdTxtField;

    @FXML
    private RadioButton addPartInHouseRBtn;

    @FXML
    private TextField addPartInventoryTxtField;

    @FXML
    private TextField addPartMachOrCompTxtField;

    @FXML
    private TextField addPartMaxTxtField;

    @FXML
    private TextField addPartMinTxtField;

    @FXML
    private TextField addPartNameTxtField;

    @FXML
    private RadioButton addPartOutsourcedRBtn;

    @FXML
    private TextField addPartPriceTxtField;


    /**
     * Confirms Add Part cancel and user is brought back to the Main Screen
     * @param event action event when Cancel button is clicked
     */
    @FXML
    public void onActionAddPartCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Add Part");
        alert.setHeaderText("Return to Main Screen");
        alert.setContentText("Are you sure you want to cancel?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * In-House radio button is selected and displays Machine ID label
     * @param event action event when In-House radio button is clicked
     */
    @FXML
    public void onActionAddPartInHouse(ActionEvent event) throws IOException {

        radioLabelAddPart.setText("Machine ID");
    }

    /**
     * Outsourced radio button is selected and displays Company Name label
     * @param event action event when Outsourced radio button is clicked
     */
    @FXML
    public void onActionAddPartOutsourced(ActionEvent event) throws IOException {

        radioLabelAddPart.setText("Company Name");
    }

    /**
     * <p>
     * Saves the part that is created.
     * </p>
     * <p>
     * Any invalid entries in text fields found stops the save and displays error message with list of exceptions.
     * </p>
     * partId++ auto generates/increments a new part ID.
     * @param event action event when save button is clicked and takes user back to Main Screen
     */
    @FXML
    public void onActionAddPartSave(ActionEvent event) throws IOException {

        String partName = addPartNameTxtField.getText();
        String partStock = addPartInventoryTxtField.getText();
        String partPrice = addPartPriceTxtField.getText();
        String partMin = addPartMinTxtField.getText();
        String partMax = addPartMaxTxtField.getText();
        String partMachOrComp = addPartMachOrCompTxtField.getText();

        try {
            exceptionMessage = Product.isPartValid(partName, Integer.parseInt(partStock), Integer.parseInt(partMax), Integer.parseInt(partMin), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Part Error");
                alert.setHeaderText("Input Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (!addPartOutsourcedRBtn.isSelected()) {
                    System.out.println("Part name: " + partName);
                    InHouse newPartInHouse = new InHouse(partId, partName, Integer.parseInt(partStock), Integer.parseInt(partMax), Integer.parseInt(partMin), Double.parseDouble(partPrice), Integer.parseInt(partMachOrComp));
                    Inventory.addPart(newPartInHouse);
                } else {
                    System.out.println("Part name: " + partName);
                    Outsourced newPartOutsourced = new Outsourced(partId, partName, Integer.parseInt(partStock), Integer.parseInt(partMax), Integer.parseInt(partMin), Double.parseDouble(partPrice), partMachOrComp);
                    Inventory.addPart(newPartOutsourced);
                }
                partId++;
                Parent addPartSave = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(addPartSave);
                Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Part Error");
            alert.setHeaderText("Input Error");
            alert.setContentText("Form contains blank or invalid fields");
            alert.showAndWait();
        }
    }

    /**
     * <p>
     * Only 1 radio button can be selected at a time, default selection is Outsourced.
     * </p>
     * "Auto Gen - Disabled" sets the part ID text field to be auto generated and disables it, so it cannot be changed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        inHouseOrOutsourcedAddPart = new ToggleGroup();
        this.addPartInHouseRBtn.setToggleGroup(inHouseOrOutsourcedAddPart);
        this.addPartOutsourcedRBtn.setToggleGroup(inHouseOrOutsourcedAddPart);
        this.addPartOutsourcedRBtn.setSelected(true);
        isOutsourced = true;

        addPartIdTxtField.setText("Auto Gen - Disabled");
        addPartIdTxtField.setDisable(true);

    }
}
