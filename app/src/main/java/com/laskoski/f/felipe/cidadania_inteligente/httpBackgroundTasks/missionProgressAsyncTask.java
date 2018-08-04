package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Felipe on 7/29/2018.
 */

public class missionProgressAsyncTask extends AsyncTask<String, Void, HashMap<String, Integer>> implements ServerProperties{
    @SuppressWarnings("unchecked")
    @Override
        protected HashMap<String, Integer> doInBackground(String... params) {
        RestTemplate restTemplate = new RestTemplate();
        String url;

        if(params == null || params[0] == null)
            return null;

        String uid = params[0];
        String requestType = params[1];

        if(requestType == null)
            url = ServerProperties.SERVER_MISSION_PROGRESS_URL;
        else //if(requestType.equals("all"))
            url = ServerProperties.SERVER_ALL_MISSION_PROGRESS_URL;

        try {
            // Set the Accept header
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", uid);

            //this ip corresponds to localhost. Since its virtual machine, it can't find localhost directly

            //Create the entity request (body plus headers)
            HttpEntity<String> request = new HttpEntity<>("bar", headers);
            //Send HTTP POST request with the token id and receive the list of missions
            return restTemplate.postForObject(url, request, HashMap.class);

//            Log.w("missionStatus list", allMissionsStatus.toString());
//
//            return allMissionsStatus;

        }catch (Exception e) {
            Log.e("http request:", e.getMessage(), e);
            return null;
        }
    }

//    public static Bitmap getImageFromDB(String url) throws ExecutionException, InterruptedException, FileNotFoundException {
//        Bitmap image = new missionProgressAsyncTask().execute(url).get();
//        if(image == null) throw new FileNotFoundException();
//        return image;
//   }
}
