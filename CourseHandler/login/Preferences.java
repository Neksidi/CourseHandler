package CourseHandler.login;

import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

public final class Preferences {

    public static final String CONFIG_FILE = "config.txt"; //tekstitedosto tunnuksia varten

    public String username; //käyttäjänimi
    String password; //salasana

    /**
     * Alustus.
     */
    public Preferences() {
        username = "admin"; //aseteteaan käyttäjänimi
        setPassword("root"); //asetetaan salasana
    }

    /**
     * Hakee käyttäjänimen.
     *
     * @return käyttäjänimi
     */
    public String getUsername() {
        return username;
    }

    /**
     * Asettaa käyttäjänimen.
     *
     * @param username käyttäjänimi
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Hakee salasanan.
     *
     * @return salasana
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asettaa salasanan.
     *
     * @param password salasana
     */
    public void setPassword(String password) {
        this.password = DigestUtils.shaHex(password);
    }

    /**
     * Kirjoittaa tiedostoon gsonia hyödyntäen.
     */
    public static void initConfig() {
        Writer writer = null; //alustus null:iksi
        try {
            Preferences preference = new Preferences();
            Gson gson = new Gson(); //luodaan gson olio
            writer = new FileWriter(CONFIG_FILE); //uusi kirjoittaja
            gson.toJson(preference, writer); //tyypin muunto Jsoniksi
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close(); //suljetaan
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Hakumetodi tiedoille.
     *
     * @return preferences
     */
    public static Preferences getPreferences() {
        Preferences preferences = new Preferences();
        // preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class);
        initConfig(); //kutsutaan edellistä metodia
        return preferences;
    }

    //Metodi mahdollisesti joskus toteutettavaa rekisteröintiä varten.
/*    
    public static void writePreferenceToFile(Preferences preference) {
        Writer writer = null;
        try {
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);

        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
     */
}
