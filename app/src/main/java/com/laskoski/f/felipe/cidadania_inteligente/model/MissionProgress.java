package com.laskoski.f.felipe.cidadania_inteligente.model;

import java.util.HashMap;

/**
 * Created by Felipe on 7/25/2018.
 */

public class MissionProgress {
    public static final Integer MISSION_NOT_STARTED = 0;
    public static final Integer MISSION_FINISHED = 1;
    public static final Integer MISSION_IN_PROGRESS = 2;
    public static final Integer TASK_COMPLETED = 100;
    public static final Integer TASK_NOT_STARTED = 0;
    public static final Integer TASK_FAILED = -1;

    public String getProgressId() {
        return progressId;
    }

    public void setProgressId(String progressId) {
        this.progressId = progressId;
    }

    public HashMap<String, Integer> getTaskProgress() {
        return taskProgress;
    }

    public void setOneTaskProgress(String taskId, Integer progress){
        this.taskProgress.put(taskId, progress);
        updateStatus();
    }

    public void setTaskProgress(HashMap<String, Integer> taskProgress) {
        this.taskProgress = taskProgress;
        updateStatus();
    }

//    public Integer getStatus() {
//        return status;
//    }

//    public void setStatus(Integer status) {
//        this.status = status;
//    }

    public MissionProgress(){}
    public MissionProgress(HashMap<String, Integer> taskProgress){
        this.taskProgress = taskProgress;
        updateStatus();
    }

    public MissionProgress(String progressId, HashMap<String, Integer> taskProgress){
        this.progressId = progressId;
        this.taskProgress = taskProgress;
        updateStatus();
    }
    public MissionProgress(String progressId, HashMap<String, Integer> taskProgress, Double status){
        this.progressId = progressId;
        this.taskProgress = taskProgress;
        this.status = status.intValue();
    }

    private void updateStatus() {
        if(taskProgress!= null)
            if(taskProgress.size() > 0)
                if(! this.taskProgress.containsValue(TASK_NOT_STARTED)){
                    status = MISSION_FINISHED;
                }else if(this.taskProgress.containsValue(TASK_COMPLETED)||
                        this.taskProgress.containsValue(TASK_FAILED) ){
                    status = MISSION_IN_PROGRESS;
                }else status = MISSION_NOT_STARTED;
    }

    public Integer getXp() {
        return xp;
    }

    public void setXp(Integer xp) {
        this.xp = xp;
    }

    private Integer xp;
    private String progressId;
    private HashMap<String, Integer> taskProgress;
    private Integer status;

    public Integer getStatus() {
        updateStatus();
        return status;
    }
}
