package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;

import java.util.ArrayList;

public class MissionsActivity extends AppCompatActivity {

    private TextView mTextMessage;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff669900")));

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final ArrayList<MissionItem> missions = new ArrayList<>();
        missions.add(new MissionItem("Aventura no MASP",
                "Agora você vai mostrar que sabe tudo de arte respondendo perguntas sobre obras de arte presentes num dos pontos mais famosos de São Paulo, o MASP!",
                R.drawable.ic_info_black_24dp));
        missions.add(new MissionItem("Em busca do tesouro...", "", R.drawable.ic_sync_black_24dp));

        ListView missionsListView = (ListView)(findViewById(R.id.missionsListView));
        MissionAdapter missionsAdapter = new MissionAdapter(this, missions);
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

}
