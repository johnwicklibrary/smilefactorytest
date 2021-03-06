package com.example.Smile.Factory.demo;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.smile.SmileFactory;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

import java.io.ByteArrayOutputStream;

public class SmileTest {

    public static byte[] convertToSmile(byte[] json, JsonFactory jsonFactory,
                                        SmileFactory smileFactory) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try // try-with-resources
                (
                        JsonGenerator jg = smileFactory.createGenerator(bos);
                        JsonParser jp = jsonFactory.createParser(json)
                ) {
            while (jp.nextToken() != null) {
                jg.copyCurrentEvent(jp);
            }
        } catch (Exception e) {
            System.out.println("Error while converting json to smile" + e);
        }

        return bos.toByteArray();
    }

    public static byte[] convertToJson(byte[] smile, JsonFactory jsonFactory,
                                       SmileFactory smileFactory) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (
                JsonParser sp = smileFactory.createParser(smile);
                JsonGenerator jg = jsonFactory.createGenerator(bos)
        ) {
            while (sp.nextToken() != null) {
                jg.copyCurrentEvent(sp);
            }
        } catch (Exception e) {
            System.out.println("Error while converting smile to json"+ e);
        }

        return bos.toByteArray();
    }


    public static void main(String[] args) throws JsonProcessingException {

        JsonFactory jsonFactory = new JsonFactory();
        SmileFactory smileFactory = new SmileFactory();

        SmileDemo smileDemo = new SmileDemo();
        smileDemo.setKey("key1");
        smileDemo.setValue("value");
        System.out.println(smileDemo);

        System.out.println("-------------convertToSmile----------------");

        byte[] smile = new SmileMapper().writeValueAsBytes(smileDemo);
        smile = convertToSmile(smile,jsonFactory,smileFactory);

        String jsonBack = new JsonMapper().writeValueAsString(smile);
        System.out.println(jsonBack);

        System.out.println("-------------convertToJson----------------");
        smileDemo = new SmileDemo();
        smileDemo.setKey("key2");
        smileDemo.setValue("value2");
        System.out.println(smileDemo);
        smile = new SmileMapper().writeValueAsBytes(smileDemo);
        smile= convertToJson(smile,jsonFactory,smileFactory);
        jsonBack = new JsonMapper().writeValueAsString(smile);
        System.out.println("jsonBack " + jsonBack);
    }
}


class SmileDemo {

    private String key;
    private String value;

    public SmileDemo() {
    }

    public SmileDemo(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SmileDemo{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}