package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;

import java.util.List;

public class CreateMissionActivity extends AppCompatActivity {

//    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
//    private DatabaseReference missionsDatabaseReference;
//    private DatabaseReference tasksDatabaseReference;
    private List<String> taskIDs;
//    private ChildEventListener tasksEventListener;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data != null){
            Log.i("getKey()",data.getStringExtra("taskID"));
            taskIDs.add(data.getStringExtra("taskID"));
            //TODO add on list view and notify adapter for change
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mission);
//        missionsDatabaseReference = mDatabase.getReference().child("missions");
////        tasksDatabaseReference = mDatabase.getReference().child("missions");
//
//        tasksEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
////                tasks.add(dataSnapshot.getValue(AbstractTask.class));
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


    }
    public void addTask(View v){
//        tasksDatabaseReference = mDatabase.getReference().child("tasks");
        Intent createTaskIntent = new Intent(getApplicationContext(), CreateTaskActivity.class);
        startActivityForResult(createTaskIntent,0);
        //TODO save taskID on mission var
    }
//    final ArrayList<AbstractTask> tasks = getTasksFromDB(currentMission);
//    TaskAdapter taskAdapter = new TaskAdapter(this,tasks);
//
//    ListView taskList = (ListView) findViewById(R.id.tasksList);
//        taskList.setAdapter(taskAdapter);
//
//        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int taskNumber, long l) {
//            Intent goToTaskDetails = new Intent(getApplicationContext(), QuestionTaskDetailsActivity.class);
//            goToTaskDetails.putExtra("task", tasks.get(taskNumber));
//            startActivityForResult(goToTaskDetails, taskNumber);
//        }
//    });

    public void saveMission(View v){
        TextView title = findViewById(R.id.missionTitle);
        TextView description = findViewById(R.id.missionDescription);
        MissionItem newMission = new MissionItem(title.getText().toString(), description.getText().toString(),-1,taskIDs);
        //TODO: check if it really save on the DB.
//        missionsDatabaseReference.push().setValue(newMission);
        Toast.makeText(this, "Missão criada!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
