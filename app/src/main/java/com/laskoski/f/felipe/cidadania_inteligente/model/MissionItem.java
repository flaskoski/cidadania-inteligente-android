package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felipe F. Laskoski on 11/25/2017.
 */

public class MissionItem implements Serializable{
    private String missionName;
    private Integer missionIconId;
    private String description;
    private List<String> taskIDs;
    private static final Integer NO_IMAGE_PROVIDED = -1;
    public List<String> getTaskIDs() {
        return taskIDs;
    }
    public void setTaskIDs(List<String> taskIDs) {
        this.taskIDs = taskIDs;
    }

    public MissionItem(){}
    public MissionItem(String missionName, String description, Integer missionIconId) {
        this.missionName = missionName;
        this.missionIconId = missionIconId;
        this.description = description;
    }
    public MissionItem(String missionName, String description, Integer missionIconId, List<String> taskIDs) {
        this.missionName = missionName;
        this.missionIconId = missionIconId;
        this.description = description;
        this.taskIDs = taskIDs;
    }
    public MissionItem(String missionName, String description) {
        this.missionName = missionName;
        this.description = description;
        this.missionIconId = NO_IMAGE_PROVIDED;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public int getMissionIconId() {
        return missionIconId;
    }

    public void setMissionIconId(Integer missionIcon) {
        this.missionIconId = missionIcon;
    }

    public boolean hasImage(){
        return this.getMissionIconId() != NO_IMAGE_PROVIDED;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
