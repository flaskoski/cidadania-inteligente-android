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

public class TaskAsyncTask implements ServerProperties {

    public static void getMissionProgressGson(String uid, RequestQueue queue, Response.Listener<List<MissionItem>> responseListener) throws InterruptedException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);

        Type hashType = new TypeToken<List<MissionItem>>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();

        GsonRequest<List<MissionItem>> request = new GsonRequest<>(SERVER_TASKS_URL, hashType, headers, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
