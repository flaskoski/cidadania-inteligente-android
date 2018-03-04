package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MissionsActivity extends AppCompatActivity {

    //private TextView mTextMessage;

    //For Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public final int RC_SIGN_IN=1;
    private String username;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference missionsDatabaseReference;
    private ChildEventListener missionsEventListener;

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


        //Database initialization
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        missionsDatabaseReference = mFirebaseDatabase.getReference().child("missions");

        //draw action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff669900")));

        //Navigation panel at the bottom
       //mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Get missions from DB and set adapter
        getMissionsFromDBandSetAdapter();

        setAuthenticationListener();

    }

    private void setAuthenticationListener() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //user signed in
                    onSignedInInitialize(user.getDisplayName());
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

    private void getMissionsFromDBandSetAdapter(){
        final ArrayList<MissionItem> missions = new ArrayList<>();
        final MissionAdapter missionsAdapter = new MissionAdapter(this, missions);
        missions.add(new MissionItem("Aventura no MASP",
                "Agora você vai mostrar que sabe tudo de arte respondendo perguntas sobre obras de arte presentes num dos pontos mais famosos de São Paulo, o MASP!",
                R.drawable.ic_info_black_24dp));
        missions.add(new MissionItem("Em busca do tesouro...", "", R.drawable.ic_sync_black_24dp));

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

        ListView missionsListView = (ListView)(findViewById(R.id.missionsListView));
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, missions);
        missionsListView.setAdapter(missionsAdapter);

        missionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToMissionDetails = new Intent(getApplicationContext(), MissionDetailsActivity.class);
                goToMissionDetails.putExtra("mission", missions.get(i));
                startActivity(goToMissionDetails);
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
