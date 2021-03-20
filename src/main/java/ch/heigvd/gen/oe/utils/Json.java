package ch.heigvd.gen.oe.utils;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Json {

    /**
     * Read a json file and return an instance of the class defined
     * @param filename name of json file
     * @param type type of class where to retrieve json data
     * @param <T> class where to retrieve data
     * @return an instance of the class defined or null if an I/O error occurred
     */
    public <T> T read(String filename, Class<T> type){

        // try-with-ressources
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename), StandardCharsets.UTF_8))) {

            Gson gson = new Gson();
            return gson.fromJson(in, type);

        } catch (IOException e) {

            e.printStackTrace();
            return null;

        }

    }

}
