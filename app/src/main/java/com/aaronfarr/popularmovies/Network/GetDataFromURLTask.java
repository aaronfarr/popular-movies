package com.aaronfarr.popularmovies.Network;

import android.os.AsyncTask;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetDataFromURLTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        if( null != strings && strings.length > 0 ) {
            String url = strings[0];
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}