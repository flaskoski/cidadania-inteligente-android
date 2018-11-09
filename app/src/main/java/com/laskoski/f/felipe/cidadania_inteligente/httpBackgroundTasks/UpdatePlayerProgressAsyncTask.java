package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Created by Felipe on 8/11/2018.
 */

public class UpdatePlayerProgressAsyncTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        if(params == null || params[0] == null)
            return null;

        String uid = params[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            String[] httpParams = new String[3];
            for(int i=0; i<params.length-1; i++)
                httpParams[i] = params[i+1];
            // Set the Accept header
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", uid);

            //this ip corresponds to localhost. Since its virtual machine, it can't find localhost directly
           // String url=SERVER_MISSION_PROGRESS_UPDATE_URL;
            //Create the entity request (body plus headers)

            HttpEntity<String[]> request = new HttpEntity<String[]>(httpParams, headers);
            //Send HTTP POST request with the token id and receive the list of missions
           // Boolean okResponse = restTemplate.postForObject(url, request, Boolean.class);
           // Log.w("UpdatePlayerProgress: ", okResponse.toString());
            return "sucess";

        }catch (Exception e) {
            Log.e("http request:", e.getMessage(), e);
            return "error";
        }
    }
}
