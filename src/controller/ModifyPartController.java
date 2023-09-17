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
 * Controller for Modify Part
 */
public class ModifyPartController implements Initializable {

    private String exceptionMessage = "";

    int partIndex = 0;

    private ToggleGroup inHouseOrOutsourcedModifyPart;

    boolean isOutsourced;

    @FXML
    private Label radioLabelModifyPart;

    @FXML
    private TextField modPartIdTxtField;

    @FXML
    private RadioButton modPartInHouseRBtn;

    @FXML
    private TextField modPartInventoryTxtField;

    @FXML
    private TextField modPartMachOrCompTxtField;

    @FXML
    private TextField modPartMaxTxtField;

    @FXML
    private TextField modPartMinTxtField;

    @FXML
    private TextField modPartNameTxtField;

    @FXML
    private RadioButton modPartOutsourcedRBtn;

    @FXML
    private TextField modPartPriceTextField;


    /**
     * Confirms Modify Part cancel and user is brought back to the Main Screen
     * @param event action event when Cancel button is clicked
     */
    @FXML
    public void onActionModPartCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modify Part");
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
    public void onActionModPartInHouse(ActionEvent event) throws IOException {

        radioLabelModifyPart.setText("Machine ID");
    }

    /**
     * Outsourced radio button is selected and displays Company Name label
     * @param event action event when Outsourced radio button is clicked
     */
    @FXML
    public void onActionModPartOutsourced(ActionEvent event) throws IOException {

        radioLabelModifyPart.setText("Company Name");
    }

    /**
     * <p>
     * Saves the part that is modified.
     * </p>
     * <p>
     * Any invalid entries in text fields found stops the save and displays error message with list of exceptions.
     * </p>
     * partId++ auto generates/increments a new part ID.
     * @param event action event when save button is clicked and takes user back to Main Screen
     */
    @FXML
    public void onActionModPartSave(ActionEvent event) throws IOException {

        int partId = Integer.parseInt(modPartIdTxtField.getText());
        String partName = modPartNameTxtField.getText();
        String partStock = modPartInventoryTxtField.getText();
        String partPrice = modPartPriceTextField.getText();
        String partMin = modPartMinTxtField.getText();
        String partMax = modPartMaxTxtField.getText();
        String partMachOrComp = modPartMachOrCompTxtField.getText();

        try {
            exceptionMessage = Product.isPartValid(partName, Integer.parseInt(partStock), Integer.parseInt(partMax), Integer.parseInt(partMin), Double.parseDouble(partPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Part Error");
                alert.setHeaderText("Input Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            }
            else {
                if (!modPartOutsourcedRBtn.isSelected()) {
                    System.out.println("Part name: " + partIndex);
                    InHouse newPartInHouse = new InHouse(partId, partName, Integer.parseInt(partStock), Integer.parseInt(partMax), Integer.parseInt(partMin), Double.parseDouble(partPrice), Integer.parseInt(partMachOrComp));
                    Inventory.updatePart(partIndex, newPartInHouse);
                } else {
                    System.out.println("Part name: " + partIndex);
                    Outsourced newPartOutsourced = new Outsourced(partId, partName, Integer.parseInt(partStock), Integer.parseInt(partMax), Integer.parseInt(partMin), Double.parseDouble(partPrice), partMachOrComp);
                    Inventory.updatePart(partIndex, newPartOutsourced);
                }
                Parent modifyProductSave = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSave);
                Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch(NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Modify Part Error");
            alert.setHeaderText("Input Error");
            alert.setContentText("Form contains blank or invalid fields");
            alert.showAndWait();

        }
    }

    Part part;
    /**
     * Used in Main Screen Controller to load selected row to Modify Part screen
     */
    public void setPart(Part part, int pIndex) {
        this.part = part;
        this.partIndex = pIndex;
        modPartIdTxtField.setText(Integer.toString(part.getId()));
        modPartNameTxtField.setText(part.getName());
        modPartInventoryTxtField.setText(Integer.toString(part.getStock()));
        modPartPriceTextField.setText(Double.toString(part.getPrice()));
        modPartMaxTxtField.setText(Integer.toString(part.getMax()));
        modPartMinTxtField.setText(Integer.toString(part.getMin()));

        if (part instanceof InHouse) {
            InHouse inHousePart = (InHouse)part;
            modPartMachOrCompTxtField.setText(Integer.toString(inHousePart.getMachineId()));
            radioLabelModifyPart.setText("Machine ID");
            modPartInHouseRBtn.setSelected(true);
        }
        else if (part instanceof Outsourced) {
            Outsourced outsourcedPart = (Outsourced)part;
            modPartMachOrCompTxtField.setText(outsourcedPart.getCompanyName());
            radioLabelModifyPart.setText("Company Name");
            modPartOutsourcedRBtn.setSelected(true);
        }
    }

    /**
     * <p>
     * Only 1 radio button can be selected at a time, default selection is Outsourced.
     * </p>
     * ID text field is disabled so it cannot be changed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        inHouseOrOutsourcedModifyPart = new ToggleGroup();
        this.modPartInHouseRBtn.setToggleGroup(inHouseOrOutsourcedModifyPart);
        this.modPartOutsourcedRBtn.setToggleGroup(inHouseOrOutsourcedModifyPart);
        this.modPartOutsourcedRBtn.setSelected(true);
        isOutsourced = true;

        modPartIdTxtField.setDisable(true);
    }
}

