package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.Task;

import java.util.ArrayList;

public class MissionDetailsActivity extends AppCompatActivity {
    MissionItem currentMission;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference tasksDatabaseReference;
    private ChildEventListener tasksEventListener;
    private ArrayList<Task> tasks;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("task number", ((Integer)requestCode).toString());
        if (resultCode == RESULT_OK) {
         //   final ArrayList<Task> tasks = getTasksFromDB(currentMission);
            tasks.get(requestCode).completed = (Boolean) data.getSerializableExtra("completed?");
            Log.i("task completed", tasks.get(requestCode).completed.toString() );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff669900")));

        Intent missionDetails = getIntent();
        currentMission = (MissionItem) missionDetails.getSerializableExtra("mission");
        TextView description = (TextView) findViewById(R.id.missionDescription);
        description.setText(currentMission.getDescription());

        //final ArrayList<Task> tasks = getTasksFromDB(currentMission);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        tasksDatabaseReference = mFirebaseDatabase.getReference().child("tasks");
        tasks = new ArrayList<>();

        tasksEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                tasks.add(dataSnapshot.getValue(Task.class));
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
        TaskAdapter taskAdapter = new TaskAdapter(this,tasks);

        ListView taskList = (ListView) findViewById(R.id.tasksList);
        taskList.setAdapter(taskAdapter);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int taskNumber, long l) {
                Intent goToTaskDetails = new Intent(getApplicationContext(), TaskDetailsActivity.class);
                goToTaskDetails.putExtra("task", tasks.get(taskNumber));
                startActivityForResult(goToTaskDetails, taskNumber);
            }
        });


    }

    private int getTasksFromDB(MissionItem mission) {

        //TODO: move that to the creation activity
//        ArrayList<String> answers = new ArrayList<>();
//        answers.add("Pablo Picasso");
//        answers.add("Leonardo da Vinci");
//        answers.add("Michelangelo Buonarroti");
//        answers.add("Claude Monet");
//
//        ArrayList<Task> tasks = new ArrayList<>();
//        tasks.add(new QuestionTask("Pinturas","Quem pintou o quadro Mona Lisa?",answers,2));
//        tasks.add(new QuestionTask("Esculturas","test question 2?",answers,4));
//
        return 0;
        //return tasks;
    }
}
