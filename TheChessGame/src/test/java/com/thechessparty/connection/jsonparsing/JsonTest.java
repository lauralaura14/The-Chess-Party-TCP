package com.thechessparty.connection.jsonparsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.thechessparty.connection.pojo.AuthorPOJO;
import com.thechessparty.connection.pojo.BookPOJO;
import com.thechessparty.connection.pojo.DayPOJO;
import com.thechessparty.connection.pojo.SimpleTestCaseJsonPOJO;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonTest {

    String simpleTestCaseJsonSource = "{\n" +
            "  \"title\": \"Coder From Scratch\",\n" +
            "  \"author\": \"John\"\n" +
            "}";

    String daySenario1 = "{\n" +
            "  \"date\": \"2021-04-10\",\n" +
            "  \"name\": \"hug your dog day\"\n" +
            "}\n";

    String authorBookScenario = "{\n" +
            "  \"authorName\": \"John\",\n" +
            "  \"books\": [\n" +
            "    {\n" +
            "      \"title\": \"lord of the rings\",\n" +
            "      \"inPrint\": true,\n" +
            "      \"publishDate\": \"2021-04-10\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"game of thrones\",\n" +
            "      \"inPrint\": false,\n" +
            "      \"publishDate\": \"2012-05-14\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"title\": \"Dr. Shavago\",\n" +
            "      \"inPrint\": true,\n" +
            "      \"publishDate\": \"1921-04-10\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    void parse() throws IOException {
        JsonNode node = JsonConverter.parse(simpleTestCaseJsonSource);
        assertEquals(node.get("title").asText(), "Coder From Scratch");
    }

    @Test
    void fromJson() throws IOException {
        JsonNode node = JsonConverter.parse(simpleTestCaseJsonSource);
        SimpleTestCaseJsonPOJO pojo = JsonConverter.fromJson(node, SimpleTestCaseJsonPOJO.class);

        assertEquals(pojo.getTitle(), "Coder From Scratch");
    }

    @Test
    void toJson(){
        SimpleTestCaseJsonPOJO pojo = new SimpleTestCaseJsonPOJO();
        pojo.setTitle("test 123");

        JsonNode node = JsonConverter.toJson(pojo);

        assertEquals(node.get("title").asText(), "test 123");
    }

    @Test
    void stringify() throws JsonProcessingException {
        SimpleTestCaseJsonPOJO pojo = new SimpleTestCaseJsonPOJO();
        pojo.setTitle("test 123");

        JsonNode node = JsonConverter.toJson(pojo);
        System.out.println(JsonConverter.stringify(node));
        System.out.println(JsonConverter.formatPrint(node));
    }

    @Test
    void dayTestSenario1() throws IOException {
        JsonNode node = JsonConverter.parse(daySenario1);
        DayPOJO pojo = JsonConverter.fromJson(node, DayPOJO.class);

        assertEquals("2021-04-10", pojo.getDate().toString());
    }

    @Test
    void authorBookSenario1() throws IOException {
        JsonNode node = JsonConverter.parse(authorBookScenario);
        AuthorPOJO pojo = JsonConverter.fromJson(node, AuthorPOJO.class);

        System.out.println("Author : " + pojo.getAuthorName());
        for(BookPOJO bp : pojo.getBooks()){
            System.out.println("Book : " + bp.getTitle());
            System.out.println("Is in print? : " + bp.isInPrint());
            System.out.println("Date : " +bp.getPublishDate());
        }
    }
}