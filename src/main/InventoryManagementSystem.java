package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Folder containing Javadoc files can be found in /Users/leahelkins/Documents/InventoryManagementSystem/Javadoc. The folder is named Javadoc.

// RUNTIME ERROR comment is located in the MainScreenController at onActionAddProduct

/**
 * Main class begins application.
 *
 * FUTURE ENHANCEMENT: Having the products and parts saved/stored in a database would make the application
 * more usable for companies in a real-life situation. Having a database would also make it to where
 * running the application each time would not reset all the data.
 *
 * @author Leah Elkins
 * WGU C482 Software I Performance Assessment
 */
public class InventoryManagementSystem extends Application {

    /**
     * Main Screen that is shown when application is started
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Inventory Management System");
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
