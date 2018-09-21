package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Felipe on 7/29/2018.
 */

public class ImageDownloader extends AsyncTask<String, Void, Bitmap>{
//    public static final String SERVER_IMAGE_URL = SERVER_ROOT_SAFE_URL+"downloadFile/";
//    public static final String SERVER_MISSION_IMAGES_URL = SERVER_IMAGE_URL+"missionImage";// + "missions/";
//    public static final String SERVER_MISSION_ICONS_URL = SERVER_IMAGE_URL+"missionIcon";// + "icons/";
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private RequestQueue getRequestQueue() {
        return requestQueue;
    }
    public ImageDownloader(RequestQueue requestQueue){
        this.requestQueue = requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    private RequestQueue requestQueue;

    @Override
    protected Bitmap doInBackground(String... urls) {
//        try {
//
          //  requestQueue.add(imageRequest);
//
//            /*        URL url = new URL(urls[0]+".png");
//            Log.w("url:",url.getPath());
//            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//            connection.connect();
//            connection.setSSLSocketFactory(requestQueue);
//            InputStream inputStream = connection.getInputStream();
//            Bitmap downloadedImage = BitmapFactory.decodeStream(inputStream);
//            return downloadedImage;*/
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(bitmap != null)
            super.onPostExecute(bitmap);
    }

    private Response.ErrorListener responseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };

    public void requestImageFromDB(String url, final ImageView view, Response.ErrorListener responseError) throws Exception {
        if(requestQueue == null) throw new Exception("Request queue not set. Use queue setter.");
        if(responseError != null) this.responseError = responseError;
        Response.Listener<Bitmap> imageResponseListener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if(response != null)
                    view.setImageBitmap(response);
            }
        };
        ImageRequest imageRequest = new ImageRequest(url+".png", imageResponseListener,0, // Image width
                0, // Image height
                ImageView.ScaleType.FIT_XY, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration new Response.ErrorListener() {
                this.responseError
        );
        requestQueue.add(imageRequest);
    }

        //Bitmap image = this.execute(url).get();
//        if(image == null) throw new FileNotFoundException();
//        return image;
}
