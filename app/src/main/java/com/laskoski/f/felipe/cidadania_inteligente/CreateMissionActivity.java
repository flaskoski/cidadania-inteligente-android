package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.Task;

import java.util.ArrayList;

public class CreateMissionActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference missionsDatabaseReference;
    private DatabaseReference tasksDatabaseReference;
    private ChildEventListener tasksEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
        missionsDatabaseReference = mDatabase.getReference().child("missions");
//        tasksDatabaseReference = mDatabase.getReference().child("missions");

        tasksEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                tasks.add(dataSnapshot.getValue(Task.class));
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
//        tasksDatabaseReference.addChildEventListener(tasksEventListener);


    }
    public void addTask(View v){}
//    final ArrayList<Task> tasks = getTasksFromDB(currentMission);
//    TaskAdapter taskAdapter = new TaskAdapter(this,tasks);
//
//    ListView taskList = (ListView) findViewById(R.id.tasksList);
//        taskList.setAdapter(taskAdapter);
//
//        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int taskNumber, long l) {
//            Intent goToTaskDetails = new Intent(getApplicationContext(), TaskDetailsActivity.class);
//            goToTaskDetails.putExtra("task", tasks.get(taskNumber));
//            startActivityForResult(goToTaskDetails, taskNumber);
//        }
//    });

    public void saveMission(View v){
        TextView title = findViewById(R.id.missionTitle);
        TextView description = findViewById(R.id.missionDescription);
        MissionItem newMission = new MissionItem(title.getText().toString(), description.getText().toString());
        missionsDatabaseReference.push().setValue(newMission);
        Toast.makeText(this, "Missão criada!", Toast.LENGTH_SHORT).show();
        finish();
    }
}