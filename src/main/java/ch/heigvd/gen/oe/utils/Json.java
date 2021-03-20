package ch.heigvd.gen.oe.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Json {

    /**
     * Open, read and parse a json file and return an instance of the class defined
     *
     * @param filename name of json file
     * @param type     type of class to return
     * @param <T>      class where to retrieve data
     * @return an instance of the class defined or null if an I/O error occurred
     */
    public <T> T parse(String filename, Class<T> type) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename), StandardCharsets.UTF_8))) {

            Gson gson = new Gson();
            return gson.fromJson(in, type);

        } catch (IOException e) { /* File could not be opened or read */
            e.printStackTrace();
            return null;
        }

    }

    public void create(Object src, String fileName){

        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(fileName + ".json"), StandardCharsets.UTF_8)){

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(src, writer);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

}
