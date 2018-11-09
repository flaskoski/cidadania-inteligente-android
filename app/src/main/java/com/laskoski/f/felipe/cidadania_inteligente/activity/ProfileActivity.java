package com.laskoski.f.felipe.cidadania_inteligente.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.laskoski.f.felipe.cidadania_inteligente.R;
import com.laskoski.f.felipe.cidadania_inteligente.connection.SslRequestQueue;
import com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks.MissionAsyncTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.Player;

public class ProfileActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_perfil:
                    return true;
                case R.id.navigation_perfil_concluidas:
                    return true;
                case R.id.navigation_perfil_conquistas:
                    return true;
            }
            return false;
        }
    };
    private MissionAsyncTask missionAsyncTask;
    private RequestQueue mRequestQueue;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation_bar_profile);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Intent parametersIntent = getIntent();
        uid = parametersIntent.getStringExtra("uid");
        //initialize variables
        missionAsyncTask = new MissionAsyncTask(this);
        //HTTPS requests queue
        mRequestQueue = new SslRequestQueue(getApplicationContext()).getSslRequesQueue();

        //get profile information (MOCK)
        missionAsyncTask.getPlayerInfo(uid, mRequestQueue, setPlayerInfo);

    }

    Response.Listener<Player> setPlayerInfo = new Response.Listener<Player>() {
        @Override
        public void onResponse(Player p) {
            TextView playerName = (TextView) findViewById(R.id.lbPlayerName);
            TextView level = findViewById(R.id.lbPlayerLevelValue);
            TextView xp = findViewById(R.id.lbPlayerXpValue);
            ProgressBar xpProgress = findViewById(R.id.playerXpProgress);

            try {
                playerName.setText(p.getUsername());
                level.setText(p.getLevel().toString());
                xp.setText(p.getXp().toString());
            }
            catch(NullPointerException e){
                Toast.makeText(ProfileActivity.this, "Erro 401 - Falha ao carregar informações de Perfil", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
                xpProgress.setMax(10000);
                xpProgress.setProgress(p.getXp());

        }
    };

}
