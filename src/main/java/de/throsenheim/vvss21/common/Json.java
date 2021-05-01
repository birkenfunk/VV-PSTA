package de.throsenheim.vvss21.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/**
 * Class for json transformations
 * @version 1.0.0
 * @author Alexander Asbeck
 */
public class Json {
    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    private Json() {
    }

    /**
     * Creates a ObjectMapper
     * @return a ObjectMapper
     */
    private static ObjectMapper getDefaultObjectMapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return defaultObjectMapper;
    }

    /**
     * Transforms a {@link String} into a {@link JsonNode}
     * @param src String that should be transformed
     * @return A JsonNote that contains the information from the string
     * @throws IOException Because
     */
    public static JsonNode parse(String src) throws IOException {
        return objectMapper.readTree(src);
    }

    /**
     * Transforms a {@link JsonNode} to a class
     * @param node The JsonNote with the inforation
     * @param clazz the class that it should be transformed into
     * @param <A> Type of the Class
     * @return a object of the class clazz with the parameter of the node
     * @throws JsonProcessingException Because
     */
    public static <A> A fromJson(JsonNode node , Class<A> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }

    /**
     * Transforms a {@link Object} into a {@link JsonNode}
     * @param a {@link Object} that should be transformed
     * @return A {@link JsonNode} with the information of the object
     */
    public static JsonNode toJson(Object a){
        return objectMapper.valueToTree(a);
    }

    /**
     * Transforms a {@link JsonNode} to a simple Sting
     * @param node The {@link JsonNode} that should be transformed
     * @return A String with the information in one Line
     * @throws IOException Because
     */
    public static String stringify(JsonNode node) throws IOException {
        return generateString(node, false);
    }

    /**
     * Transforms a {@link JsonNode} to a pritty Sting
     * @param node The {@link JsonNode} that should be transformed
     * @return A String with the information splitted on different lines
     * @throws IOException Because
     */
    public static String prittyPrint(JsonNode node) throws IOException {
        return generateString(node,true);
    }

    /**
     * Transforms a {@link JsonNode} into a String
     * @param node The {@link JsonNode} that should be transformed
     * @param pritty True for a pritty String False if String in one line
     * @return String with the information of the node
     * @throws IOException Because
     */
    private static String generateString(JsonNode node, boolean pritty) throws IOException {
        ObjectWriter objectWriter = objectMapper.writer();
        if(pritty){
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(node);
    }
}
