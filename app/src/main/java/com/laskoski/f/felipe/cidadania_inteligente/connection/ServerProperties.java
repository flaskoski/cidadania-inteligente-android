package com.laskoski.f.felipe.cidadania_inteligente.connection;

/**
 * Created by Felipe on 7/31/2018.
 */

public interface ServerProperties {
    public static final String SERVER_ROOT_URL = "http://10.0.2.2:8080/";
    public static final String SERVER_ROOT_SAFE_URL = "https://10.0.2.2:6443/";
    public static final String SERVER_PLAYER_URL = SERVER_ROOT_SAFE_URL+"player/";
    public static final String SERVER_MISSION_PROGRESS_URL = SERVER_ROOT_SAFE_URL+"player/missionProgress";
    public static final String SERVER_TASKS_URL = SERVER_ROOT_SAFE_URL+"tasks";
    public static final String SERVER_ALL_MISSION_PROGRESS_URL = SERVER_ROOT_SAFE_URL+"player/allMissionsProgress";
}
