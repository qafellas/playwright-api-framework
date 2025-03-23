package org.qafellas.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.qafellas.utilities.Helpers;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsersTests {
    public APIRequestContext request;
    public Playwright playwright;
    public String user;
    public String token;

    @BeforeMethod
    public void setUp(){
        user = "user608";
        playwright = Playwright.create();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL("https://practice.expandtesting.com")
                .setExtraHTTPHeaders(headers));

    }

    @AfterMethod
    public void tearDown(){
        request.dispose();
        playwright.close();
    }

    @Test()
    public void shouldRegisterUser() throws IOException {
        /*
        {
          "name": "user106",
          "email": "user106@gmail.com",
          "password": "user106"
        }
         */
        Map<String, String> payload = new HashMap<>();
        payload.put("name", user);
        payload.put("email", user + "@gmail.com");
        payload.put("password", user);

        APIResponse response = request.post("/notes/api/users/register", RequestOptions.create().setData(payload));
        Assert.assertEquals(response.status(), 201);
        Assert.assertEquals(response.statusText(), "Created");
        System.out.println(response.text());

        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        System.out.println(responseBodyJson.toPrettyString());
        Assert.assertEquals(responseBodyJson.get("message").asText(), "User account created successfully");
    }

    @Test()
    public void shouldGetAuthorized() throws IOException {
        /*
        {
    "email": "user106@gmail.com",
    "password": "user106"
}
         */
        Map<String, String> payload = new HashMap<>();
        payload.put("email", user + "@gmail.com");
        payload.put("password", user);

        APIResponse response = request.post("/notes/api/users/login", RequestOptions.create().setData(payload));
        Assert.assertEquals(response.status(), 200);
        Assert.assertEquals(response.statusText(), "OK");

        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        System.out.println(responseBodyJson.toPrettyString());
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Login successful");
        token = responseBodyJson.get("data").get("token").asText();
        System.out.println("Token ===> " + token);

    }

    @Test()
    public void shouldViewUserProfile() throws IOException {
        APIResponse response = request.get("/notes/api/users/profile", RequestOptions.create()
                .setHeader("x-auth-token", token));
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        System.out.println(responseBodyJson.toPrettyString());
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Profile successful");
    }

    @Test()
    public void shouldEditUserProfile() throws IOException {
        /*
            {
    "name": "user104",
    "phone": "47575868",
    "company": "qaFellas"
         }
         */
        Map<String, String> payload = new HashMap<>();
        payload.put("name", user);
        payload.put("phone", "47575868");
        payload.put("company", "qaFellas");
        APIResponse response = request.patch("/notes/api/users/profile", RequestOptions.create()
                .setData(payload)
                .setHeader("x-auth-token", token));
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        System.out.println(responseBodyJson.toPrettyString());
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Profile updated successful");
    }


    @Test()
    public void shouldDeleteUser() throws IOException {
        APIResponse response = request.delete("/notes/api/users/delete-account", RequestOptions.create()
                .setHeader("x-auth-token", token));
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        System.out.println(responseBodyJson.toPrettyString());
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Account successfully deleted");

    }

}
