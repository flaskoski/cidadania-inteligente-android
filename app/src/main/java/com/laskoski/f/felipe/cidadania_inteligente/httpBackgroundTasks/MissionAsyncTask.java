package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.laskoski.f.felipe.cidadania_inteligente.connection.AsyncResponse;
import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Felipe on 8/11/2018.
 */

public class MissionAsyncTask extends AsyncTask<String, Void, List<MissionItem>> implements ServerProperties {
    AsyncResponse delegate = null;
    static final String SERVER_MISSIONS_URL = SERVER_ROOT_SAFE_URL+"myMissions";

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
    public static void getMissionsGson(String uid, RequestQueue queue, Response.Listener<List<MissionItem>> responseListener) throws InterruptedException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);

        Type hashType = new TypeToken<List<MissionItem>>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();

        GsonRequest<List<MissionItem>> request = new GsonRequest<>(SERVER_MISSIONS_URL, hashType, headers, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
        });
        queue.add(request);
    }
    public static void getMissionProgress(String uid, RequestQueue queue, Response.Listener<MissionProgress> responseListener, String id) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);
        headers.put("missionID", id);

        Type hashType = new TypeToken<List<MissionItem>>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();

        GsonRequest<MissionProgress> request = new GsonRequest<>(SERVER_MISSION_PROGRESS_URL, hashType, headers, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

}
