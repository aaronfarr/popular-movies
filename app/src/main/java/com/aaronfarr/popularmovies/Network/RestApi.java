package com.aaronfarr.popularmovies.Network;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;

public class RestApi {
    public static JSONObject get( String endpoint ) throws ExecutionException, InterruptedException, JSONException {
        String response = new GetDataFromURLTask().execute(endpoint).get();
        return new JSONObject(response);
    }
}