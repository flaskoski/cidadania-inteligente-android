package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.io.Serializable;

/**
 * Created by Felipe F. Laskoski on 11/25/2017.
 */

public class MissionItem implements Serializable{
    public MissionItem(String missionName, String description, Integer missionIconId) {
        this.missionName = missionName;
        this.missionIconId = missionIconId;
        this.description = description;
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
    private String missionName;
    private Integer missionIconId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;
    private static final Integer NO_IMAGE_PROVIDED = -1;

}
