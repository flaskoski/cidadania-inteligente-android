package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felipe on 11/25/2017.
 */

public abstract class AbstractTask implements Serializable{
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
        if(progress == MissionProgress.TASK_COMPLETED) {
            finished = true;
            completed = true;
        }
        else if(progress == MissionProgress.TASK_FAILED)
            finished=true;
    }

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

    public String getType(){return "Tarefa Genérica";}

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }
    public Boolean isFinished(){
        return finished;
    }
    public Boolean isCompleted(){
        return completed;
    }

    //experience points that earns if completes the mission
    private Integer xp=0;
    //mission difficulty (from 1 to 10)
    private Integer difficulty=1;
    //tags to search the mission
    private List<String> tags;
    //Check if the task is available (i.e. open/allowed) for the user to try
    private Boolean available = true;

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }


    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


    @Override
    public boolean equals(Object obj) {
        return this.get_id().equals(((AbstractTask)obj).get_id());
    }

    public abstract Class<?> getActivityClass();

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public abstract int getTypeIcon();
}
