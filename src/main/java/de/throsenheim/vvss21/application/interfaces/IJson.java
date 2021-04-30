package de.throsenheim.vvss21.application.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface IJson {
    /**
     * Transforms a {@link String} into a {@link JsonNode}
     * @param src String that should be transformed
     * @return A JsonNote that contains the information from the string
     */
    JsonNode parse(String src) throws IOException;

    /**
     * Transforms a {@link JsonNode} to a class
     * @param node The JsonNote with the inforation
     * @param clazz the class that it should be transformed into
     * @return a object of the class clazz with the parameter of the node
     */
    <A> A fromJson(JsonNode node , Class<A> clazz) throws JsonProcessingException;

    /**
     * Transforms a {@link Object} into a {@link JsonNode}
     * @param a {@link Object} that should be transformed
     * @return A {@link JsonNode} with the information of the object
     */
    JsonNode toJson(Object a);

    /**
     * Transforms a {@link JsonNode} to a simple Sting
     * @param node The {@link JsonNode} that should be transformed
     * @return A String with the information in one Line
     */
    String stringify(JsonNode node) throws IOException;

    /**
     * Transforms a {@link JsonNode} to a pritty Sting
     * @param node The {@link JsonNode} that should be transformed
     * @return A String with the information splitted on different lines
     */
    String prittyPrint(JsonNode node) throws IOException;
}
