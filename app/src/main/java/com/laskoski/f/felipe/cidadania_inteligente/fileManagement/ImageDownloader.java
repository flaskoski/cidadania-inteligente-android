package com.laskoski.f.felipe.cidadania_inteligente.fileManagement;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Felipe on 7/29/2018.
 */

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> implements ServerProperties{
    public static final String SERVER_IMAGE_URL = SERVER_ROOT_URL+"downloadFile/";
    public static final String SERVER_MISSION_IMAGES_URL = SERVER_IMAGE_URL+"missionImage";// + "missions/";
    public static final String SERVER_MISSION_ICONS_URL = SERVER_IMAGE_URL+"missionIcon";// + "icons/";
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
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
    }

    public static Bitmap getImageFromDB(String url) throws ExecutionException, InterruptedException, FileNotFoundException {
        Bitmap image = new ImageDownloader().execute(url).get();
        if(image == null) throw new FileNotFoundException();
        return image;
    }
}
