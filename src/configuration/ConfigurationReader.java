/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hung.LM2
 */
public class ConfigurationReader {

    public static Configuration readConfig() {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("config.json"));
            return gson.fromJson(reader, Configuration.class);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigurationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
