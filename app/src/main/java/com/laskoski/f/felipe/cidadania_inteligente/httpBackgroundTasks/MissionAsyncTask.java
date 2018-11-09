package com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.gson.reflect.TypeToken;
import com.laskoski.f.felipe.cidadania_inteligente.connection.ServerProperties;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;
import com.laskoski.f.felipe.cidadania_inteligente.model.Player;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Felipe on 8/11/2018.
 */

public class MissionAsyncTask {
    public static final String IMAGE_TYPE_MISSION_IMAGE = "missionImage";
    public static final String IMAGE_TYPE_ICON = "icon";
    ServerProperties serverProperties;

    public MissionAsyncTask(Context applicationContext){
        serverProperties = new ServerProperties(applicationContext);
    }

    /**
     *
     * @param uid - Player id for server authorization
     * @param queue - https request queue
     * @param responseListener
     * @throws InterruptedException
     */
    public void getMissionsGson(String uid, RequestQueue queue, Response.Listener<List<MissionItem>> responseListener) throws InterruptedException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);

        Type hashType = new TypeToken<List<MissionItem>>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();

        GsonRequest<List<MissionItem>> request = new GsonRequest<>(serverProperties.SERVER_MISSIONS_URL, hashType, headers, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
            }
        });
        queue.add(request);
    }



    /**
     /**
     *
     * @param uid - Player id for server authorization
     * @param queue - https request queue
     * @param responseListener
     * @param missionId
     * @param taskId
     * @param taskProgress - Progress of the task (from -1 to 100) as string
     */
    public void setMissionProgress(String uid, RequestQueue queue, Response.Listener<Boolean> responseListener, String missionId, String taskId, String taskProgress) {
        Hashtable<String, String> headers = new Hashtable<>();
        Hashtable<String, String> params = new Hashtable();
        headers.put("Authorization", uid);
        params.put("missionId", missionId);
        params.put("taskId", taskId);
        params.put("taskProgress", taskProgress);

        Type hashType = new TypeToken<Boolean>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();

        GsonRequest<Boolean> request = new GsonRequest<>(serverProperties.SERVER_MISSION_PROGRESS_UPDATE_URL, hashType, headers, params, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  Log.e("http request:", error.getMessage());
                //TODO send error msg on UI and save progress on cache for future sync.
               // error.printStackTrace();
            }
        });
        queue.add(request);
    }

    /**
     * @param uid - Player id for server authorization
     * @param queue - https request queue
     * @param responseListener
     * @param missionId
     */
    public void getMissionProgress(String uid, RequestQueue queue, Response.Listener<MissionProgress> responseListener, String missionId) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);
        headers.put("missionID", missionId);

        Type hashType = new TypeToken<MissionProgress>() {}.getType();
        //Class hashType = (new HashMap<String, MissionProgress>()).getClass();

        GsonRequest<MissionProgress> request = new GsonRequest<>(serverProperties.SERVER_MISSION_PROGRESS_URL, hashType, headers, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

   // private HashMap<String, MissionProgress> missionsProgress;

    /**
     *
     /**
     *
     * @param uid - Player id for server authorization
     * @param queue - https request queue
     * @param responseListener
     * @throws InterruptedException
     */
    public void getMissionsProgress(String uid, RequestQueue queue, Response.Listener<HashMap<String, MissionProgress>> responseListener) throws InterruptedException {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);

        Type hashType = new TypeToken<HashMap<String, MissionProgress>>() {}.getType();

        GsonRequest<HashMap<String, MissionProgress>> request = new GsonRequest<>(serverProperties.SERVER_ALL_MISSION_PROGRESS_URL, hashType, headers, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //missionsProgress = null;
                //TODO error message
            }
        });
        queue.add(request);
    }


    public void getTasks(String uid, RequestQueue queue, Response.Listener<List<QuestionTask>> responseListener, List<String> taskIDs) {
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

        GsonRequest<List<QuestionTask>> request = new GsonRequest<>(serverProperties.SERVER_TASKS_URL, hashType, headers, params, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }


    private Response.ErrorListener responseError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
        }
    };



    public void requestImageFromDB(String imageType, RequestQueue requestQueue, String url, final ImageView view, Response.ErrorListener responseError) throws Exception {
        if(requestQueue == null) throw new Exception("Request queue not set. Use queue setter.");
        if(responseError != null) this.responseError = responseError;
        Response.Listener<Bitmap> imageResponseListener = new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                if(response != null)
                    view.setImageBitmap(response);
            }
        };
        switch (imageType){
            case IMAGE_TYPE_ICON:
                url = serverProperties.SERVER_MISSION_ICONS_URL + url;
                break;
            case IMAGE_TYPE_MISSION_IMAGE:
                url = serverProperties.SERVER_MISSION_IMAGES_URL + url;
                break;
            default:
                url = serverProperties.SERVER_IMAGE_URL + url;
        }
        ImageRequest imageRequest = new ImageRequest(url+".png", imageResponseListener,0, // Image width
                0, // Image height
                ImageView.ScaleType.FIT_XY, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration new Response.ErrorListener() {
                this.responseError
        );
        requestQueue.add(imageRequest);
    }

    public void getPlayerInfo(String uid, RequestQueue queue, Response.Listener<Player> responseListener) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", uid);

        GsonRequest<Player> request = new GsonRequest<>(serverProperties.SERVER_PLAYER_INFO_URL, Player.class, headers, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //missionsProgress = null;
            }
        });
        queue.add(request);
        //----MOCK------
//        Player p = new Player(uid);
//        p.setXp(1050);
//        p.setLevel(10);
//        return p;
        //----------------
    }
}
