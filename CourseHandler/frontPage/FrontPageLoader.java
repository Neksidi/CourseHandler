package CourseHandler.frontPage;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;
import static javafx.application.Application.launch;


public class FrontPageLoader extends Application {
    /**
     * avaa etusivun fxml tiedoston
     *
     * @param stage avattava ikkuna
     * @throws Exception poikkeus
     */    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("frontPage.fxml")); // avataan etusivun fxml tiedosto
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setHeight(700);
        stage.setWidth(1000);
        stage.show();
    }

    /**
     * Käynnistys.
     *
     * @param args argumentit
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}