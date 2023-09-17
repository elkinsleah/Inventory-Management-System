package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Model for Inventory
 */
public class Inventory {

    private static ObservableList<Part> allParts;
    private static ObservableList<Product> allProducts;

    /**
     * Constructor for creating an inventory
     */
    public Inventory() {
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }

    /**
     * Gets all parts in the allParts list
     * @return returns all the parts in the list
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Adds a new part to the Inventory's parts list
     * @param newPart the new part to be added to the Inventory
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Deletes a part from the allParts list
     * @param selectedPart the part that is to be deleted from the allParts list
     * @return true if the selected part exists in Inventory and is successfully deleted
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * Updates existing part in Inventory
     * @param index the index position of the part to be updated
     * @param selectedPart the part selected to be updated
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * Searches for a part using the part name/partial name
     * @param partialName the name/partial name of the part being searched
     * @return returns the found part/parts.
     */
    public static ObservableList<Part> lookupPartName(String partialName) {
        ObservableList<Part> partsFound = FXCollections.observableArrayList();
        for (Part part : getAllParts()) {
            if (part.getName().toLowerCase().contains(partialName.toLowerCase())) {
                partsFound.add(part);
            }
        }
        return partsFound;
    }

    /**
     * Searches for a part using the part ID
     * @param searchPartId the ID of the part being searched
     * @return returns the located part. Returns null if part is not found.
     */
    public static Part lookupPartId(int searchPartId) {
        for (Part part : allParts) {
            if (part.getId() == searchPartId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Gets all products in the allProducts list
     * @return returns all the products in the list
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Adds a new product to the Inventory's products list
     * @param newProduct the new product to be added to the Inventory
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * Deletes a product from the allProducts list
     * @param selectedProduct the product that is to be deleted from the allProducts list
     * @return true if the selected product exists in Inventory and is successfully deleted
     */
    public static boolean deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
        return true;
    }

    /**
     * Updates existing product in Inventory
     * @param index the index position of the product to be updated
     * @param selectedProduct the product selected to be updated
     */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Searches for a product using the product name/partial name
     * @param partialName the name/partial name of the product being searched
     * @return returns the found product/products.
     */
    public static ObservableList<Product> lookupProductName(String partialName) {
        ObservableList<Product> productsFound = FXCollections.observableArrayList();
        for (Product product : getAllProducts()) {
            if (product.getProductName().toLowerCase().contains(partialName.toLowerCase())) {
                productsFound.add(product);
            }
        }
        return productsFound;
    }

    /**
     * Searches for a product using the product ID
     * @param searchProductId the ID of the product being searched
     * @return returns the located product. Returns null if product is not found.
     */
    public static Product lookupProductId(int searchProductId) {
        for (Product product : allProducts) {
            if (product.getProductId() == searchProductId) {
                return product;
            }
        }
        return null;
    }
}

