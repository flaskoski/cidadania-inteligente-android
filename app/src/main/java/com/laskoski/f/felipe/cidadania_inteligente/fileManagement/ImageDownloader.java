package com.laskoski.f.felipe.cidadania_inteligente.fileManagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Felipe on 7/29/2018.
 */

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    public static final String SERVER_ROOT_URL = "http://10.0.2.2:8080/downloadFile/";
    public static final String SERVER_MISSIONS_PATH = SERVER_ROOT_URL+ "missions/";
    public static final String SERVER_MISSION_ICONS_PATH = SERVER_MISSIONS_PATH+ "icons/";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]+".png");
            Log.w("url:",url.getPath());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap downloadedImage = BitmapFactory.decodeStream(inputStream);
            return downloadedImage;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }
}
