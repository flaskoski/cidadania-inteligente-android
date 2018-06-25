package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;

/**
 * Created by Felipe on 11/25/2017.
 */

public abstract class AbstractTask implements Serializable{
    protected String title = "";
    private Boolean completed = false;
    protected String type="Tarefa Genérica";
    public AbstractTask(){}
    public static final int TIMER_OFF = -1;

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

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    public Boolean isCompleted(){
        return completed;
    }
}
