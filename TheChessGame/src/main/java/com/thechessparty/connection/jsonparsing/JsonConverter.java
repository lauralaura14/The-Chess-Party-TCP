package com.thechessparty.connection.jsonparsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonConverter {

    // class variables
    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    //------------------ public methods --------------------------

    /**
     * Builder method that creates an ObjectMapper object with configurations
     *
     * @return ObjectMapper that has personalized configurations
     */
    private static ObjectMapper getDefaultObjectMapper() {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.registerModule(new JavaTimeModule());
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return defaultObjectMapper;
    }

    /**
     * Takes in a JSON String and parses the data tree. The tree will represent the objects and value pairs
     * found in the JSON String.
     *
     * @param src a JSON String object
     * @return a JsonNode
     * @throws IOException if the values in the String are invalid
     */
    public static JsonNode parse(String src) throws IOException {
        return objectMapper.readTree(src);
    }

    /**
     * Takes a JsonNode object and a specified class type and converts the values of the JSON object into a POJO
     *
     * @param node      a JsonNode node that is storing the JSON type value pairs
     * @param classType the Class type that the JSON is to be converted to
     * @param <A>       generic datatype that will match the classType
     * @return the newly created POJO
     * @throws JsonProcessingException if the values in the String are invalid
     */
    public static <A> A fromJson(JsonNode node, Class<A> classType) throws JsonProcessingException {
        return objectMapper.treeToValue(node, classType);
    }

    /**
     * Takes in a Java object and converts the data into a JsonNode
     *
     * @param a a Java object that is to be converted
     * @return the newly created JsonNode object with the values of 'a' Object
     */
    public static JsonNode toJson(Object a) {
        return objectMapper.valueToTree(a);
    }

    /**
     * Creates a JSON format String from the values given in a JsonNode. String will be produced in a single line
     * unformatted.
     *
     * @param node JsonNode that stores the data to be converted
     * @return String of the data
     * @throws JsonProcessingException may throw exception if the JsonNode is invalid
     */
    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateSting(node, false);
    }

    /**
     * Creates a JSON format String from the values given in a JsonNode. String will be produced in a tree structure
     * formatted.
     *
     * @param node JsonNode that stores the data to be converted
     * @return String of the data
     * @throws JsonProcessingException May throw exception if the JsonNode is invalid
     */
    public static String formatPrint(JsonNode node) throws JsonProcessingException {
        return generateSting(node, true);
    }

    //------------ private helper methods -------------------

    /**
     * Method does the logical conversion of the JsonNode into a String. May produce either an unformatted JSON String
     * or a tree structure String dependant on the tree flag
     *
     * @param node JsonNode that stores the data to be converted
     * @param tree boolean flag that defines the String output format
     * @return String of the JSON data
     * @throws JsonProcessingException May throw exception if the JsonNode is invalid
     */
    private static String generateSting(JsonNode node, boolean tree) throws JsonProcessingException {
        ObjectWriter objectWriter = objectMapper.writer();
        if (tree) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(node);
    }
}
