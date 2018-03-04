package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;

/**
 * Created by Felipe on 11/25/2017.
 */

public abstract class Task implements Serializable{
    protected String title = "";
    public Boolean completed = false;
    String type = "task";

    public Task(){}
    public static final int TIMER_OFF = -1;

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getType(){
        return this.type;
    }
    public void setType(String type){
        this.type = type;
    }

}