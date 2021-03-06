package com.example.Smile.Factory.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.SmileParser;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

import static com.fasterxml.jackson.dataformat.smile.SmileGenerator.Feature.CHECK_SHARED_NAMES;
import static com.fasterxml.jackson.dataformat.smile.SmileGenerator.Feature.CHECK_SHARED_STRING_VALUES;

/**
 * This is used for converting SMILE back to JSON in the dump format.
 */
public class SmileToJson implements Function<byte[], byte[]> {
    private static SmileFactory smileFactory = new SmileFactory();
    private static final JsonFactory jsonFactory = new JsonFactory();

    static {
        smileFactory = smileFactory.disable(CHECK_SHARED_NAMES).disable(CHECK_SHARED_STRING_VALUES);
    }

    private SmileToJson() {
    }

    public static SmileToJson instance() {
        return SingletonHolder.instance;
    }

    private final static class SingletonHolder {
        private final static SmileToJson instance = new SmileToJson();
    }

    public byte[] apply(byte[] bytes) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (
            SmileParser sp = smileFactory.createParser(bytes);
            JsonGenerator jg = jsonFactory.createGenerator(bos)
        ) {
            while (sp.nextToken() != null) {
                jg.copyCurrentEvent(sp);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {

        String json = "{\"message\":\"hello!\"}";
        SomeClass obFromJson = new JsonMapper().readValue(json, SomeClass.class);
        byte[] smile = new SmileMapper().writeValueAsBytes(obFromJson);
        SmileToJson.instance().apply(smile);
        System.out.println();
        SomeClass obFromSmile = new SmileMapper().readValue(smile, SomeClass.class);
        String jsonBack = new JsonMapper().writeValueAsString(obFromSmile);
        System.out.println(jsonBack);

        System.out.println("__________________________________________");

        obFromJson = new SomeClass();
        obFromJson.setMessage("This is a test message");

        smile = new SmileMapper().writeValueAsBytes(obFromJson);
        SmileToJson.instance().apply(smile);
        System.out.println();
        obFromSmile = new SmileMapper().readValue(smile, SomeClass.class);
        jsonBack = new JsonMapper().writeValueAsString(obFromSmile);
        System.out.println(jsonBack);

        System.out.println("__________________________________________");



    }
}

class SomeClass {

    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SomeClass{" +
                "message='" + message + '\'' +
                '}';
    }
}