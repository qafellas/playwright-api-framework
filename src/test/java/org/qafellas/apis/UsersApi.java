package org.qafellas.apis;

import com.fasterxml.jackson.databind.JsonNode;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.qafellas.utilities.Constants;
import org.qafellas.utilities.Helpers;
import org.testng.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsersApi extends BaseApi {

    public APIResponse registerUser(String username, String email, String password) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("name", username);
        payload.put("email", email);
        payload.put("password", password);
        APIResponse response = formApiRequest().post(Constants.REGISTER_PATH, RequestOptions.create().setData(payload));
        return response;
    }

    public String getAuthToken(String email, String password) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", password);
        APIResponse response = formApiRequest().post(Constants.AUTH_TOKEN_PATH, RequestOptions.create().setData(payload));
        Assert.assertEquals(response.status(), 200);
        Assert.assertEquals(response.statusText(), "OK");
        JsonNode responseBodyJson = Helpers.responseJsonConverter(response);
        Assert.assertEquals(responseBodyJson.get("message").asText(), "Login successful");
        String token = responseBodyJson.get("data").get("token").asText();
        return token;
    }

    public APIResponse getUserProfile(String authToken) throws IOException {
        APIResponse response = formApiRequest().get(Constants.PROFILE_PATH, RequestOptions.create()
                .setHeader("x-auth-token", authToken));
        return response;
    }

    public APIResponse editUser(String authToken, String user, String phone, String company) throws IOException {
        Map<String, String> payload = new HashMap<>();
        payload.put("name", user);
        payload.put("phone", phone);
        payload.put("company", company);
        APIResponse response = formApiRequest().patch(Constants.PROFILE_PATH, RequestOptions.create()
                .setData(payload)
                .setHeader("x-auth-token", authToken));
        System.out.println(response.text());
        return response;
    }

    public APIResponse deleteUser(String authToken) throws IOException {
        APIResponse response = formApiRequest().delete(Constants.DELETE_USER_PATH, RequestOptions.create()
                .setHeader("x-auth-token", authToken));
        return response;
    }


}
