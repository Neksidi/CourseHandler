package CourseHandler.addCourse;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static javafx.application.Application.launch;


public class AddCourseLoader extends Application {

    /**
     * Avaa luonti -sivun fxml tiedoston.
     *
     * @param stage avattava ikkuna
     * @throws Exception poikkeus
     */    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("addCourse.fxml")); //avataan luontisivun fxml tiedosto
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);      
        stage.show();
    }

    /**
     * käynnistys
     *
     * @param args argumentit
     */    
    public static void main(String[] args) {
        launch(args);
    }
    
}