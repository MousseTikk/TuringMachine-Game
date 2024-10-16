package g56583.esi.atl.view.fx;

import g56583.esi.atl.controller.fx.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainApp is the entry point of the JavaFX application. It sets up the primary stage
 * and initializes the main controller and views.
 */
public class MainApp extends Application {

    /**
     * The main entry point for all JavaFX applications. The start method is called
     * after the system is ready for the application to begin running.
     *
     * @param primaryStage The primary stage for this application, onto which
     *                     the application scene can be set.
     */
    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController(primaryStage);
        ProblemSelectorView problemSelectorView = new ProblemSelectorView(controller);
        Scene scene = new Scene(problemSelectorView);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("TuringMachine-G56583-COLIN DUSART");
        controller.setScene(scene);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves as fallback in case the application is launched by other means,
     * e.g., from an IDE with limited FX support.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
