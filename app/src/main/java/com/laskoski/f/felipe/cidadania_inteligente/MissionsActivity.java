package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.support.test.espresso.idling.*;

import com.laskoski.f.felipe.cidadania_inteligente.model.GenericTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MissionsActivity extends AppCompatActivity {

    //private TextView mTextMessage;

    //For Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private String uid;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public final int RC_SIGN_IN=1;
    private String username;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference missionsDatabaseReference;
    private ChildEventListener missionsEventListener;
    private CountingIdlingResource idlingSignIn = new CountingIdlingResource("SIGN_IN");

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
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
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff669900")));

        //Navigation panel at the bottom
       //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getMissionsFromDBAndSetAdapter();
        //TODO send uid and get missions

        setAuthenticationListener();


    }

    private void setAuthenticationListener() {
        idlingSignIn.increment();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user signed in
                    onSignedInInitialize(user.getDisplayName());
                    //for testing purposes
                    idlingSignIn.decrement();
                    getMissions();
                }else {
                    //user signed out
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    private void getMissions() {

        mFirebaseAuth.getCurrentUser().getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            uid = task.getResult().getToken();
                            new DownloadMissions().execute();
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
    }

    private class DownloadMissions extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            // before the network request begins, show a progress indicator
        }
        @Override
        protected String doInBackground(Void... params) {
            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            //GET
//            try{
//                String url="http://10.0.2.2:8080/myMissions";
//                Log.w("user", "4343434343434");
//
//                // Make the HTTP GET request, marshaling the response to a String
//                Object result = restTemplate.getForObject(url, String.class);
//                Log.w("http response:", result.toString());
//                return result.toString();
//            }catch (Exception e) {       return "";    }

            try {
                // Set the Accept header

                //headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                //headers.setContentType(MediaType.APPLICATION_JSON);
                //headers.set("X-TP-DeviceID", "your value");
                //httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                //headers.set("Authorization", "Bearer " + uid);
                //HttpEntity<String> requestEntity = new HttpEntity<String>("parameters", headers);
                // The connection URL
                //String url = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0";
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                headers.set("Authorization", "Bearer " + uid);
                String url="http://10.0.2.2:8080/myMissions";
                // Add the String message converter
                //restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                Log.w("user", "4343434343434");
                HttpEntity<String> request = new HttpEntity<>(new String("bar"), headers);
                String result = restTemplate.postForObject(url, request, String.class);

                // Make the HTTP GET request, marshaling the response to a String
                //Object result = restTemplate.postForObject(,,String.class);
                Log.w("http response:", result.toString());
                return result.toString();
            }catch (Exception e) {
                Log.e("http request:", e.getMessage(), e);
                return "";
            }
        }
        @Override
        protected void onPostExecute(String mission) {
            // hide the progress indicator when the network request is complete

            // return the list of states
            //refreshRssFeed(feed);
        }

    }

    private void getMissionsFromDBAndSetAdapter(){
        //Database initialization
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        missionsDatabaseReference = mFirebaseDatabase.getReference().child("missions");

        //Adapter Initialization
        final ArrayList<MissionItem> missions = new ArrayList<>();
        final MissionAdapter missionsAdapter = new MissionAdapter(this, missions);

        List<GenericTask> tasks = getTasksFromDB();

        //add 2 additional missions for prototyping
        missions.add(new MissionItem("Aventura no MASP",
                "Agora você vai mostrar que sabe tudo de arte respondendo perguntas sobre obras de arte presentes num dos pontos mais famosos de São Paulo, o MASP!",
                R.drawable.ic_info_black_24dp, Arrays.asList(new String[]{"-L6qBmFKK-6IggKrAV6j", "-L6nRIpyJ2UO8Q42KM0G"})));
        missions.add(new MissionItem("Em busca do tesouro...", "", R.drawable.ic_sync_black_24dp, Arrays.asList(new String[]{"-L6qBmFKK-6IggKrAV6j"})));

        //get missions from DB
        missionsEventListener = new ChildEventListener() {
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

        //set List view and adapter
        ListView missionsListView = (ListView)(findViewById(R.id.missionsListView));
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, missions);
        missionsListView.setAdapter(missionsAdapter);

        //Add action to list item
        missionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToMissionDetails = new Intent(getApplicationContext(), MissionDetailsActivity.class);
                goToMissionDetails.putExtra("mission", missions.get(i));
                startActivity(goToMissionDetails);
            }
        });
    }

    private List<GenericTask> getTasksFromDB() {
        final List<GenericTask> tasks = new ArrayList<>();

        DatabaseReference tasksDatabaseReference = mFirebaseDatabase.getReference().child("tasks");
        //Adapter Initialization
        final TaskAdapter taskAdapter = new TaskAdapter(this,tasks);

        //get missions from DB
        ChildEventListener tasksEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                tasks.add(dataSnapshot.getValue(QuestionTask.class));
                taskAdapter.notifyDataSetChanged();
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
        tasksDatabaseReference.addChildEventListener(tasksEventListener);
        return tasks;
    }

    private void onSignedOutCleanUp() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RC_SIGN_IN == requestCode) {
            if (resultCode == RESULT_OK)
                Toast.makeText(this, "Entrou!", Toast.LENGTH_SHORT).show();
            else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    private void onSignedInInitialize(String username) {
        this.username = username;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            case R.id.create_mission:
                Intent createMissionIntent = new Intent(getApplicationContext(), CreateMissionActivity.class);
                startActivity(createMissionIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
