package org.qafellas.utilities;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;

import java.io.IOException;

public class Helpers {
    /**
     * Convert api response in to json format
     * @param response
     * @return
     * @throws IOException
     */
    public static JsonNode responseJsonConverter(APIResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(response.body());
    }
}
