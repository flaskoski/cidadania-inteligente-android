package com.laskoski.f.felipe.cidadania_inteligente.model;


import java.util.HashMap;
import java.util.Hashtable;

public class Player {

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public HashMap<String, MissionProgress> getMissions() {
        return missions;
    }

    public void setMissions(HashMap<String, MissionProgress> missions) {
        this.missions = missions;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Player(){}
    public Player(String firebaseId){
        this.firebaseId = firebaseId;
    }


    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    private Integer level;
    private String _id;
    private Integer xp;
    private String firebaseId;
    private String username;
    private HashMap<String, MissionProgress> missions;

    public Integer addXp(Integer xpWon) {
        this.xp += xpWon;
        return this.xp;
    }
}
