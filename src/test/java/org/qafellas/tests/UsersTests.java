package org.qafellas.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.qafellas.apis.UsersApi;
import org.qafellas.utilities.ConfigReader;
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
    public String user, email, password;
    public String token;
    public UsersApi usersApi;
    public String tokenUserDeletion;

    @BeforeMethod
    public void setUp() throws IOException {
        user = ConfigReader.confReader().getProperty("registeryName");
        email = ConfigReader.confReader().getProperty("registryEmail");
        password = ConfigReader.confReader().getProperty("registeryPassword");
        usersApi = new UsersApi();
    }

    @AfterMethod
    public void tearDown(){
        usersApi.closeApiContext();
    }

    @Test()
    public void shouldRegisterUser() throws IOException {
        APIResponse response = usersApi.registerUser(user, email, password);
        Assert.assertEquals(response.status(), 201);
        Assert.assertEquals(response.statusText(), "Created");
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "User account created successfully");
    }


    @Test()
    public void shouldViewUserProfile() throws IOException {
        String token = usersApi.getAuthToken(email, password);
        APIResponse response = usersApi.getUserProfile(token);
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Profile successful");
    }

    @Test()
    public void shouldEditUserProfile() throws IOException {
        String token = usersApi.getAuthToken(email, password);
        APIResponse response = usersApi.editUser(token, user, "67478585", "qatest");
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Profile updated successful");
    }


    @Test()
    public void shouldDeleteUser() throws IOException {
        tokenUserDeletion = usersApi.getAuthToken(email, password);
        APIResponse response = usersApi.deleteUser(tokenUserDeletion);
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Account successfully deleted");

    }

    @Test()
    public void shouldNotDeleteUser() throws IOException {
        APIResponse response = usersApi.deleteUser(tokenUserDeletion);
        Assert.assertEquals(response.status(), 401);
        Assert.assertEquals(response.statusText(), "Unauthorized");
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Access token is not valid or has expired, you will need to login");

    }

}
