package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.util.HashMap;

/**
 * Created by Felipe on 7/25/2018.
 */

public class MissionProgress {
    public static final Integer MISSION_NOT_STARTED = 0;
    public static final Integer MISSION_FINISHED = 1;
    public static final Integer MISSION_IN_PROGRESS = 2;

    public String getMissionId() {
        return missionId;
    }

    public void setMissionId(String missionId) {
        this.missionId = missionId;
    }

    public HashMap<String, Integer> getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(HashMap<String, Integer> taskProgress) {
        this.taskProgress = taskProgress;
    }

//    public Integer getStatus() {
//        return status;
//    }

//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public MissionProgress(String missionId, HashMap<String, Integer> taskProgress){
        this.missionId = missionId;
        this.taskProgress = taskProgress;
        getStatus();
    }

    public Integer getStatus() {
        if(! this.taskProgress.containsValue(AbstractTask.TASK_NOT_STARTED)){
            status = MISSION_FINISHED;
        }else if( this.taskProgress.containsValue(AbstractTask.TASK_COMPLETED)
                ||this.taskProgress.containsValue(AbstractTask.TASK_FAILED)){
            status = MISSION_IN_PROGRESS;
        }else status = MISSION_NOT_STARTED;
        return status;
    }

    private String missionId;
    private HashMap<String, Integer> taskProgress;
    private Integer status;
}
