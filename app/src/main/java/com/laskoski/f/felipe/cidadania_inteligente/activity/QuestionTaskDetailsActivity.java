package com.laskoski.f.felipe.cidadania_inteligente.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.laskoski.f.felipe.cidadania_inteligente.R;
import com.laskoski.f.felipe.cidadania_inteligente.model.MissionProgress;
import com.laskoski.f.felipe.cidadania_inteligente.model.QuestionTask;

import org.w3c.dom.Text;

public class QuestionTaskDetailsActivity extends AppCompatActivity {
    public QuestionTask task;
    private Boolean goBackConfirmed;
    Intent taskResult = new Intent();
    //Tempo para responder
    CountDownTimer countDownTimer;
    public static final int[] answerViewIds = {R.id.answer1, R.id.answer2, R.id.answer3, R.id.answer4, R.id.answer5, R.id.answer6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_task_details);

//***back button shouldnt appear here, so that players can't see the question and exit.
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.colorActionBar));
        this.task = getTaskDetails();
        this.taskResult.putExtra("taskId", task.get_id());
        setQuestion();
        setNumberOfAnswers();
        setAnswers();
        if( this.task.isFinished())
            setFinishedState();

        setTime();
//        TextView question = (TextView) listItemView.findViewById((R.id.question));
//        question.setText(((QuestionTask)currentItem).getQuestion());
//
//        int counter = 1;
//        for(String answer : ((QuestionTask)currentItem).getAnswers()){
//            ((TextView) listItemView.findViewById(QuestionTaskDetailsActivity.answerViews[counter])).setText(answer);
//            counter++;
//        }

    }

    private void setTime() {
        final TextView timer = (TextView) findViewById(R.id.lb_timer);
        countDownTimer =new CountDownTimer(task.getTimeToAnswer()*1000, 1000) {
            final TextView timer = (TextView) findViewById(R.id.lb_timer);
            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }
            public void onFinish() {
                timer.setText("0");
                taskResult.putExtra("taskStatus", MissionProgress.TASK_FAILED);
                setResult(RESULT_OK, taskResult);

                AlertDialog.Builder dialogs = new AlertDialog.Builder(QuestionTaskDetailsActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
                dialogs.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })      .setTitle("Atenção")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage("O seu tempo acabou!")
                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                finish();
                            }
                        });
                dialogs.show();
            }
        };
        if(task.getTimeToAnswer() != null && task.getTimeToAnswer() > 0){
            countDownTimer.start();
        }
        else timer.setVisibility(View.GONE);
    }

    private void setFinishedState() {
        AlertDialog.Builder dialogs = new AlertDialog.Builder(QuestionTaskDetailsActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
        dialogs.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        })      .setTitle("Atenção")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Essa tarefa já foi respondida!")
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                    }
                });

        for(int i=1; i <= task.getAnswers().size(); i++)
            ((TextView) findViewById(answerViewIds[i-1])).setLinksClickable(false);
        TextView correct_answer = findViewById(answerViewIds[this.task.getCorrectAnswer()-1]);
        correct_answer.setBackground(getResources().getDrawable(R.drawable.transition_right));
        TransitionDrawable transitionRectangleRightAnswer = (TransitionDrawable) correct_answer.getBackground();
        correct_answer.setTextColor(Color.WHITE);
        transitionRectangleRightAnswer.startTransition(0);
        dialogs.show();

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(QuestionTaskDetailsActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
        confirmationDialog.setTitle("Atenção")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Quer mesmo sair da questão? Você não vai poder voltar para responder depois.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        taskResult.putExtra("taskStatus", MissionProgress.TASK_FAILED);
                        setResult(RESULT_OK, taskResult);
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, null);
        confirmationDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if timer is still ticking, finish it
        countDownTimer.cancel();
    }

    /**
     * set answer texts
     */
    private void setAnswers() {

        for(int i=1; i <= task.getAnswers().size(); i++)
            ((TextView) findViewById(answerViewIds[i-1])).setText(task.getAnswers().get(i-1));
    }
    private void setQuestion() {
        ((TextView) findViewById(R.id.question)).setText(task.getQuestion() );
    }

    private QuestionTask getTaskDetails() {
        Intent taskDetails = getIntent();
        return (QuestionTask) taskDetails.getSerializableExtra("task");
    }

    /**
     * Hide Unnecessary answer text views and correct their margins
     */
    private void setNumberOfAnswers() {
        for(int i=6; i > task.getAnswers().size(); i--)
            findViewById(answerViewIds[i-1]).setVisibility(View.GONE);

        //reset margins
//        switch(task.getAnswers().size()){
//            case 3:
//                ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams) findViewById(answerViewIds[3]).getLayoutParams();
//                params3.setMargins(16,8,16,16);
//                findViewById(answerViewIds[3]).setLayoutParams(params3);
//                break;
//            case 5:
//                ConstraintLayout.LayoutParams params5= (ConstraintLayout.LayoutParams) findViewById(answerViewIds[5]).getLayoutParams();
//                params5.setMargins(16,8,16,16);
//                findViewById(answerViewIds[5]).setLayoutParams(params5);
//                break;
//        }


    }

    public void checkAnswer(View v){
        TextView correct_answer = findViewById(answerViewIds[task.getCorrectAnswer()-1]);
        TextView user_answer = (TextView) v;
        Integer taskStatus = MissionProgress.TASK_FAILED;

        //check if the answer is correct
        if(correct_answer == user_answer)
            taskStatus = MissionProgress.TASK_COMPLETED;

        //draw green rectangle on correct option
        correct_answer.setBackground(getResources().getDrawable(R.drawable.transition_right));
        TransitionDrawable transitionRectangleRightAnswer = (TransitionDrawable) correct_answer.getBackground();
        correct_answer.setTextColor(Color.WHITE);
        transitionRectangleRightAnswer.startTransition(400);

        //draw rectangle on user option
        TransitionDrawable transitionRectangleUserAnswer = (TransitionDrawable) user_answer.getBackground();
        user_answer.setTextColor(Color.WHITE);
        transitionRectangleUserAnswer.startTransition(400);


        //tenho que checar quantas respostas tem
        for(int answerOption : answerViewIds)
        {
            TextView answerView = findViewById(answerOption);
            if(answerView != null)
                answerView.setOnClickListener(null);
        }
        taskResult.putExtra("taskStatus", taskStatus);
        setResult(RESULT_OK, taskResult);

        //2 seconds to go back
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);

    }
}
