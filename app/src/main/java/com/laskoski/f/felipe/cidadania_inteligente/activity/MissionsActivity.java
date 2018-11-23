package com.laskoski.f.felipe.cidadania_inteligente.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import android.support.test.espresso.idling.*;

import com.laskoski.f.felipe.cidadania_inteligente.CreateMissionActivity;
import com.laskoski.f.felipe.cidadania_inteligente.R;
import com.laskoski.f.felipe.cidadania_inteligente.adapter.MissionAdapter;
import com.laskoski.f.felipe.cidadania_inteligente.connection.ParallelRequestsManager;
import com.laskoski.f.felipe.cidadania_inteligente.connection.SslRequestQueue;
import com.laskoski.f.felipe.cidadania_inteligente.featureAdmin.ToggleRouter;
import com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks.MissionAsyncTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MissionsActivity extends AppCompatActivity  {



    //For Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private String uid;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public final int RC_SIGN_IN=1;
    public final int ACTIVITY_MISSION_DETAILS=2;

    //For Mockito functional tests using FB authentication
    private CountingIdlingResource idlingSignIn = new CountingIdlingResource("SIGN_IN");
    //Activity Variables
    private ListView missionsListView;
    private MissionAdapter missionsAdapter;
    private List<MissionItem> missions = new ArrayList<>();
    //class to deal with request to get Missions list
    private Boolean firstTimeRequestingMissions = true;
    private Integer missionNumberStarted;
    private HashMap<String, MissionProgress> missionsProgress;
    private RequestQueue mRequestQueue;
    private MissionAsyncTask missionAsyncTask;
    //for getting missions info
    private ParallelRequestsManager missionRequestsRemaining = new ParallelRequestsManager(2);
    private ToggleRouter toggleRouter;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            missions = missionsAdapter.getOriginalValues();
            //TODO bug with wrong image icons
            //mRequestQueue.cancelAll(RequestFiler ...);
            missionsAdapter.notifyDataSetChanged();
            switch (item.getItemId()) {
                case R.id.navigation_notStarted:
                    missionsAdapter.getFilter().filter(MissionProgress.MISSION_NOT_STARTED.toString());
                    return true;
                case R.id.navigation_inProgress:
                    missionsAdapter.getFilter().filter(MissionProgress.MISSION_IN_PROGRESS.toString());
                    return true;
                case R.id.navigation_completed:
                    missionsAdapter.getFilter().filter(MissionProgress.MISSION_FINISHED.toString());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions);

        //draw action bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorActionBar));

        //Navigation panel at the bottom
       //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toggleRouter = new ToggleRouter(this);
        toggleRouter.setAllFeatures();

        //HTTPS requests queue
        mRequestQueue = new SslRequestQueue(getApplicationContext()).getSslRequesQueue();

        //Load server information from application.properties
        missionAsyncTask = new MissionAsyncTask(this);

        //If first time on the activity
        if (savedInstanceState == null) {
            setAdapter();
            //getExampleMissionsFromDBAndSetAdapter();
            //TODO infinite scroll
            authenticateAndLoadUserMissions();
        }

    }

    private void authenticateAndLoadUserMissions() {
        idlingSignIn.increment();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){//user signed in
                    onSignedInInitialize(user.getDisplayName());
                    //for testing purposes
                    //idlingSignIn.decrement();
                    validatePlayerAndGetMissions();
                }else {//user signed out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void validatePlayerAndGetMissions() {

        Task<GetTokenResult> getTokenResultTask = mFirebaseAuth.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            uid = task.getResult().getToken();
                            if (firstTimeRequestingMissions) {
                                getMissions();
                                firstTimeRequestingMissions = false;
                            }

                            
                        } else {
                            Toast.makeText(getApplicationContext(), "Conexão não estabelecida. Verifique se está com a internet ativada.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Conexão não estabelecida. Verifique se está com a internet ativada.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //--Get missions List
    private void getMissions(){
        try {
            //get missions
            missionAsyncTask.getMissionsGson(uid, mRequestQueue, missionsResponseListener);
            //get missions progress
            missionAsyncTask.getMissionsProgress(uid, mRequestQueue, missionProgressResponseListener);

        } catch (InterruptedException | /*ExecutionException | */NullPointerException e) {
            if(missionsProgress == null)
                missionsProgress = new HashMap<>();
            e.printStackTrace();
        }
    }

    private Response.Listener<List<MissionItem>> missionsResponseListener = new Response.Listener<List<MissionItem>>()
    {
        @Override
        public void onResponse (List<MissionItem> response){
            //Add the missions the server sent that are not on the missions list
//            for(MissionItem m : response) {
//                if (!missions.contains(m))
//                    missions.add(m);
//            }
//            //remove missions which are not on DB anymore.
//            for(MissionItem m : missions){
//                if(!response.contains(m))
//                    missions.remove(m);
//            }
            missions = response;
            //missionsAdapter.notifyDataSetChanged();
            //missions.addAll(response);
            missionRequestsRemaining.decreaseRemainingRequests();
            if(missionRequestsRemaining.isComplete())
                updateMissionsProgressAndAdapter();
        }
    };


    private Response.Listener<HashMap<String, MissionProgress>> missionProgressResponseListener = new Response.Listener<HashMap<String,MissionProgress>>()
    {
        @Override
        public void onResponse (HashMap <String, MissionProgress> response){
            missionsProgress = response;
            missionRequestsRemaining.decreaseRemainingRequests();

            if(missionRequestsRemaining.isComplete())
                updateMissionsProgressAndAdapter();
        }
    };
    //--


    private void updateMissionsProgressAndAdapter() {
        missionRequestsRemaining.reset();
        for (MissionItem m : missions) {
            if(missionsProgress != null) {
                MissionProgress missionProgress = missionsProgress.get(m.get_id());
                if (missionProgress != null)
                    m.setStatus(missionsProgress.get(m.get_id()).getStatus());
                else m.setStatus(MissionItem.MISSION_NOT_STARTED);
            }
            else m.setStatus(MissionItem.MISSION_NOT_STARTED);
        }
        missionsAdapter.setOriginalValues(missions);
        //filter on ListView
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(navigation.getSelectedItemId());
    }

    //}
    private void setAdapter(){
        missionsAdapter = new MissionAdapter(this, missions);
        //pass over the queue and server configuration
        missionsAdapter.setRequestQueue(mRequestQueue, missionAsyncTask);

        //set List view and adapter
        missionsListView = (ListView)(findViewById(R.id.missionsListView));
        final SwipeRefreshLayout swipeRefreshLayout = (findViewById(R.id.swipeRefresh));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMissions();
                swipeRefreshLayout.setRefreshing(false);
//                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//                navigation.setSelectedItemId(navigation.getSelectedItemId());
            }
        });
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, missions);
        missionsListView.setAdapter(missionsAdapter);

        //Add action to list item
        missionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToMissionDetails = new Intent(getApplicationContext(), MissionDetailsActivity.class);
                goToMissionDetails.putExtra("mission", (MissionItem) missionsListView.getAdapter().getItem(i));
                goToMissionDetails.putExtra("toggleFeature", toggleRouter);
                missionNumberStarted = i;
                startActivityForResult(goToMissionDetails, ACTIVITY_MISSION_DETAILS);
            }
        });
    }

    private void onSignedOutCleanUp() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RC_SIGN_IN == requestCode) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "Bem Vindo!", Toast.LENGTH_SHORT).show();
//            else if (resultCode == RESULT_CANCELED) {
//                finish();
//            }
        }
        else if(ACTIVITY_MISSION_DETAILS == requestCode){
            if(resultCode == RESULT_OK)
            {
                int missionStartedStatus = data.getIntExtra("missionStatus", 0);
                ((MissionItem)missionsListView.getAdapter().getItem(missionNumberStarted)).setStatus(Integer.valueOf(missionStartedStatus));
                updateNavigationOptionList(missionStartedStatus);
            }
        }
    }

    private void updateNavigationOptionList(int status) {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        if(status == MissionProgress.MISSION_FINISHED)
            navigation.setSelectedItemId(R.id.navigation_completed);
        else if(status == MissionProgress.MISSION_IN_PROGRESS)
            navigation.setSelectedItemId(R.id.navigation_inProgress);
        else navigation.setSelectedItemId(R.id.navigation_notStarted);
    }

    private void onSignedInInitialize(String username) {
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAuthStateListener != null)
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            case R.id.profile_menu:
                Intent goToPlayerProfileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                goToPlayerProfileIntent.putExtra("uid", uid);
                startActivity(goToPlayerProfileIntent);
                return true;
//            case R.id.create_mission:
//                Intent createMissionIntent = new Intent(getApplicationContext(), CreateMissionActivity.class);
//                startActivity(createMissionIntent);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

//    private void getExampleMissionsFromDBAndSetAdapter() {
//        //TODO delete this function after DB fully operational
//Database initialization
//Adapter Initialization
//List<AbstractTask> tasks = getTasksFromDB();

//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        missionsDatabaseReference = mFirebaseDatabase.getReference().child("missions");
//add 2 additional missions for prototyping
//        missions.add(new MissionItem("Aventura no MASP",
//                "Agora você vai mostrar que sabe tudo de arte respondendo perguntas sobre obras de arte presentes num dos pontos mais famosos de São Paulo, o MASP!",
//                R.drawable.ic_info_black_24dp, Arrays.asList(new String[]{"-L6qBmFKK-6IggKrAV6j", "-L6nRIpyJ2UO8Q42KM0G"})));
//        missions.add(new MissionItem("Em busca do tesouro...", "", R.drawable.ic_sync_black_24dp, Arrays.asList(new String[]{"-L6qBmFKK-6IggKrAV6j"})));
//        missionsAdapter.notifyDataSetChanged();
//    private List<AbstractTask> getTasksFromDB() {
//        final List<AbstractTask> tasks = new ArrayList<>();
//
//        DatabaseReference tasksDatabaseReference = mFirebaseDatabase.getReference().child("tasks");
//        //Adapter Initialization
//        final TaskAdapter taskAdapter = new TaskAdapter(this,tasks);
//
//        //get missions from DB
//        ChildEventListener tasksEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                tasks.add(dataSnapshot.getValue(QuestionTask.class));
//                taskAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        };
//        tasksDatabaseReference.addChildEventListener(tasksEventListener);
//        return tasks;
//    }


//get missions from DB
        /*missionsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                missions.add(dataSnapshot.getValue(MissionItem.class));
                missionsAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        missionsDatabaseReference.addChildEventListener(missionsEventListener);
        */