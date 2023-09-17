package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import static model.Inventory.*;

/**
 * Controller for Add Product
 */
public class AddProductController implements Initializable {

    private String exceptionMessage = "";

    private static int productId = 4;

    private final ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();

    private final Inventory inventory = null;

    private ObservableList<Part> displayParts = FXCollections.observableArrayList();

    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TableView<Part> availablePartsTableAdd;
    @FXML
    private TextField addProductIdTxtField;

    @FXML
    private TextField addProductInvTxtField;

    @FXML
    private TextField addProductMaxTxtField;

    @FXML
    private TextField addProductMinTxtField;

    @FXML
    private TextField addProductNameTxtField;

    @FXML
    private TextField addProductPriceTxtField;

    @FXML
    private TableColumn<Part, Integer> associatedPartIdColumn;

    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryColumn;

    @FXML
    private TableColumn<Part, String> associatedPartNameColumn;

    @FXML
    private TableColumn<Part, Double> associatedPartPriceColumn;

    @FXML
    private TableColumn<Part, Integer> availablePartIdColumn;

    @FXML
    private TableColumn<Part, Integer> availablePartInventoryColumn;

    @FXML
    private TableColumn<Part, String> availablePartNameColumn;

    @FXML
    private TableColumn<Part, Double> availablePartPriceColumn;

    @FXML
    private TextField searchAvailablePartsTxtField;


    /**
     * Adds a selected available part to the new product parts table (associated parts)
     * @param event action event when Add button is clicked
     */
    @FXML
    public void onActionAddToAssociatedParts(ActionEvent event) throws IOException {

        Part selectedPart = availablePartsTableAdd.getSelectionModel().getSelectedItem();
        tempAssociatedParts.add(selectedPart);
        associatedPartsTable.setItems(tempAssociatedParts);
    }

    /**
     * Confirms Add Product cancel and user is brought back to the Main Screen
     * @param event action event when Cancel button is clicked
     */
    @FXML
    public void onActionAddProductCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Add Product");
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
     * <p>
     * Saves the product that is created.
     * </p>
     * <p>
     * Any invalid entries in text fields found stops the save and displays error message with list of exceptions.
     * </p>
     * productId++ auto generates/increments a new product ID.
     * @param event action event when save button is clicked and takes user back to Main Screen
     */
    @FXML
    public void onActionAddProductSave(ActionEvent event) throws IOException {

        String productName = addProductNameTxtField.getText();
        String productStock = addProductInvTxtField.getText();
        String productPrice = addProductPriceTxtField.getText();
        String productMin = addProductMinTxtField.getText();
        String productMax = addProductMaxTxtField.getText();

        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productStock), Integer.parseInt(productMax), Integer.parseInt(productMin), Double.parseDouble(productPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Add Product Error");
                alert.setHeaderText("Input Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product name: " + productName);
                Product newProduct = new Product();
                newProduct.setProductId(productId);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductStock(Integer.parseInt(productStock));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                Inventory.addProduct(newProduct);

                for (Part associated : tempAssociatedParts) {
                    newProduct.addAssociatedPart(associated);
                }

                productId++;
                Parent addProductSaveParent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(addProductSaveParent);
                Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(("Add Product Error"));
            alert.setHeaderText("Input Error");
            alert.setContentText("Form contains blank or invalid fields");
            alert.showAndWait();
        }
    }

    /**
     * Searches available parts by name/partial name or ID and filters parts that match.
     *
     * @param event action event when part name or ID is entered into search field.
     * When search field is set back to empty and enter/return is clicked on keyboard, the table repopulates with all available parts.
     * Generates error message if part is not found.
     */
    @FXML
    public void onActionAvailablePartsSearch(ActionEvent event) throws IOException {

        String searchPartialName;
        int searchPartId;

        if (isInteger(searchAvailablePartsTxtField.getText())) {
            searchPartId = Integer.parseInt(searchAvailablePartsTxtField.getText());
            lookupPartId(searchPartId);
            if (lookupPartId(searchPartId) == null) {
                availablePartsTableAdd.setItems(getAllParts());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Search Error");
                alert.setHeaderText("Searched part not found");
                alert.setContentText("Please type part ID or part name to search");
                alert.showAndWait();
                return;
            }
            ObservableList<Part> partsFound = FXCollections.observableArrayList();
            partsFound.add(lookupPartId(searchPartId));
            availablePartsTableAdd.setItems(partsFound);
        }
        else {
            searchPartialName = searchAvailablePartsTxtField.getText();
            lookupPartName(searchPartialName);
            if (lookupPartName(searchPartialName).isEmpty()) {
                availablePartsTableAdd.setItems(getAllParts());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Search Error");
                alert.setHeaderText("Searched part not found");
                alert.setContentText("Please type part ID or part name to search");
                alert.showAndWait();
                return;
            }
            availablePartsTableAdd.setItems(lookupPartName(searchPartialName));
        }
    }

    /**
     * Removes an associated part from the new product and confirms before removing
     * @param event action event when Remove Associated Part button is clicked
     */
    @FXML
    public void onActionRemoveAssociatedPart(ActionEvent event) throws IOException {

        Part part = associatedPartsTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Remove Part");
        alert.setHeaderText("Removing Part");
        alert.setContentText("Are you sure you want to remove this part from the product?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            tempAssociatedParts.remove(part);
        }
    }

    /**
     * Verifies if search input is an integer or string.
     * @param input user input in search field
     * @return return true if user input is an integer, false if it is not.
     */
    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * <p>
     * Initializes table views and loads both table data (available parts and associated parts)
     * </p>
     * <p>
     * Available Part table columns
     * </p>
     * <p>
     * New Product Parts (Associated Parts) table columns
     * </p>
     * Auto Gen - Disabled used to disable ID field and auto generate ID
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        displayParts = getAllParts();

        availablePartsTableAdd.setItems(displayParts);
        availablePartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        availablePartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        availablePartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availablePartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(tempAssociatedParts);
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductIdTxtField.setText("Auto Gen - Disabled");
        addProductIdTxtField.setDisable(true);
    }
}
