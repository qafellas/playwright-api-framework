package org.qafellas.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import org.qafellas.apis.NotesApi;
import org.qafellas.apis.UsersApi;
import org.qafellas.utilities.ConfigReader;
import org.qafellas.utilities.Helpers;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class NotesTests {
    public UsersApi usersApi;
    public NotesApi notesApi;
    String authToken;
    String noteID;
    @BeforeMethod
    public void setUp() throws IOException {
        usersApi = new UsersApi();
        notesApi = new NotesApi();
        authToken = usersApi.getAuthToken(ConfigReader.confReader().getProperty("testUser"), ConfigReader.confReader().getProperty("testPassword"));
    }
    @AfterMethod
    public void tearDown(){
        usersApi.closeApiContext();
    }

    @Test
    public void shouldAddNote() throws IOException {
        APIResponse response = notesApi.createNewNote(authToken, "New York trip", "Taxi: $45\n Hotel: $560", "Work");
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Note successfully created");
        noteID = responseBodyJson.get("data").get("id").asText();
        System.out.println("Note ID: " + noteID);
    }

    @Test
    public void shouldViewSpecificNote() throws IOException {
        APIResponse response = notesApi.retrieveSpecificNote(authToken, noteID);
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Note successfully retrieved");
        Assert.assertEquals(responseBodyJson.get("data").get("id").asText(), noteID);
    }

    @Test
    public void shouldNOTViewInvalidNote() throws IOException {
        APIResponse response = notesApi.retrieveSpecificNote(authToken, "6364ghhbehbr");
        Assert.assertEquals(response.status(), 400);
        Assert.assertEquals(response.statusText(), "Bad Request");
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Note ID must be a valid ID");
        Assert.assertFalse(responseBodyJson.get("success").asBoolean());
    }

    @Test
    public void shouldDeleteSpecificNote() throws IOException {
        APIResponse response = notesApi.deleteNote(authToken, noteID);
        Assert.assertEquals(response.status(), 200);
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Note successfully deleted");
        Assert.assertTrue(responseBodyJson.get("success").asBoolean());
    }

}
