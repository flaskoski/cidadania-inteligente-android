package com.laskoski.f.felipe.cidadania_inteligente;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.laskoski.f.felipe.cidadania_inteligente.model.AbstractTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import java.util.ArrayList;

public class MissionDetailsActivity extends AppCompatActivity {
    MissionItem currentMission;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference tasksDatabaseReference;
    private ChildEventListener tasksEventListener;
    private ArrayList<AbstractTask> tasks;
    private Integer lastTaskNumber;
    private TaskAdapter taskAdapter;
    private ProgressBar progressBar;
    private TextView taskscompleted;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.i("task number", "dssds");
       // Log.i("task number", ((Integer)requestCode).toString());
        if (resultCode == RESULT_OK && data != null) {
         //   final ArrayList<AbstractTask> tasks = getTasksFromDB(currentMission);
                Boolean answeredCorrectly = data.getBooleanExtra("correct?",false);
                tasks.get(lastTaskNumber).setCompleted(true);
                ((QuestionTask)tasks.get(lastTaskNumber)).setAnsweredCorrectly(answeredCorrectly);
                //TODO right/wrong field
                taskAdapter.notifyDataSetChanged();
                if(answeredCorrectly){
                    incrementProgress();

                }
           // Log.i("task completed", tasks.get(requestCode).completed.toString() );

        }
    }

    private void incrementProgress(){
        progressBar.incrementProgressBy(1);
        taskscompleted.setText(String.valueOf(progressBar.getProgress())+"/"+taskscompleted.getText().toString().split("/")[1]);
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
        tasks = new ArrayList<>();
        TextView description = (TextView) findViewById(R.id.missionDescription);
        description.setText(currentMission.getDescription());

        //set progress bar
        progressBar = findViewById(R.id.missionProgress);
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);

        taskscompleted = findViewById(R.id.tasksCompleted);
        getTasksFromDB(currentMission);

    }
    private void setListView(){

    }
    private int getTasksFromDB(final MissionItem mission) {

        //Database initialization
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        tasksDatabaseReference = mFirebaseDatabase.getReference().child("tasks");

        //Adapter Initialization

        taskAdapter = new TaskAdapter(this,tasks);

        //get mock tasks
       // tasks.add(new QuestionTask("Question 1"))
        //get missions from DB
        tasksEventListener = new ChildEventListener() {
            QuestionTask task;
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                task = dataSnapshot.getValue(QuestionTask.class);
                if (mission.getTaskIDs().contains(dataSnapshot.getKey())){
                    tasks.add(task);
                    progressBar.setMax(tasks.size());
                    taskscompleted.setText("0/"+String.valueOf(tasks.size()));
                }
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

        //set List view and adapter
        ListView taskList = (ListView) findViewById(R.id.tasksList);
        taskList.setAdapter(taskAdapter);


        //TODO: move that to the creation activity
        ArrayList<String> answers = new ArrayList<>();
        answers.add("Pablo Picasso");
        answers.add("Leonardo da Vinci");
        answers.add("Michelangelo Buonarroti");
        answers.add("Claude Monet");

        tasks.add(new QuestionTask("Pinturas","Quem pintou o quadro Mona Lisa?",answers,2));
        tasks.add(new QuestionTask("Esculturas","test question 2?",answers,4));

        //Add action to list item
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int taskNumber, long l) {
                Intent goToTaskDetails = new Intent(getApplicationContext(), QuestionTaskDetailsActivity.class);
                goToTaskDetails.putExtra("task", tasks.get(taskNumber));
                lastTaskNumber = taskNumber;
                startActivityForResult(goToTaskDetails, taskNumber);
            }
        });

        return 0;
        //return tasks;
    }
}
