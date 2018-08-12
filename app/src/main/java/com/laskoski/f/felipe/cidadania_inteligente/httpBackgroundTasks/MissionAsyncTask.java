package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.laskoski.f.felipe.cidadania_inteligente.connection.AsyncResponse;
import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Felipe on 8/11/2018.
 */

public class MissionAsyncTask extends AsyncTask<String, Void, List<MissionItem>> implements ServerProperties {
    AsyncResponse delegate = null;
    static final String SERVER_MISSIONS_URL = SERVER_ROOT_URL+"myMissions";

    @Override
    protected void onPreExecute() {
        // before the network request begins, show a progress indicator
    }
    @Override
    protected List<MissionItem> doInBackground(String... params) {
        if(params == null || params[0] == null)
            return null;
        String uid = params[0];


        // Create a new RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();
        try {
            // Set the Accept header
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            headers.set("Authorization", uid);

            //this ip corresponds to localhost. Since its virtual machine, it can't find localhost directly
            //Create the entity request (body plus headers)
            HttpEntity<String> request = new HttpEntity<>("body", headers);
            //Send HTTP POST request with the token id and receive the list of missions
            MissionItem[] missionsFromDB = restTemplate.postForObject(SERVER_MISSIONS_URL, request, MissionItem[].class);
            Log.w("http response", Arrays.toString(missionsFromDB));

            return Arrays.asList(missionsFromDB);

        }catch (Exception e) {
            Log.e("http request:", e.getMessage(), e);
            return null;
        }
    }

}
