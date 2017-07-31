package com.aaronfarr.popularmovies.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class NetworkUtilities {
    public static boolean checkConnectivity( Context context ) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    public static JSONObject getJsonFromUrl( String url ) throws ExecutionException, InterruptedException, JSONException {
        String response = new GetDataFromURLTask().execute(url).get();
        return new JSONObject(response);
    }
}
