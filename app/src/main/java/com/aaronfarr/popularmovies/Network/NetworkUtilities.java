package com.aaronfarr.popularmovies.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class NetworkUtilities {
    public static boolean checkConnectivity( Context context ) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static String getDataFromServer(String endpoint) throws IOException {
        BufferedReader inputStream;
        URL url = new URL( endpoint );
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        inputStream = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );
        return inputStream.readLine();
    }
}
