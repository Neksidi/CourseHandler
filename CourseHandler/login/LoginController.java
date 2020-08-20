/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseHandler.login;

import CourseHandler.database.DatabaseHandler;
import CourseHandler.frontPage.FrontPageController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Ville
 */
public class LoginController implements Initializable {

    DatabaseHandler databaseHandler = new DatabaseHandler();
    @FXML
    private JFXTextField nimi;
    @FXML
    private JFXButton kirjauduNappi;

    Preferences preference;
    @FXML
    private JFXPasswordField salasana;
    @FXML
    private JFXButton frontPageButton;

    /**
     * Alustaa luokan.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        preference = Preferences.getPreferences(); // haetaan tietoja toisesta luokasta
    }

    /**
     * Tarkistaa annetun käyttäjätunnuksen ja kirjautuu sisään sekä päivittää
     * kirjautumistiedon
     *
     * @param event tpahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void kirjaudu(ActionEvent event) throws IOException {

        String uname = nimi.getText();
        String pwd = DigestUtils.shaHex(salasana.getText()); // salasana suojattuna

        if (uname.equals(preference.getUsername()) && pwd.equals(preference.getPassword())) {

            String qu = "UPDATE LOGIN SET log = true WHERE log = false"; //päivitetään login taulun arvoa kirjautuneeksi
            System.out.println(qu);
            if (databaseHandler.execAction(qu)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION); //tiedonanto
                alert.setHeaderText(null);
                alert.setContentText("Kirjauduit sisään onnistuneesti");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR); //virheilmoitus
                alert.setHeaderText(null);
                alert.setContentText("Jotain meni pieleen");
                alert.showAndWait();
            }

            loadPage(); //haetaan etusivu

        } else if (!uname.equals(preference.getUsername()) || !pwd.equals(preference.getPassword())) { //jos jokin ei täsmää
            Alert alert = new Alert(Alert.AlertType.ERROR); //virheilmoitus
            alert.setHeaderText(null);
            alert.setContentText("Tunnus tai salasana virheellinen");
            alert.showAndWait();
        }

    }

    /**
     * Vie etusivulle, kun etusivu nappia painetaan.
     *
     * @param event tapahtumankäsittelijä
     */
    @FXML
    private void handleFrontPageButtonAction(ActionEvent event) throws IOException {
        loadPage();
    }

    /**
     * Lataa etusivun.
     *
     * @throws IOException poikkeus
     */
    private void loadPage() throws IOException {
        Stage page = (Stage) kirjauduNappi.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/frontPage/frontPage.fxml")); //haetaan etusivu
        Scene scene = new Scene(root);
        page.setScene(scene);
        page.show();
    }

}
