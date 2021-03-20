package ch.heigvd.gen.oe.utils;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class jsonTest {

    static private class TestClass {
        private int integer;
        private double decimal;
        private String string;
        private String[] strings;
        private ArrayList<innerClass> arrayInnerClass;

        private class innerClass {
            private int a, b;
        }
    }


    @Test
    public void jsonParserShouldOpenReadParseJsonFile() {

        final String FILENAME = "jsonTest.json";
        final int INT_TO_TEST = 42;
        final String STRING_TO_TEST = "This is a simple test to read json";
        final String JSON_STRING = "{\"integer\":" + INT_TO_TEST + ", \"string\":\"" + STRING_TO_TEST + "\"}";

        TestClass test = null;

        /* Create json test file*/
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(FILENAME), StandardCharsets.UTF_8)) {

            writer.write(JSON_STRING);

        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonParser parser = new jsonParser();
        test = parser.read(FILENAME, TestClass.class);

        /* Delete json file */
        try {
            Files.deleteIfExists(Path.of(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(INT_TO_TEST, test.integer);
        assertEquals(STRING_TO_TEST, test.string);
    }


    @Test
    public void jsonParserShouldWorkWithArrays() {

        final String FILENAME = "jsonTest.json";
        final String[] ARRAY_STRING_TO_TEST = {"Test", "array"};
        final String JSON_STRING = "{\"strings\": [\"" + ARRAY_STRING_TO_TEST[0] + "\",\"" + ARRAY_STRING_TO_TEST[1]
                + "\"],\n\"arrayInnerClass\": [{\"a\":4, \"b\":2}]}";

        TestClass test = null;

        /* Create json test file*/
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(FILENAME), StandardCharsets.UTF_8)) {

            writer.write(JSON_STRING);

        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonParser parser = new jsonParser();
        test = parser.read(FILENAME, TestClass.class);

        /* Delete json file */
        try {
            Files.deleteIfExists(Path.of(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertArrayEquals(ARRAY_STRING_TO_TEST, test.strings);
        assertEquals(4, test.arrayInnerClass.get(0).a);
        assertEquals(2, test.arrayInnerClass.get(0).b);
    }

    @Test
    public void jsonParserShouldWorkWithMultipleObjects(){

        final String FILENAME = "jsonTest.json";
        final String JSON_STRING = "[{\"decimal\": 4.2}, {\"decimal\": 4.2}, {\"decimal\": 4.2}]";

        TestClass[] tests = null;

        /* Create json test file*/
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(FILENAME), StandardCharsets.UTF_8)) {

            writer.write(JSON_STRING);

        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonParser parser = new jsonParser();
        tests = parser.read(FILENAME, TestClass[].class);

        /* Delete json file */
        try {
            Files.deleteIfExists(Path.of(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(3, tests.length);
    }


}