/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseHandler.addCourse;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import CourseHandler.database.DatabaseHandler;
import CourseHandler.login.Preferences;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ville
 */
public class AddCourseController implements Initializable {

    //@FXML
    //private JFXComboBox<String> Tiedekunta;
    Preferences preferences = new Preferences();
    @FXML
    private JFXComboBox<String> Laitos;
    @FXML
    private JFXComboBox<String> ajankohta;
    @FXML
    private JFXComboBox<String> Tutkinto;
    @FXML
    private JFXComboBox<String> Laajuus;
    @FXML
    private JFXTextField KurssinNimi;
    @FXML
    private JFXTextField Tunniste;
    @FXML
    private JFXButton LuoNappi;

    @FXML
    private HBox INFO;

    DatabaseHandler databaseHandler;
    @FXML
    private JFXButton EtusivuNappi;
    @FXML
    private JFXButton LuontiNappi;
    @FXML
    private JFXButton HakuNappi;
    @FXML
    private JFXButton login;

    /**
     * Alustaa luokan.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        databaseHandler = new DatabaseHandler();  //luodaa olio tietokannan käsittelyä varten
     
        login.setText("Kirjautunut: " + preferences.getUsername()  //Annetaan kirjautumis -näppäimelle teksti, kun luonti -sivu avataan
                + "\nKlikkaa uloskirjautuaksesi");
        //Tiedekunta.getItems().addAll("Luonnontieteiden ja metsätieteiden tiedekunta", "Yhteiskuntatieteiden ja kauppatieteiden tiedekunta");
        Laitos.getItems().addAll("Tietojenkäsittelytieteen laitos",
                "Kauppatieteiden laitos", "Sovelletun fysiikan laitos");
        Laajuus.getItems().addAll("1op", "2op", "3op");
        Tutkinto.getItems().addAll("Kandidaatti", "Maisteri");
        ajankohta.getItems().addAll("Kevät 2018", "Syksy 2018");
        JFXDepthManager.setDepth(INFO, 1);
        checkData(); //haetaan kannassa olevat kurssit heti näytille
    }

    /**
     * Hakee tiedot kentistä ja luo kurssin kurssitauluun.
     *
     * @param event tapahtumankäsittelijä
     */
    @FXML
    private void LuoKurssi(ActionEvent event) {
        String KurssiTunniste = Tunniste.getText();
        String KurssiNimi = KurssinNimi.getText();
        //String KurssiTiedekunta ="";
        String KurssiLaitos = Laitos.getValue();
        String KurssiTutkinto = Tutkinto.getValue();
        String KurssiLaajuus = Laajuus.getValue();
        String KurssiAjankohta = ajankohta.getValue();

        if (KurssiTunniste.isEmpty() || KurssiNimi.isEmpty()
                || KurssiLaitos.isEmpty() || KurssiTutkinto.isEmpty()
                || KurssiLaajuus.isEmpty() || KurssiAjankohta.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Täytä kaikki tiedot");
            alert.showAndWait();
            return;
        }
       //lisätään tauluun tiedot
        String qu = "INSERT INTO COURSE VALUES ("
                + "'" + KurssiTunniste + "',"
                + "'" + KurssiNimi + "',"
                + "'" + KurssiLaitos + "',"
                + "'" + KurssiTutkinto + "',"
                + "'" + KurssiLaajuus + "',"
                + "'" + KurssiAjankohta + "'"
                + ")";
        System.out.println(qu);
        if (databaseHandler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Onnistui");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Epäonnistui");
            alert.showAndWait();
        }
    }

    /**
     * Listaa mitä kursseja on luotu, kun kurssien luontisivu avataan.
     */
    private void checkData() {
        String qu = "SELECT KurssinNimi FROM COURSE"; //haetaan kurssit nimillä
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
            while (rs.next()) {
                String name = rs.getString("KurssinNimi");
                System.out.println(name);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddCourseController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Avaa etusivun.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void handleEtusivuAction(ActionEvent event) throws IOException {
        Stage page = (Stage) EtusivuNappi.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/frontPage/frontPage.fxml")); //haetaan etusivu
        Scene scene = new Scene(root);
        page.setScene(scene);
        page.show();
    }

    /**
     * Ei tee mitään, kun kurssien luonti-sivua koitetaan avata uudelleen.
     *
     * @param event tapahtumankäsittelijä
     */
    @FXML
    private void handleLuontiAction(ActionEvent event) {
    }

    /**
     * Avaa kurssien haku-sivun.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void handleHakuAction(ActionEvent event) throws IOException {
        Stage page = (Stage) HakuNappi.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/searchCourse/searchCourse.fxml")); // haetaan hakusivu
        Scene scene = new Scene(root);
        page.setScene(scene);
        page.show();
    }

    /**
     * Kirjaa käyttäjän ulos ja päivittää tietokannan tiedon, sekä palauttaa
     * etusivulle.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void handleLoginAction(ActionEvent event) throws IOException {
        String qu = "UPDATE LOGIN SET log = false WHERE log = true"; //päivitetään login tauluun tieto uloskirjautumisesta
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

        Stage page = (Stage) login.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/frontPage/frontPage.fxml")); //haetaan etusivu
        Scene scene = new Scene(root);
        page.setScene(scene);
        page.show();
    }

}
