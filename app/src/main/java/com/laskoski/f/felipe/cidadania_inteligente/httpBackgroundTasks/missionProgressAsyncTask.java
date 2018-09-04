package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.reflect.TypeToken;
import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Felipe on 7/29/2018.
 */

public class missionProgressAsyncTask extends AsyncTask<String, Void, HashMap<String, MissionProgress>> implements ServerProperties{
    private static HashMap<String, MissionProgress> missionsProgress;

    @SuppressWarnings("unchecked")
    @Override
        protected HashMap<String, MissionProgress> doInBackground(String... params) {
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
            ParameterizedTypeReference<HashMap<String, MissionProgress>> typeref = new ParameterizedTypeReference<HashMap<String, MissionProgress>>() {};
            ResponseEntity<HashMap<String, MissionProgress>> response = restTemplate.exchange(url, HttpMethod.POST, request, typeref);
            return response.getBody();
            //return restTemplate.postForObject(url, request, HashMap.class);

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

    public static GsonRequest<HashMap<String, MissionProgress>> getMissionProgressGson(String uid, OnCompleteListener<GetTokenResult> queue, RequestFuture<HashMap<String, MissionProgress>> future) throws InterruptedException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);
        Type hashType = new TypeToken<HashMap<String, MissionProgress>>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();
        JsonObjectRequest
        GsonRequest<HashMap<String, MissionProgress>> request = new GsonRequest<>(ServerProperties.SERVER_ALL_MISSION_PROGRESS_URL, hashType, headers, new Response.Listener<HashMap<String, MissionProgress>>() {
            @Override
            public void onResponse(HashMap<String, MissionProgress> response) {
                missionsProgress = response;
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                missionsProgress = null;
            }
        });
        return request;
    }
}
