package com.msd.console.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HttpRequestClient {

    private final static String endPoint = "http://localhost:8080/api/";

    public static JSONObject authenticate(String username, String password) throws IOException {
        HttpPost post = new HttpPost(endPoint.concat("authenticate"));
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"username\":" + "\"" + username + "\",");
        json.append("\"password\":" + "\"" + password + "\"");
        json.append("}");

        // send a JSON data
        post.setEntity(new StringEntity(json.toString()));
        post.addHeader("content-type", "application/json");

        String result = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            result = EntityUtils.toString(response.getEntity());
        }
        return new JSONObject(result);
    }


    /**
     * Save Person Details
     *
     * @param token
     * @param firstName
     * @param lastName
     * @param age
     * @param favouriteColor
     * @param hobby
     * @return
     * @throws IOException
     */
    public static JSONObject savePerson(String token, int id, String firstName, String lastName, int age, String favouriteColor, String hobby) throws IOException {
        HttpPost post = new HttpPost(endPoint.concat("person/add"));

        StringBuilder json = new StringBuilder();
        String[] hobbies = hobby.split(",");

        json.append("{");
        json.append("\"id\":" + "\"" + id + "\",");
        json.append("\"firstName\":" + "\"" + firstName + "\",");
        json.append("\"lastName\":" + "\"" + lastName + "\",");
        json.append("\"age\":" + "\"" + age + "\",");
        json.append("\"favouriteColor\":" + "\"" + favouriteColor + "\",");
        json.append("\"hobby\":" + "\"" + Arrays.toString(hobbies) + "\"");
        json.append("}");

        // send a JSON data
        post.setEntity(new StringEntity(json.toString()));
        post.addHeader("content-type", "application/json");
        post.addHeader("Authorization", token);

        String result = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            result = EntityUtils.toString(response.getEntity());
        }
        return new JSONObject(result);
    }

    /**
     * Delete Person
     *
     * @param token
     * @param personId
     * @return
     */
    public static String deletePerson(String token, int personId) throws IOException {
        HttpDelete delete = new HttpDelete(endPoint.concat("person/delete/" + personId));

        delete.addHeader("content-type", "application/json");
        delete.addHeader("Authorization", token);

        String result = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(delete)) {
            result = EntityUtils.toString(response.getEntity());
        }
        return result;
    }

    /**
     * Get Person By Id
     *
     * @param token
     * @param personId
     * @return
     * @throws IOException
     */
    public static JSONObject getPerson(String token, int personId) throws IOException {
        HttpGet get = new HttpGet(endPoint.concat("person/view/" + personId));

        get.addHeader("content-type", "application/json");
        get.addHeader("Authorization", token);

        String result = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)) {
            result = EntityUtils.toString(response.getEntity());
        }
        return new JSONObject(result);
    }

    /**
     * Get All Persons
     * @param token
     * @return
     */
    public static JSONArray getAllPerson(String token) throws IOException {
        HttpGet get = new HttpGet(endPoint.concat("person/view"));

        get.addHeader("content-type", "application/json");
        get.addHeader("Authorization", token);

        String result = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(get)) {
            System.out.println(response);
            result = EntityUtils.toString(response.getEntity());
        }
        return new JSONArray(result);

    }
}

