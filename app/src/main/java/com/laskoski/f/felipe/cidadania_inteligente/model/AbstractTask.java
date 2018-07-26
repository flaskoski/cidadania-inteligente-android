package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;

/**
 * Created by Felipe on 11/25/2017.
 */

public abstract class AbstractTask implements Serializable{
    public static final Integer TASK_COMPLETED = 100;
    public static final Integer TASK_NOT_STARTED = 0;
    public static final Integer TASK_FAILED = -1;
    protected String title = "";
    private String _id;
    private Boolean finished = false;
    private Integer progress;
    private Boolean completed = false;

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
        if(progress == TASK_COMPLETED) {
            finished = true;
            completed = true;
        }
        else if(progress == TASK_FAILED)
            finished=true;
    }

    protected String type="Tarefa Genérica";
    public AbstractTask(){}
    public static final int TIMER_OFF = -1;

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String get_id() {
        return _id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
    public Boolean isFinished(){
        return finished;
    }
    public Boolean isCompleted(){
        return completed;
    }
}
