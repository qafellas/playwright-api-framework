package org.qafellas.apis;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;
import org.qafellas.utilities.ConfigReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseApi {
    public Playwright playwright;
    public APIRequestContext request;

    public APIRequestContext formApiRequest() throws IOException {
        playwright = Playwright.create();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        request = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setBaseURL(ConfigReader.confReader().getProperty("baseUrl"))
                .setExtraHTTPHeaders(headers));
        return request;
    }

    public void closeApiContext(){
        request.dispose();
        playwright.close();
    }
}
