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
import com.google.gson.Gson;
import com.laskoski.f.felipe.cidadania_inteligente.model.AbstractTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private FileWriter writer;
    private FileInputStream reader;
    Gson gson = new Gson();
    private final String TASKSFILENAME = "/tasks.dat";
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

                taskAdapter.notifyDataSetChanged();
                if(answeredCorrectly){
                    incrementProgress();

                }
//            try {
//                Log.w("JSON", gson.toJson(tasks));
//            //     writer.write(gson.toJson(tasks));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private void incrementProgress(){
        progressBar.incrementProgressBy(1);
        taskscompleted.setText(String.valueOf(progressBar.getProgress())+"/"+taskscompleted.getText().toString().split("/")[1]);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        try {
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

        //set List view and adapter
        ListView taskList = (ListView) findViewById(R.id.tasksList);
        taskList.setAdapter(taskAdapter);

        //set internal storage
//        try {
//            Log.w("caminho",getFilesDir().getAbsolutePath());
//            reader = openFileInput( TASKSFILENAME);
//            //TODO read file
//
//            reader.close();
//            deleteFile(TASKSFILENAME);
//            writer = new FileWriter(new File(TASKSFILENAME));
//        } catch (FileNotFoundException e) {
//            try {
//                writer = new FileWriter(new File(TASKSFILENAME));
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
     /*
     FIREBASE REALTIME DATABASE
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
*/