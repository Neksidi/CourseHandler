/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CourseHandler.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

import javax.swing.JOptionPane;

/**
 *
 * @author Ville
 */
public final class DatabaseHandler {

    private static DatabaseHandler handler = null;
 
    private static final String DB_URL = "jdbc:derby:database;create=true";    //määrittää mihin tietokanta luodaan
  
    private static Connection conn = null;  //luo ohjelman ja tietokannan välisen yhteyden
  
    private static Statement stmt = null;   //lauseke taulun luontia varten

    /**
     * Alustus.
     */
    public DatabaseHandler() {
        createConnection();
        setupCourseTable();
        setupLoginTable();
    }

    /**
     * Luo tietokantaan yhteyden.
     */
    void createConnection() {
        try {
            //lataa ajurin
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    /**
     * Luo tietokantaan taulun, jos sitä ei ole olemassa.
     */
    void setupCourseTable() {
        String TABLE_NAME = "COURSE";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();  //haetaan data mihin taulu on saatettu tallentaa

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);   //haetaan taulu datasta  

            if (tables.next()) {  //tutkitaan onko taulu olemassa
                System.out.println("Table " + TABLE_NAME + " is working");
            } //luodaan taulu jos se ei ole olemassa
            else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     Tunniste varchar(200) primary key,\n"
                        + "     KurssinNimi varchar(200),\n"
                        + "     Laitos varchar(200),\n"
                        + "     Tutkinto varchar(200),\n"
                        + "     Laajuus varchar(200),\n"
                        + "     ajankohta varchar(200)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " - - - setupDatabase");
        } finally {

        }
    }

    /**
     * Toinen taulu kirjautumistiedolle. Luodaan jos sitä ei ole olemassa.
     */
    public void setupLoginTable() {
        String TABLE_NAME = "LOGIN";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();  //haetaan data mihin taulu on saatettu tallentaa

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);   //haetaan taulu datasta

            if (tables.next()) { //tutkitaan onko taulu olemassa
                System.out.println("Table " + TABLE_NAME + " is working");

            } //luodaan taulu jos se ei ole olemassa
            else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     log boolean"
                        + " )");

                boolean LoginKirjautuminen = false; //alustetaan tauluun lisättävä sisäänkirjautumis muuttuja falseksi

                String qu = "INSERT INTO LOGIN VALUES ("
                        + "'" + LoginKirjautuminen + "'"
                        + ")";
                System.out.println(qu);

                execAction(qu); //tehdään kysely
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " - - - setupDatabase");
        } finally {

        }
    }

    /**
     * Haetaan taulusta tiedot.
     *
     * @param query kysely
     * @return tulos
     */
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
           
            result = stmt.executeQuery(query);  //kyselyn suoritus
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }

    /**
     * Tutkitaan onnistuiko kysely.
     *
     * @param qu kysely
     * @return totuusarvo
     */
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
           
            stmt.execute(qu);  //kyselyn suoritus
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }
}
