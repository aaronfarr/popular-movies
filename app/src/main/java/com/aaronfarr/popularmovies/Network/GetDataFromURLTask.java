package com.aaronfarr.popularmovies.Network;

import android.os.AsyncTask;
import java.io.IOException;

public class GetDataFromURLTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        if( null != strings && strings.length > 0 ) {
            String url = strings[0];
            try {
                return NetworkUtilities.getDataFromServer(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}