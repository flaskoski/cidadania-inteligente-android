package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felipe F. Laskoski on 11/25/2017.
 */

public class MissionItem implements Serializable{

    public List<String> getTaskIDs() {
        return taskIDs;
    }
    public static final Integer MISSION_FINISHED = 1;
    public static final Integer MISSION_NOT_STARTED = 0;
    public static final Integer MISSION_IN_PROGRESS = 2;
    public static final Integer MISSION_FAILED = -1;

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    public Boolean getMandatorySequence() {
        return mandatorySequence;
    }

    public void setMandatorySequence(Boolean mandatorySequence) {
        this.mandatorySequence = mandatorySequence;
    }

    private Boolean mandatorySequence;
    private String missionName;
    private String description;
    private Integer status = MISSION_NOT_STARTED;
    private Integer missionIconId; //Not being used
    private static final Integer NO_IMAGE_PROVIDED = -1;
    private List<String> taskIDs;
    //experience points that earns if completes the mission
    private Integer xp=0;
    //user level needed
    private Integer level=1;
    //mission difficulty (from 1 to 10)
    private Integer difficulty=1;
    //tags to search the mission
    private List<String> tags;
    private String _id;

}
