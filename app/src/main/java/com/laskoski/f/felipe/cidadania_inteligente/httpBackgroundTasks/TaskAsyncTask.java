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
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Felipe on 8/11/2018.
 */

public class TaskAsyncTask {

    public static void getTasks(String uid, RequestQueue queue, Response.Listener<List<QuestionTask>> responseListener, List<String> taskIDs) {
        Map<String, String> headers = new Hashtable<>();
        Map<String, String> params = new Hashtable<>();
        headers.put("Authorization", uid);
        String body = taskIDs.toString();
        Integer i=0;
        for(String taskID : taskIDs){
            params.put("taskID"+i.toString(), taskID);
            i++;
        }

        Type hashType = new TypeToken<List<QuestionTask>>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();

//        GsonRequest<List<QuestionTask>> request = new GsonRequest<>(SERVER_TASKS_URL, hashType, headers, params, responseListener, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//               error.printStackTrace();
//            }
//        });
//        queue.add(request);
    }
}
