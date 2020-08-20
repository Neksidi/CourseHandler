/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseHandler.frontPage;

import CourseHandler.database.DatabaseHandler;
import CourseHandler.login.Preferences;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ville
 */
public class FrontPageController implements Initializable {

    Preferences preferences = new Preferences();

    DatabaseHandler databaseHandler;
    @FXML
    private JFXButton EtusivuNappi;
    @FXML
    private JFXButton LuontiNappi;
    @FXML
    private JFXButton HakuNappi;
    @FXML
    private VBox INFO;
    @FXML
    public JFXButton kirjautuminen;
/**
 * asettaa kirjautumisnapille tekstin
 * @param kirjautuminen kirjautumisteksti
 */
    public void kirjauduSetText(String kirjautuminen) {
        this.kirjautuminen.setText(kirjautuminen);
    }
/**
 * hakee kirjautumisnapin tekstin
 * @return teksti
 */
    public String kirjauduGetText() {
        return kirjautuminen.getText();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = new DatabaseHandler();
        JFXDepthManager.setDepth(INFO, 1);

        String qu = "SELECT log FROM LOGIN";  // haetaan kirjautumistieto
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                boolean login = rs.getBoolean("log");
                if (login == true) {
                    kirjauduSetText("Kirjautunut: " + preferences.getUsername() // asetetaan napille käyttäjänimi
                            + "\nKlikkaa uloskirjautuaksesi");
                }
                if (login == false) {
                    kirjauduSetText("Kirjaudu");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FrontPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ei tee mitään, kun Etusivua koitetaan avata uudelleen.
     *
     * @param event tapahtumankäsittelijä
     */
    @FXML
    private void handleEtusivuAction(ActionEvent event) {
    }

    /**
     * Avaa luontisivun.
     *
     * @param event tapahtumankäsittelijä
     * @throws Exception poikkeus
     */
    @FXML
    private void handleLuontiAction(ActionEvent event) throws Exception {

        if (kirjautuminen.getText().equals("Kirjaudu")) {
            Alert alert = new Alert(Alert.AlertType.ERROR); //virheilmoitus
            alert.setHeaderText(null);
            alert.setContentText("Kirjaudu sisään!");
            alert.showAndWait();
        } else {
            Stage page = (Stage) LuontiNappi.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/addCourse/addCourse.fxml")); //viedään luontisivulle
            Scene scene = new Scene(root);
            page.setScene(scene);
            page.show();
        }

    }

    /**
     * Avaa kurssien haku-sivun.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void handleHakuAction(ActionEvent event) throws IOException, Exception {

        if (kirjautuminen.getText().equals("Kirjaudu")) {
            Alert alert = new Alert(Alert.AlertType.ERROR); // virheilmoitus
            alert.setHeaderText(null);
            alert.setContentText("Kirjaudu sisään!");
            alert.showAndWait();
        } else {

            Stage page = (Stage) HakuNappi.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/searchCourse/searchCourse.fxml")); //viedään hakusivulle
            Scene scene = new Scene(root);
            page.setScene(scene);
            page.show();
        }
    }

    /**
     * Päivittää uloskirjautumistiedon tietokantaan, jos käyttäjä on kirjautunut
     * sisään. Muulloin avaa kirjautumissivun.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void kirjaudu(ActionEvent event) throws IOException {
        if (!"Kirjaudu".equals(kirjauduGetText())) { //jos napissa lukee käyttjänimi

            kirjauduSetText("Kirjaudu");
            String qu = "UPDATE LOGIN SET log = false WHERE log = true"; //päivitetään login taulukon arvo uloskirjautuneeksi
            System.out.println(qu);
            if (databaseHandler.execAction(qu)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION); //tiedonanto
                alert.setHeaderText(null);
                alert.setContentText("Kirjauduit ulos");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR); //virheilmoitus
                alert.setHeaderText(null);
                alert.setContentText("Jotain meni pieleen");
                alert.showAndWait();
            }
            //kirjaudu ulos
        } else {
            Stage page = (Stage) kirjautuminen.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/login/login.fxml")); //viedään kirjautumissivulle
            Scene scene = new Scene(root);
            page.setScene(scene);
            page.show();
        }
    }

}
