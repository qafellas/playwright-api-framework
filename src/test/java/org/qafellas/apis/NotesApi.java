package org.qafellas.apis;

import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import org.qafellas.utilities.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NotesApi extends BaseApi {

    public APIResponse createNewNote(String authToken, String title, String description, String category) throws IOException {
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("description", description);
        data.put("category", category);

        APIResponse response = formApiRequest().post(Constants.NOTE_API_PATH, RequestOptions.create().setData(data)
                .setHeader("x-auth-token", authToken)
        );
        return response;
    }

    public APIResponse retrieveSpecificNote(String authToken, String noteID) throws IOException {
        APIResponse response = formApiRequest().get(Constants.NOTE_API_PATH + "/" + noteID, RequestOptions.create()
                .setHeader("x-auth-token", authToken)
        );
        return response;
    }

    public APIResponse deleteNote(String authToken, String noteID) throws IOException {
        APIResponse response = formApiRequest().delete(Constants.NOTE_API_PATH + "/" + noteID, RequestOptions.create()
                .setHeader("x-auth-token", authToken)
        );
        return response;
    }

}
