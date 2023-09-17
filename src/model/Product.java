package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for Products
 */
public class Product {

    /**
     * Observable List used to display a product's parts
     */
    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Fields that make up a product
     */
    private int productId;
    private String productName;
    private double productPrice;
    private int productStock;
    private int productMin;
    private int productMax;

    /**
     * Detailed constructor for Products
     *
     * @param productId for ID of the product
     * @param productName for name of the product
     * @param productStock for stock/inventory of the product
     * @param productMax for maximum amount of inventory of the product
     * @param productMin for minimum amount of inventory of the product
     * @param  productPrice for price of the product
     */
    public Product(int productId, String productName, int productStock, int productMax, int productMin, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productMin = productMin;
        this.productMax = productMax;
    }

    /**
     * Default constructor used for the add product and modify product save
     */
    public Product(){}

    /**
     * Gets the product ID
     * @return returns the product ID
     */
    public int getProductId() {
        return productId;
    }

    /**
     * Sets the product ID
     * @param productId the product ID to set
     */
    public void setProductId(int productId) {this.productId = productId;
    }

    /**
     * Gets the product name
     * @return returns the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the product name
     * @param productName the product name to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets the product price
     * @return returns the product price
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Sets the product price
     * @param productPrice the product price to set
     */
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    /**
     * Gets the product stock
     * @return returns the product stock
     */
    public int getProductStock() {
        return productStock;
    }

    /**
     * Sets the product stock
     * @param productStock the product stock to set
     */
    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    /**
     * Gets the product minimum amount
     * @return returns the product minimum amount
     */
    public int getProductMin() {
        return productMin;
    }

    /**
     * Sets the product minimum amount
     * @param productMin the product minimum amount to set
     */
    public void setProductMin(int productMin) {
        this.productMin = productMin;
    }

    /**
     * Gets the product maximum amount
     * @return returns the product maximum amount
     */
    public int getProductMax() {
        return productMax;
    }

    /**
     * Sets the product maximum amount
     * @param productMax the product maximum amount to set
     */
    public void setProductMax(int productMax) {
        this.productMax = productMax;
    }

    /**
     * Gets an Observable List of all parts associated with product
     * @return returns a list of all parts associated with the product
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedParts;
    }

    /**
     * Adds a part to the associated parts with a product
     * @param newPart the new part to add to the associated parts
     */
    public void addAssociatedPart(Part newPart) {
        this.associatedParts.add(newPart);
    }

    /**
     * Deletes a part in the associated parts list
     * @param selectedAssociatedPart the selected part in the associated parts list to be deleted
     * @return true if the selected part exists in the product's associated parts list and is deleted successfully
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }


    /**
     * <p>
     * Checks for validation of part entries in the text fields.
     * </p>
     * Creates an error message if name field is empty.
     * Creates an error message if inventory field is not greater than 0.
     * Creates an error message if price field is not greater than 0.
     * Creates an error message if maximum field is less than minimum field.
     * Creates an error message if inventory field is not between minimum and maximum values.
     * Creates an error message if minimum field is not greater than 0.
     * Creates an error message if maximum field is not greater than 0.
     * Creates an error message if maximum and minimum field numbers are the same.
     * @return errorMessage returns the error message that applies to the invalid entry.
     */
    public static String isPartValid(String partName, int partStock, int partMax, int partMin, double partPrice, String errorMessage) {
        if (partName.isEmpty()) {
            errorMessage = errorMessage + "\nName field is required";
        }
        if (partStock <= 0) {
            errorMessage = errorMessage + "\nInventory must be greater than 0";
        }
        if (partPrice <= 0) {
            errorMessage = errorMessage + "\nPrice must be greater than 0";
        }
        if (partMax < partMin) {
            errorMessage = errorMessage + "\nMaximum inventory must be greater than minimum inventory";
        }
        if (partStock < partMin || partStock > partMax) {
            errorMessage = errorMessage + "\nInventory must be between minimum and maximum values";
        }
        if (partMin <= 0) {
            errorMessage = errorMessage + "\nMinimum inventory must be greater than 0";
        }
        if (partMax <= 0) {
            errorMessage = errorMessage + "\nMaximum inventory must be greater than 0";
        }
        if (partMax == partMin) {
            errorMessage = errorMessage + "\nMaximum inventory and minimum inventory numbers cannot be the same";
        }
        return errorMessage;
    }

    /**
     * <p>
     * Checks for validation of part entries in the text fields.
     * </p>
     * Creates an error message if name field is empty.
     * Creates an error message if inventory field is not greater than 0.
     * Creates an error message if price field is not greater than 0.
     * Creates an error message if maximum field is less than minimum field.
     * Creates an error message if inventory field is not between minimum and maximum values.
     * Creates an error message if minimum field is not greater than 0.
     * Creates an error message if maximum field is not greater than 0.
     * Creates an error message if maximum and minimum field numbers are the same.
     * Creates an error message if sum of the price of all parts is greater than product price.
     * @return errorMessage returns the error message that applies to the invalid entry.
     */

    public static String isProductValid (String productName, int productStock, int productMax, int productMin, double productPrice, String errorMessage) {

        if (productName.isEmpty()) {
            errorMessage = errorMessage + "\nName field is required";
        }
        if (productStock <= 0) {
            errorMessage = errorMessage + "\nInventory must be greater than 0";
        }
        if (productPrice <= 0) {
            errorMessage = errorMessage + "\nPrice must be greater than 0";
        }
        if (productMax < productMin) {
            errorMessage = errorMessage + "\nMaximum inventory must be greater than minimum inventory";
        }
        if (productStock < productMin || productStock > productMax) {
            errorMessage = errorMessage + "\nInventory must be between minimum and maximum values";
        }
        if (productMin <= 0) {
            errorMessage = errorMessage + "\nMinimum inventory must be greater than 0";
        }
        if (productMax <= 0) {
            errorMessage = errorMessage + "\nMaximum inventory must be greater than 0";
        }
        if (productMax == productMin) {
            errorMessage = errorMessage + "\nMaximum inventory and minimum inventory numbers cannot be the same";
        }
        return errorMessage;
    }
}
