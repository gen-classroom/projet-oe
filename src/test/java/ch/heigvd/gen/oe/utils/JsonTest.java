package ch.heigvd.gen.oe.utils;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests of methodes parse() and create() from class Json
 *
 * @author Do Vale Lopes Miguel
 */
public class JsonTest {

    static private class TestClass {
        private int integer;
        private double decimal;
        private String string;
        private String[] strings;
        private ArrayList<innerClass> arrayInnerClass;

        TestClass() {
            integer = 42;
            decimal = 4.2;
            string = "Classic TestClass construct";
            strings = new String[]{"Classic", "TestClass", "construct"};
            arrayInnerClass = new ArrayList<>();
            arrayInnerClass.add(new innerClass());
        }

        private class innerClass {
            innerClass() {
                a = 4;
                b = 2;
            }

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

        Json parser = new Json();
        test = parser.parse(FILENAME, TestClass.class);

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

        Json parser = new Json();
        test = parser.parse(FILENAME, TestClass.class);

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
    public void jsonParserShouldWorkWithMultipleObjects() {

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

        Json parser = new Json();
        tests = parser.parse(FILENAME, TestClass[].class);

        /* Delete json file */
        try {
            Files.deleteIfExists(Path.of(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(3, tests.length);
    }


    @Test
    public void jsonCreateShouldCreateAJsonFile() {
        final String EXPECTED_JSON = "{\n" +
                "  \"integer\": 42,\n" +
                "  \"decimal\": 4.2,\n" +
                "  \"string\": \"Classic TestClass construct\",\n" +
                "  \"strings\": [\n" +
                "    \"Classic\",\n" +
                "    \"TestClass\",\n" +
                "    \"construct\"\n" +
                "  ],\n" +
                "  \"arrayInnerClass\": [\n" +
                "    {\n" +
                "      \"a\": 4,\n" +
                "      \"b\": 2\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        final String FILENAME = "jsonTest";
        final int BUFFER_SIZE = 300;

        Json json = new Json();
        json.create(new TestClass(), FILENAME);

        int nbChars = 0;
        char[] buffer = new char[BUFFER_SIZE];
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(FILENAME + ".json"), StandardCharsets.UTF_8))) {
            while (nbChars != -1) {
                nbChars = in.read(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Delete json file */
        try {
            Files.deleteIfExists(Path.of(FILENAME + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(EXPECTED_JSON, new String(buffer).trim());
    }


    @Test
    public void jsonCreateShoulWorkWithArrayObjects() {
        final String EXPECTED_JSON = "[\n" +
                "  {\n" +
                "    \"integer\": 42,\n" +
                "    \"decimal\": 4.2,\n" +
                "    \"string\": \"Classic TestClass construct\",\n" +
                "    \"strings\": [\n" +
                "      \"Classic\",\n" +
                "      \"TestClass\",\n" +
                "      \"construct\"\n" +
                "    ],\n" +
                "    \"arrayInnerClass\": [\n" +
                "      {\n" +
                "        \"a\": 4,\n" +
                "        \"b\": 2\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    \"integer\": 42,\n" +
                "    \"decimal\": 4.2,\n" +
                "    \"string\": \"Classic TestClass construct\",\n" +
                "    \"strings\": [\n" +
                "      \"Classic\",\n" +
                "      \"TestClass\",\n" +
                "      \"construct\"\n" +
                "    ],\n" +
                "    \"arrayInnerClass\": [\n" +
                "      {\n" +
                "        \"a\": 4,\n" +
                "        \"b\": 2\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]";

        final String FILENAME = "jsonTest";
        final int BUFFER_SIZE = 3000;

        Json json = new Json();
        json.create(new TestClass[]{new TestClass(), new TestClass()}, FILENAME);

        int nbChars = 0;
        char[] buffer = new char[BUFFER_SIZE];
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(FILENAME + ".json"), StandardCharsets.UTF_8))) {
            while (nbChars != -1) {
                nbChars = in.read(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Delete json file */
        try {
            Files.deleteIfExists(Path.of(FILENAME + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(EXPECTED_JSON, new String(buffer).trim());
    }

}