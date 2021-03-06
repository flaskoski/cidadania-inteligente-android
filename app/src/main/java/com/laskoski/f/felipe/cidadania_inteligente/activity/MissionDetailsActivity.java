package com.laskoski.f.felipe.cidadania_inteligente.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.laskoski.f.felipe.cidadania_inteligente.R;
import com.laskoski.f.felipe.cidadania_inteligente.adapter.TaskAdapter;
import com.laskoski.f.felipe.cidadania_inteligente.connection.ParallelRequestsManager;
import com.laskoski.f.felipe.cidadania_inteligente.connection.SslRequestQueue;
import com.laskoski.f.felipe.cidadania_inteligente.featureAdmin.ToggleRouter;
import com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks.MissionAsyncTask;
import com.laskoski.f.felipe.cidadania_inteligente.httpBackgroundTasks.TaskAsyncTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.AbstractTask;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionItem;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MissionDetailsActivity extends AppCompatActivity {
    MissionItem currentMission;
    //For Firebase Authentication
    private FirebaseUser firebaseUser;
    private String uid;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    public final int RC_SIGN_IN=1;
    private String username;

//    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference tasksDatabaseReference;
//    private ChildEventListener tasksEventListener;
    private ArrayList<AbstractTask> tasks = new ArrayList<>();;
    private TaskAdapter taskAdapter;
    private Integer taskStartedNumber;
    private ProgressBar progressBar;
    private TextView taskscompleted;
    private MissionProgress missionProgress;
    private Map<Object, Object> tasksMap;

    private TaskAsyncTask taskAsyncTask;
    private RequestQueue mRequestQueue;
    private ParallelRequestsManager taskRequestsRemaining;
    private MissionAsyncTask missionAsyncTask;
    private ToggleRouter toggleRouter;

    private void sendMissionProgressBack(){
        if (missionProgress != null) {
            Intent taskResult = new Intent();
            taskResult.putExtra("missionStatus", missionProgress.getStatus());
            setResult(RESULT_OK, taskResult);
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                sendMissionProgressBack();
                return true;
        }
        return(super.onOptionsItemSelected(item));
    }


    @Override
    public void onBackPressed() {
        sendMissionProgressBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_details);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorActionBar));

        Intent missionDetails = getIntent();
        currentMission = (MissionItem) missionDetails.getSerializableExtra("mission");
        toggleRouter = (ToggleRouter) missionDetails.getSerializableExtra("toggleFeature");
        actionBar.setTitle(currentMission.getMissionName());

        //Set mission description on screen
        TextView description = (TextView) findViewById(R.id.missionDescription);
        description.setText(currentMission.getDescription());

        //set progress bar
        progressBar = findViewById(R.id.missionProgress);
        Drawable progressDrawable = progressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setProgressDrawable(progressDrawable);

        //
        taskRequestsRemaining = new ParallelRequestsManager(2);
        //set mission image
        mRequestQueue = new SslRequestQueue(getApplicationContext()).getSslRequesQueue();

        //Load server information from application.properties
        missionAsyncTask = new MissionAsyncTask(this);

        ImageView missionImage = (ImageView)  findViewById(R.id.missionImage);
        //taskAsyncTask = new TaskAsyncTask(this);


        try {
            missionAsyncTask.requestImageFromDB(MissionAsyncTask.IMAGE_TYPE_MISSION_IMAGE, mRequestQueue, currentMission.get_id(), missionImage, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        taskscompleted = findViewById(R.id.tasksCompleted);
        setAdapter();
        validateUserAndGetTasks();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
     //   final ArrayList<AbstractTask> tasks = getTasksFromDB(currentMission);

            String taskStartedId = data.getStringExtra("taskId" );
            Integer taskStatus = data.getIntExtra("taskStatus",MissionProgress.TASK_NOT_STARTED);
            if(taskStatus.equals(MissionProgress.TASK_COMPLETED)) {
                if(toggleRouter.featureIsEnabled("feature.xp"))
                    Toast.makeText(this, "+"+tasks.get(taskStartedNumber).getXp().toString()+" XP", Toast.LENGTH_LONG).show();
            }
            //new UpdatePlayerProgressAsyncTask().execute(params);
            missionAsyncTask.setMissionProgress(uid, mRequestQueue, onSetMissionProgressResponse, currentMission.get_id(), taskStartedId, taskStatus.toString());


            //--Update missionProgress object and ListView
            missionProgress.setOneTaskProgress(taskStartedId, taskStatus);
            updateListViewWithTaskResult(taskStatus);
            if(missionProgress.getStatus() == MissionProgress.MISSION_FINISHED) {
                setMissionCompletedView(true);
                if(toggleRouter.featureIsEnabled("feature.xp"))
                    if(missionProgress.isAllCorrect()) {
                        AlertDialog.Builder dialogs = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog);
                        dialogs.setIcon(R.drawable.gift)
                                .setMessage("Missão 100% correta: " + currentMission.getXp() + " XP!")
                                .setNeutralButton("OK", null);
                        dialogs.show();
                    }
            }
            //--
        }
    }

    private Response.Listener<Boolean> onSetMissionProgressResponse = new Response.Listener<Boolean>() {
        @Override
        public void onResponse(Boolean response) {
            if(response != null)
                Log.w("UpdatePlayerProgress: ", response.toString());
        }
    };

    private void updateListViewWithTaskResult(Integer taskResult) {
        if(taskResult.equals(MissionProgress.TASK_COMPLETED) || taskResult.equals(MissionProgress.TASK_FAILED)) {
            tasks.get(taskStartedNumber).setFinished(true);
            tasks.get(taskStartedNumber).setProgress(taskResult);
            setTasksAvailability();
            taskAdapter.notifyDataSetChanged();
            if (taskResult.equals(MissionProgress.TASK_COMPLETED)) {
                (tasks.get(taskStartedNumber)).setCompleted(true);
                incrementProgress();
            }
        }
    }

    private void setMissionCompletedView(Boolean animate) {
        ImageView imgMissionCompleted = findViewById(R.id.missionCompleted);
        imgMissionCompleted.setVisibility(View.VISIBLE);
        if(animate) {
            imgMissionCompleted.setAlpha(0f);
            imgMissionCompleted.setTranslationY(40f);
            imgMissionCompleted.animate().translationYBy(-40f).alpha(1f).setDuration(800);
        }
        progressBar.setVisibility(View.INVISIBLE);
        taskscompleted.setVisibility(View.INVISIBLE);

    }

    private void incrementProgress(){
        progressBar.incrementProgressBy(1);
        taskscompleted.setText(String.valueOf(progressBar.getProgress())+"/"+taskscompleted.getText().toString().split("/")[1]);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putSerializable("currentMission", currentMission);
        //savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    private void setListView(){

    }

    private Response.Listener<List<AbstractTask>> onTasksResponse = new Response.Listener<List<AbstractTask>>() {
        @Override
        public void onResponse(List<AbstractTask> tasksFromDB) {

            //TODO if(tasksFromDB == null? nullpointer exception
            Log.w("http response", currentMission.getTaskIDs().toString());
            tasksMap = new LinkedHashMap<>();
            for(AbstractTask task : tasksFromDB){
                tasksMap.put(task.get_id(), task);
            }
            taskRequestsRemaining.decreaseRemainingRequests();

            if(taskRequestsRemaining.isComplete())
                updateTasksProgress();
        }
    };

    private Response.Listener<MissionProgress> onMissionProgressResponse = new Response.Listener<MissionProgress>() {
        @Override
        public void onResponse(MissionProgress response) {
            //if(response == null?
            missionProgress = response;
            taskRequestsRemaining.decreaseRemainingRequests();

            if(taskRequestsRemaining.isComplete())
                updateTasksProgress();
        }
    };

    private void updateTasksProgress() {
        HashMap<String, Integer> tasksProgress = missionProgress.getTaskProgress();
        if(tasksMap == null) {
            Toast.makeText(this, "Falha ao carregar tarefas das missões.", Toast.LENGTH_SHORT).show();
            return;
        }
        Iterator it = tasksMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry task = ((Map.Entry) it.next());
            if(tasksProgress.get(task.getKey()) != null) {
                ((AbstractTask) task.getValue()).setProgress((Integer) tasksProgress.get(task.getKey()));
                Log.i("Task Progress: ", ((AbstractTask) task.getValue()).getProgress().toString());
            }
            else
                tasksProgress.put(task.getKey().toString(), MissionProgress.TASK_NOT_STARTED);
            if(!tasks.contains((AbstractTask) task.getValue()))
                tasks.add((AbstractTask) task.getValue());
        }
        taskAdapter.notifyDataSetChanged();
        Integer countCompletedTasks = 0;
        for(AbstractTask task : tasks) {
            if (task.isCompleted())
                countCompletedTasks++;
        }
        setTasksAvailability();
        progressBar.setMax(tasks.size());
        progressBar.setProgress(countCompletedTasks);
        taskscompleted.setText(countCompletedTasks.toString()+"/"+String.valueOf(tasks.size()));
        if(missionProgress.getStatus() == MissionProgress.MISSION_FINISHED)
            setMissionCompletedView(false);
//        }
//        else{
          //  Toast.makeText(this, "Erro ao carregar detalhes da missão.", Toast.LENGTH_SHORT).show();
        //}
    }

    //set Availability of tasks if mission is sequenced
    private void setTasksAvailability(){
        if(currentMission.isMandatorySequence()) {
            Boolean firstMissionFound = false;
            for (AbstractTask task : tasks)
                if (firstMissionFound)
                    task.setAvailable(false);
                else if (task.getProgress().equals(MissionProgress.TASK_NOT_STARTED)) {
                    firstMissionFound = true;
                    task.setAvailable(true);
                }
                else task.setAvailable(false);
        }
    }

    private void validateUserAndGetTasks() {
        //idlingSignIn.increment();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            uid = task.getResult().getToken();
                            getTasksFromDB();
                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
    }

    private void getTasksFromDB() {
        missionAsyncTask.getTasks(uid, mRequestQueue, onTasksResponse, currentMission.getTaskIDs());
        missionAsyncTask.getMissionProgress(uid, mRequestQueue, onMissionProgressResponse, currentMission.get_id());
    }

    private void setAdapter(){

        //Database initialization
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        tasksDatabaseReference = mFirebaseDatabase.getReference().child("tasks");

        //Adapter Initialization
        taskAdapter = new TaskAdapter(this,tasks);
        final SwipeRefreshLayout swipeRefreshLayout = (findViewById(R.id.swipeRefresh));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTasksFromDB();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //set List view and adapter
        ListView taskList = (ListView) findViewById(R.id.tasksList);
        taskList.setAdapter(taskAdapter);
//
//        ArrayList<String> answers = new ArrayList<>();
//        if(currentMission.get_id() == null) {
//            answers.add("Pablo Picasso");
//            answers.add("Leonardo da Vinci");
//            answers.add("Michelangelo Buonarroti");
//            answers.add("Claude Monet");
//
//            tasks.add(new QuestionTask("Pinturas", "Quem pintou o quadro Mona Lisa?", answers, 2));
//            tasks.add(new QuestionTask("Esculturas", "test question 2?", answers, 4));
//        }

        //Add action to list item
        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int taskNumber, long l) {
                Intent goToTaskDetails = new Intent(getApplicationContext(), tasks.get(taskNumber).getActivityClass());
                taskStartedNumber = taskNumber;
                goToTaskDetails.putExtra("task", tasks.get(taskNumber));
                startActivityForResult(goToTaskDetails, taskNumber);
            }
        });

    }
}
//***** set internal storage
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