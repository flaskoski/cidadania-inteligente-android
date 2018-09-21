package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felipe F. Laskoski on 11/25/2017.
 */

public class MissionItem implements Serializable{
    private String missionName;
    private Integer missionIconId; //Not being used
    private String description;
    private String _id;
    private List<String> taskIDs;
    private static final Integer NO_IMAGE_PROVIDED = -1;
    public List<String> getTaskIDs() {
        return taskIDs;
    }
    private Integer status = MISSION_NOT_STARTED;

    public static final Integer MISSION_FINISHED = 1;
    public static final Integer MISSION_NOT_STARTED = 0;
    public static final Integer MISSION_IN_PROGRESS = 2;
    public static final Integer MISSION_FAILED = -1;

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
        if(progress == taskIDs.size()) {
            status = MISSION_FINISHED;
        }
    }
    public void increaseProgress(){
        this.progress++;
        if(progress == taskIDs.size()) {
            status = MISSION_FINISHED;
        }
        else status = MISSION_IN_PROGRESS;
    }
    private Integer progress;

    public String get_id() {
        return _id;
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

    @Override
    public boolean equals(Object obj) {
        return this.get_id().equals(((MissionItem)obj).get_id());
    }

}
