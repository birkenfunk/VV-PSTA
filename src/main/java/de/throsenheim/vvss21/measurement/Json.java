package de.throsenheim.vvss21.measurement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class Json {

    private static ObjectMapper objectMapper = getDefaultObjectMapper();

    private static ObjectMapper getDefaultObjectMapper(){
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        return defaultObjectMapper;
    }

    public static JsonNode parse(String src) throws IOException {
        return objectMapper.readTree(src);

    }

    public static <A> A fromJson(JsonNode node , Class<A> clazz) throws JsonProcessingException {
        return objectMapper.treeToValue(node, clazz);
    }

    public static JsonNode toJson(Object a){
        return objectMapper.valueToTree(a);
    }

    public static String stringify(JsonNode node) throws IOException {
        return generateString(node, false);
    }

    public static String prittyPrint(JsonNode node) throws IOException {
        return generateString(node,true);
    }

    private static String generateString(JsonNode node, boolean pritty) throws IOException {
        ObjectWriter objectWriter = objectMapper.writer();
        if(pritty){
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(node);
    }
}
