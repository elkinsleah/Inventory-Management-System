package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.MainScreenController.getProductToModify;
import static model.Inventory.*;

/**
 * Controller for Modify Product
 */
public class ModifyProductController implements Initializable {

    private final Inventory inventory = null;

    private String exceptionMessage = "";

    int productIndex = 0;

    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TableView<Part> availablePartsTableModify;
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
    private TextField modProductIdTxtField;

    @FXML
    private TextField modProductInvTxtField;

    @FXML
    private TextField modProductMaxTxtField;

    @FXML
    private TextField modProductMinTxtField;

    @FXML
    private TextField modProductNameTxtField;

    @FXML
    private TextField modProductPriceTxtField;

    @FXML
    private TextField searchAvailablePartsTxtField;

    private ObservableList<Part> tempAssociatedParts = FXCollections.observableArrayList();

    private ObservableList<Part> displayParts = FXCollections.observableArrayList();

    private final Product modifyThisProduct;

    /**
     * Constructor
     */
    public ModifyProductController() {
        this.modifyThisProduct = getProductToModify();
    }

    /**
     * Adds a selected available part to the associated parts table
     * @param event action event when Add button is clicked
     */
    @FXML
    public void onActionAddToAssociatedParts(ActionEvent event) throws IOException {

        Part selectedPart = availablePartsTableModify.getSelectionModel().getSelectedItem();
        tempAssociatedParts.add(selectedPart);
        associatedPartsTable.setItems(tempAssociatedParts);
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
                availablePartsTableModify.setItems(getAllParts());
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
            availablePartsTableModify.setItems(partsFound);
        }
        else {
            searchPartialName = searchAvailablePartsTxtField.getText();
            lookupPartName(searchPartialName);
            if (lookupPartName(searchPartialName).isEmpty()) {
                availablePartsTableModify.setItems(getAllParts());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Search Error");
                alert.setHeaderText("Searched part not found");
                alert.setContentText("Please type part ID or part name to search");
                alert.showAndWait();
                return;
            }
            availablePartsTableModify.setItems(lookupPartName(searchPartialName));
        }
    }

    /**
     * Confirms Modify Product cancel and user is brought back to the Main Screen
     * @param event action event when Cancel button is clicked
     */
    @FXML
    public void onActionModProductCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modify Product");
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
     * Saves the product that is modified.
     * </p>
     * <p>
     * Any invalid entries in text fields found stops the save and displays error message with list of exceptions.
     * </p>
     * productId++ auto generates/increments a new product ID.
     * @param event action event when save button is clicked and takes user back to Main Screen
     */
    @FXML
    public void onActionModProductSave(ActionEvent event) throws IOException {

        int productId = Integer.parseInt(modProductIdTxtField.getText());
        String productName = modProductNameTxtField.getText();
        String productStock = modProductInvTxtField.getText();
        String productPrice = modProductPriceTxtField.getText();
        String productMin = modProductMinTxtField.getText();
        String productMax = modProductMaxTxtField.getText();

        try {
            exceptionMessage = Product.isProductValid(productName, Integer.parseInt(productStock), Integer.parseInt(productMax), Integer.parseInt(productMin), Double.parseDouble(productPrice), exceptionMessage);
            if (exceptionMessage.length() > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Product Error");
                alert.setHeaderText("Input Error");
                alert.setContentText(exceptionMessage);
                alert.showAndWait();
                exceptionMessage = "";
            } else {
                System.out.println("Product name: " + productIndex);
                Product newProduct = new Product();
                newProduct.setProductId(productId);
                newProduct.setProductName(productName);
                newProduct.setProductPrice(Double.parseDouble(productPrice));
                newProduct.setProductStock(Integer.parseInt(productStock));
                newProduct.setProductMin(Integer.parseInt(productMin));
                newProduct.setProductMax(Integer.parseInt(productMax));
                Inventory.updateProduct(productIndex, newProduct);

                for (Part associated : tempAssociatedParts) {
                    newProduct.addAssociatedPart(associated);
                }

                Parent modifyProductSaveParent = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                Scene scene = new Scene(modifyProductSaveParent);
                Stage window = (Stage) ((Button) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(("Modify Product Error"));
            alert.setHeaderText("Input Error");
            alert.setContentText("Form contains blank or invalid fields");
            alert.showAndWait();
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

    Product product;
    /**
     * Used in Main Screen Controller to load selected row to Modify Product screen
     */
    public void setProduct(Product product, int prIndex) {
        this.product = product;
        this.productIndex = prIndex;
        modProductIdTxtField.setText(Integer.toString(product.getProductId()));
        modProductNameTxtField.setText(product.getProductName());
        modProductInvTxtField.setText(Integer.toString(product.getProductStock()));
        modProductPriceTxtField.setText(Double.toString(product.getProductPrice()));
        modProductMaxTxtField.setText(Integer.toString(product.getProductMax()));
        modProductMinTxtField.setText(Integer.toString(product.getProductMin()));
        associatedPartsTable.setItems(product.getAllAssociatedParts());
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
     * Populates the pre-saved text fields of the selected product to modify (Name, ID, Price, etc.)
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

        modProductIdTxtField.setText(Integer.toString(modifyThisProduct.getProductId()));
        modProductNameTxtField.setText(modifyThisProduct.getProductName());
        modProductInvTxtField.setText(Integer.toString(modifyThisProduct.getProductStock()));
        modProductPriceTxtField.setText(Double.toString(modifyThisProduct.getProductPrice()));
        modProductMaxTxtField.setText((Integer.toString(modifyThisProduct.getProductMax())));
        modProductMinTxtField.setText((Integer.toString(modifyThisProduct.getProductMin())));

        availablePartsTableModify.setItems(displayParts);
        availablePartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        availablePartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        availablePartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        availablePartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTable.setItems(tempAssociatedParts);
        associatedPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tempAssociatedParts = modifyThisProduct.getAllAssociatedParts();

        modProductIdTxtField.setDisable(true);
    }
}
