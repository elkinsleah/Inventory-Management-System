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
import model.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;
import static model.Inventory.addProduct;

/**
 * Controller for Main Screen
 */
public class MainScreenController implements Initializable {

    @FXML
    private TableView<Part> tableViewPart;
    @FXML

    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    @FXML
    private TableView<Product> tableViewProduct;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    @FXML
    private TextField searchPartTxtField;
    @FXML
    private TextField searchProductTxtField;

    private static Product modifyThisProduct;

    private static Inventory inventory = null;
    static boolean addTestData = true;

    private ObservableList<Part> displayParts = FXCollections.observableArrayList();
    private ObservableList<Product> displayProducts = FXCollections.observableArrayList();

    /**
     * Used to assist with Modify Product, gets product to modify
     * @return returns selected product to modify
     */
    public static Product getProductToModify() {
        return modifyThisProduct;
    }

    /**
     * Used to assist with Modify Product, sets product to modify
     */
    public void setProductToModify(Product modProduct, int prIndex) {
        MainScreenController.modifyThisProduct = modProduct;
    }

    /**
     * Takes user to the Add Part screen
     * @param event action event when Add button is clicked under the Parts table
     */
    @FXML
    public void onActionAddPart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes user to the Add Product screen
     * @param event action event when Add button is clicked under the Products table
     * <p>
     * RUNTIME ERROR: Exception in thread "JavaFX Application Thread" java.lang.RuntimeException: java.lang.reflect.InvocationTargetException
     * Caused by: java.lang.NullPointerException: Location is required.
     * There was no location to the package. /view/ needs to be typed before AddProduct.fxml in order for the location to be found.
     * </p>
     */
    @FXML
    public void onActionAddProduct(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Deletes selected part in the Parts table.
     *
     * @param event action event when Delete button is clicked below Parts table.
     * Generates error message if no part is selected.
     * Generates confirmation message before deleting part.
     */
    @FXML
    public void onActionDeletePart(ActionEvent event) throws IOException {
        if (tableViewPart.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Error");
            alert.setHeaderText("No part selected");
            alert.setContentText("No part was selected to delete");
            alert.showAndWait();

        } else {
            Part partToDelete = tableViewPart.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Part");
            alert.setHeaderText("Deleting Part");
            alert.setContentText("Are you sure you want to delete " + partToDelete.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                deletePart(partToDelete);
            }
        }
    }

    /**
     * Deletes selected product in the Products table.
     *
     * @param event action event when Delete button is clicked below Products table.
     * Generates error message if no product is selected.
     * Error message if deleting product with associated parts.
     * Generates confirmation message before deleting product.
     */
    @FXML
    public void onActionDeleteProduct(ActionEvent event) throws IOException {

        Product selectedProduct = tableViewProduct.getSelectionModel().getSelectedItem();
        if (tableViewProduct.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Delete Error");
            alert.setHeaderText("No product selected");
            alert.setContentText("No product was selected to delete");
            alert.showAndWait();
        } else {
            if (selectedProduct.getAllAssociatedParts().size() < 1) {
                Product productToDelete = tableViewProduct.getSelectionModel().getSelectedItem();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Delete Product");
                alert.setHeaderText("Deleting Product");
                alert.setContentText("Are you sure you want to delete " + productToDelete.getProductName() + "?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    deleteProduct(productToDelete);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.NONE);
                alert.setTitle("Delete Error");
                alert.setHeaderText("Product has parts");
                alert.setContentText("Cannot delete product with associated parts");
                alert.showAndWait();
            }
        }
    }

    /**
     * Exits the Inventory Management System Application
     *
     * @param event action event when Exit button is clicked.
     * Generates confirmation message before exiting.
     */
    @FXML
    public void onActionExit(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Exit");
        alert.setHeaderText("Exit Application");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Takes user to Modify Part screen
     *
     * @param event action event when Modify button is clicked under the Parts table.
     * Generates error message if no part is selected.
     */
    @FXML
    public void onActionModifyPart(ActionEvent event) throws IOException {
        if (tableViewPart.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Modify Error");
            alert.setHeaderText("No part selected");
            alert.setContentText("No part was selected to modify");
            alert.showAndWait();

        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
            Parent mainScreenParent;
            mainScreenParent = loader.load();
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

            ModifyPartController controller = loader.getController();
            Part selectedPart = tableViewPart.getSelectionModel().getSelectedItem();
            int pIndex = Inventory.getAllParts().indexOf(selectedPart);
            controller.setPart(selectedPart, pIndex);
        }
    }

    /**
     * Takes user to Modify Product screen
     *
     * @param event action event when Modify button is clicked under the Products table.
     * Generates error message if no product is selected.
     */
    @FXML
    public void onActionModifyProduct(ActionEvent event) throws IOException {
        if (tableViewProduct.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.NONE);
            alert.setTitle("Modify Error");
            alert.setHeaderText("No product selected");
            alert.setContentText("No product was selected to modify");
            alert.showAndWait();

        } else {
            modifyThisProduct = tableViewProduct.getSelectionModel().getSelectedItem();
            int prIndex = Inventory.getAllProducts().indexOf(modifyThisProduct);
            setProductToModify(modifyThisProduct, prIndex);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
            Parent mainScreenParent;
            mainScreenParent = loader.load();
            Scene mainScreenScene = new Scene(mainScreenParent);
            Stage mainStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScreenScene);
            mainStage.show();

            ModifyProductController controller = loader.getController();
            Product selectedProduct = tableViewProduct.getSelectionModel().getSelectedItem();
            controller.setProduct(selectedProduct, prIndex);
        }
    }

    /**
     * Searches parts by name/partial name or ID and filters parts that match.
     *
     * @param event action event when part name or ID is entered into search field.
     * When search field is set back to empty and enter/return is clicked on keyboard, the table repopulates with all available parts.
     * Generates error message if part is not found.
     */
    @FXML
    public void onActionSearchPart(ActionEvent event) {

        String searchPartialName;
        int searchPartId;

        if (isInteger(searchPartTxtField.getText())) {
            searchPartId = Integer.parseInt(searchPartTxtField.getText());
            lookupPartId(searchPartId);
            if (lookupPartId(searchPartId) == null) {
                tableViewPart.setItems(getAllParts());
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
            tableViewPart.setItems(partsFound);
        }
        else {
            searchPartialName = searchPartTxtField.getText();
            lookupPartName(searchPartialName);
            if (lookupPartName(searchPartialName).isEmpty()) {
                tableViewPart.setItems(getAllParts());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Search Error");
                alert.setHeaderText("Searched part not found");
                alert.setContentText("Please type part ID or part name to search");
                alert.showAndWait();
                return;
            }
            tableViewPart.setItems(lookupPartName(searchPartialName));
        }
    }

    /**
     * Searches products by name/partial name or ID and filters products that match.
     *
     * @param event action event when product name or ID is entered into search field.
     * When search field is set back to empty and enter/return is clicked on keyboard, the table repopulates with all available products.
     * Generates error message if product is not found.
     */
    @FXML
    public void onActionSearchProduct(ActionEvent event) {

        String searchPartialName;
        int searchProductId;

        if (isInteger(searchProductTxtField.getText())) {
            searchProductId = Integer.parseInt(searchProductTxtField.getText());
            lookupProductId(searchProductId);
            if (lookupProductId(searchProductId) == null) {
                tableViewProduct.setItems(getAllProducts());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Search Error");
                alert.setHeaderText("Searched product not found");
                alert.setContentText("Please type product ID or product name to search");
                alert.showAndWait();
                return;
            }
            ObservableList<Product> productsFound = FXCollections.observableArrayList();
            productsFound.add(lookupProductId(searchProductId));
            tableViewProduct.setItems(productsFound);
        }
        else {
            searchPartialName = searchProductTxtField.getText();
            lookupProductName(searchPartialName);
            if (lookupProductName(searchPartialName).isEmpty()) {
                tableViewProduct.setItems(getAllProducts());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initModality(Modality.NONE);
                alert.setTitle("Search Error");
                alert.setHeaderText("Searched product not found");
                alert.setContentText("Please type product ID or product name to search");
                alert.showAndWait();
                return;
            }
            tableViewProduct.setItems(lookupProductName(searchPartialName));
        }
    }

    /**
     * Verifies if search input is a string or integer.
     * @param id user input in search field
     * @return return true if user input is an integer, false if it is not.
     */
    public boolean isInteger(String id) {
        try {
            Integer.parseInt(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Initializes table views and loads Parts and Products table data on Main Screen.
     * Initial inventory of Parts and Products generated on first call.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (addTestData)
        {testData();}

        displayParts = getAllParts();
        displayProducts = getAllProducts();

        tableViewPart.setItems(displayParts);
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableViewProduct.setItems(displayProducts);
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
    }

    /**
     * Generates initial Inventory of Parts and Products.
     * Adds associated parts to products.
     */
    public void testData() {

        inventory = new Inventory();

        InHouse brakes = new InHouse(1,"Brakes", 12, 50, 1, 25.00, 1);
        InHouse tires = new InHouse(2, "Tires", 18, 30, 2, 40.00, 2);
        InHouse saddle = new InHouse(3, "Saddle", 10, 45, 4, 80.00, 3);

        Outsourced seatpost = new Outsourced(4, "Seat Post", 10, 50, 2, 12.00, "Dorel Industries");
        Outsourced handlebars = new Outsourced(5, "Handlebars", 20, 80, 4, 30.00, "Roth Distributing");
        Outsourced pedals = new Outsourced(6, "Pedals", 26, 42, 2, 50.00, "Schwinn");

        Product cannondale = new Product(1, "Cannondale", 20, 40, 5, 1499.99);
        Product trek = new Product(2, "Trek", 15, 45, 3, 1099.99);
        Product yeti = new Product(3, "Yeti", 28, 36, 2, 3599.99);

        cannondale.addAssociatedPart(brakes);
        cannondale.addAssociatedPart(seatpost);

        trek.addAssociatedPart(brakes);
        trek.addAssociatedPart(tires);
        trek.addAssociatedPart(saddle);

        yeti.addAssociatedPart(brakes);
        yeti.addAssociatedPart(pedals);
        yeti.addAssociatedPart(handlebars);

        addPart(brakes);
        addPart(tires);
        addPart(saddle);
        addPart(seatpost);
        addPart(handlebars);
        addPart(pedals);

        addProduct(cannondale);
        addProduct(trek);
        addProduct(yeti);

        addTestData = false;
    }
}