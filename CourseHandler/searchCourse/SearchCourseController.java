/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseHandler.searchCourse;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import CourseHandler.addCourse.AddCourseController;
import CourseHandler.database.DatabaseHandler;
import CourseHandler.login.Preferences;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ville
 */
public class SearchCourseController implements Initializable {

    ObservableList<Course> list = FXCollections.observableArrayList();

    Preferences preferences;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Course> tableView;
    @FXML
    private TableColumn<Course, String> tunnisteCol;
    @FXML
    private TableColumn<Course, String> nimiCol;
    @FXML
    private TableColumn<Course, String> laajuusCol;
    @FXML
    private TableColumn<Course, String> laitosCol;
    @FXML
    private JFXComboBox<String> Tiedekunta;
    @FXML
    private JFXComboBox<String> Laitos;
    @FXML
    private JFXComboBox<String> Tutkinto;
    @FXML
    private JFXComboBox<String> ajankohta;
    @FXML
    private JFXTextField Tunniste;
    @FXML
    private JFXButton Hae;

    DatabaseHandler databaseHandler;
    @FXML
    private JFXButton EtusivuNappi;
    @FXML
    private JFXButton LuontiNappi;
    @FXML
    private JFXButton HakuNappi;
    @FXML
    private VBox Info1;
    @FXML
    private VBox Info2;
    @FXML
    private JFXButton kirjautuminen;
    @FXML
    private Separator separator;

    /**
     * Alustaa luokan.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        databaseHandler = new DatabaseHandler();  //Olio tietokannan käsittelyä varten
        preferences = new Preferences();

        kirjautuminen.setText("Kirjautunut: " + preferences.getUsername()
                + "\nKlikkaa uloskirjautuaksesi");
        Tiedekunta.getItems().addAll("Luonnontieteiden ja metsätieteiden tiedekunta", //Lisätään tiedekunta -comboboxiin eri tiedekuntia
                "Yhteiskuntatieteiden ja kauppatieteiden tiedekunta");

        Laitos.getItems().addAll("Tietojenkäsittelytieteen laitos", "Kauppatieteiden laitos", //Lisätään laitos -comboboxiin eri tiedekuntiin kuuluvia laitoksia
                "Sovelletun fysiikan laitos");

        Tutkinto.getItems().addAll("Kandidaatti", "Maisteri");  //Lisätään Tutkinto -comboboxiin eri tutkinnot mihin kurssi voisi kuulua

        ajankohta.getItems().addAll("Kevät 2018", "Syksy 2018"); //Lisätään ajankohta -comboboxiin eri ajankohdat milloin kurssi pidetään

        JFXDepthManager.setDepth(Info1, 1); //luo syvennystä pakolliset hakuehdot sisältävään vboxiin
        JFXDepthManager.setDepth(Info2, 1);

        initCol();  //kutsuu kolumnit alustavaa luokkaa
    }

    /**
     * Alustaa kolumnit.
     */
    private void initCol() {
        //Annetaan nimet tablepanessa oleville kolumneille
        tunnisteCol.setCellValueFactory(new PropertyValueFactory<>("tunniste"));
        nimiCol.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        laajuusCol.setCellValueFactory(new PropertyValueFactory<>("laajuus"));
        laitosCol.setCellValueFactory(new PropertyValueFactory<>("laitos"));

    }

    /**
     * Käsittelee laitoksia tiedekunnan valinnan mukaan. Riippuen siitä, mikä
     * tiedekunta valitaan, muutetaan valittavia laitoksia laitos -checkboxissa.
     *
     * @param event tapahtumankäsittelijä
     */
    @FXML
    private void handleTiedekuntaAction(ActionEvent event) {

        if (Tiedekunta.getValue() == "Luonnontieteiden ja metsätieteiden tiedekunta") { //tutkitaan tiedekunta -boxin arvo
            Laitos.getItems().clear();
            Laitos.getItems().addAll("Tietojenkäsittelytieteen laitos", "Sovelletun fysiikan laitos");
        }
        if (Tiedekunta.getValue() == "Yhteiskuntatieteiden ja kauppatieteiden tiedekunta") {
            Laitos.getItems().clear();
            Laitos.getItems().add("Kauppatieteiden laitos");
        }
    }

    /**
     * Etusivu -näppäintä klikatessa tämä metodi ohjaa etusivulle.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void handleEtusivuAction(ActionEvent event) throws IOException {
        Stage page = (Stage) EtusivuNappi.getScene().getWindow();
        //määritetään mikä sivu avataan
        Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/frontPage/frontPage.fxml")); //haetaan etusivu
        Scene scene = new Scene(root);
        page.setScene(scene);  //asetetaan sivu näkymäksi
        page.show();
    }

    /**
     * Kurssien luonti -näppäintä klikatessa tämä metodi ohjaa kurssien luonti
     * -sivulle.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void handleLuontiAction(ActionEvent event) throws IOException {
        Stage page = (Stage) LuontiNappi.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/addCourse/addCourse.fxml")); //haetaan luontisivu
        Scene scene = new Scene(root);
        page.setScene(scene); //asetetaan sivu näkymäksi
        page.show();
    }

    /**
     * Ei tee mitään, kun kurssien haku-sivua koitetaan avata uudelleen.
     *
     * @param event tapahtumankäsittelijä
     */
    @FXML
    private void handleHakuAction(ActionEvent event) {
    }

    /**
     * Kirjaa käyttäjän ulos ja päivittää tiedon tietokantaan, sekä palauttaa
     * etusivulle.
     *
     * @param event tapahtumankäsittelijä
     * @throws IOException poikkeus
     */
    @FXML
    private void kirjaudu(ActionEvent event) throws IOException {
   
        String qu = "UPDATE LOGIN SET log = false WHERE log = true";   //muutetaan sisäänkirjautumista käsittelevä arvo trueksi
        System.out.println(qu);
      
        if (databaseHandler.execAction(qu)) {  //jos kysely onnistui niin annetaan siitä ilmoitus
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Kirjauduit ulos");
            alert.showAndWait();
        } //jos kysely ei onnistunut niin annetaan siitä ilmoitus
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Jotain meni pieleen");
            alert.showAndWait();
        }

        Stage page = (Stage) kirjautuminen.getScene().getWindow();
     
        Parent root = FXMLLoader.load(getClass().getResource("/CourseHandler/frontPage/frontPage.fxml")); //haetaan etusivu
        Scene scene = new Scene(root);     
        page.setScene(scene); //asetetaan sivu näkymäksi
        page.show();
    }

    /**
     * Hakee tietokannasta hakuehtojen mukaisen kurssin.
     *
     * @param event tapahtumankäsittelijä
     */
    @FXML
    private void handleButtonAction(ActionEvent event) {

       
        String SearchTerms = ""; //haettavat asiat määrittelevä muuttuja
     
        list.clear(); //jos tehdään uusi haku niin tyhjennetään lista. Listaa käytetään apuna tietojen viennissä TableViewiin
     
        if (!Tunniste.getText().isEmpty()) {   //jos haettava tunniste on annettu niin lisätään se SearchTerms muuttujaan
           
            if (SearchTerms != "") { //ensimmäisen lisättävän haku määritteen eteen ei laiteta AND:a
               
                SearchTerms = SearchTerms + " AND "; //lisätään AND sitä varten jos haku määritteitä on useampia
            }
            SearchTerms = SearchTerms + "Tunniste = '" + Tunniste.getText() + "'";
        }
       
        if (Laitos.getValue() != null) { //jos haettava laitos on annettu niin lisätään se SearchTerms muuttujaan
            if (SearchTerms != "") {
              
                SearchTerms = SearchTerms + " AND ";  //lisätään AND sitä varten jos haku määritteitä on useampia
            }
            SearchTerms = SearchTerms + "Laitos = '" + Laitos.getValue() + "'";
        }
             
        if (Tutkinto.getValue() != null) { //jos haettava tutkinto on annettu niin lisätään se SearchTerms muuttujaa  
            if (SearchTerms != "") {
              
                SearchTerms = SearchTerms + " AND ";  //lisätään AND sitä varten jos haku määritteitä on useampia
            }
            SearchTerms = SearchTerms + "Tutkinto = '" + Tutkinto.getValue() + "'";
        }
      
        if (ajankohta.getValue() != null) {  //jos haettava ajankohta on annettu niin lisätään se SearchTerms muuttujaa
            if (SearchTerms != "") {
              
                SearchTerms = SearchTerms + " AND ";  //lisätään AND sitä varten jos haku määritteitä on useampia
            }
            SearchTerms = SearchTerms + "ajankohta = '" + ajankohta.getValue() + "'";
        }

     
        String qu = "SELECT * FROM COURSE WHERE " + SearchTerms; //kysely haettavia kursseja varten
        ResultSet rs = databaseHandler.execQuery(qu);
        try {
        
            while (rs.next()) {  //haetaan tauluun tableViewiin vietävät tiedot muuttujiin ja lopulta listaan
                String tunniste = rs.getString("Tunniste");
                String nimi = rs.getString("KurssinNimi");
                String laajuus = rs.getString("Laajuus");
                String laitos = rs.getString("Laitos");

                list.add(new Course(tunniste, nimi, laajuus, laitos));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddCourseController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        tableView.getItems().setAll(list); //viedään listassa olevat tiedot tauluun
    }

    /**
     * Sisäluokka kurssille.
     */
    public static class Course {

        private final SimpleStringProperty tunniste;
        private final SimpleStringProperty nimi;
        private final SimpleStringProperty laajuus;
        private final SimpleStringProperty laitos;

        /**
         * Alustus.
         *
         * @param tunniste kurssin tunniste
         * @param nimi kurssin nimi
         * @param laajuus kurssin laajuus opintopisteinä
         * @param laitos kurssin pitävä laitos
         */
        Course(String tunniste, String nimi, String laajuus, String laitos) {
            this.tunniste = new SimpleStringProperty(tunniste);
            this.nimi = new SimpleStringProperty(nimi);
            this.laajuus = new SimpleStringProperty(laajuus);
            this.laitos = new SimpleStringProperty(laitos);
        }

        /**
         * Hakee tunnisteen.
         *
         * @return tunniste
         */
        public String getTunniste() {
            return tunniste.get();
        }

        /**
         * Hakee nimen.
         *
         * @return nimi
         */
        public String getNimi() {
            return nimi.get();
        }

        /**
         * Hakee laajuuden.
         *
         * @return laajuus
         */
        public String getLaajuus() {
            return laajuus.get();
        }

        /**
         * Hakee laitoksen.
         *
         * @return laitos
         */
        public String getLaitos() {
            return laitos.get();
        }

    }

}
