package com.laskoski.f.felipe.cidadania_inteligente.connection;

import android.content.Context;

import java.util.Properties;

/**
 * Created by Felipe on 7/31/2018.
 */

public class ServerProperties {
    public String SERVER_ROOT_URL = "http://10.0.2.2:8080/";
    public final String SERVER_ROOT_SAFE_URL;// = "https://10.0.2.2:6443/";
    public final String SERVER_MISSION_PROGRESS_UPDATE_URL;
    public final String SERVER_MISSION_PROGRESS_URL;
    public final String SERVER_TASKS_URL;
    public final String SERVER_ALL_MISSION_PROGRESS_URL;
    public final String SERVER_MISSIONS_URL;
    public final String SERVER_IMAGE_URL;
    public final String SERVER_MISSION_IMAGES_URL;
    public final String SERVER_MISSION_ICONS_URL;
    public final String SERVER_PLAYER_INFO_URL;

    private static AssetsPropertyReader assetsPropertyReader;
    private static Properties applicationProperties;

    public ServerProperties(Context context){
        this.assetsPropertyReader = new AssetsPropertyReader(context);
        this.applicationProperties = this.assetsPropertyReader.getProperties("application.properties");
        this.SERVER_ROOT_SAFE_URL = this.applicationProperties.getProperty("server_protocol")+"://"+ this.applicationProperties.getProperty("server_ip") + ":" + this.applicationProperties.getProperty("server_port") + "/";
        this.SERVER_MISSION_PROGRESS_UPDATE_URL = this.SERVER_ROOT_SAFE_URL+"player/updateOne";
        this.SERVER_MISSION_PROGRESS_URL = this.SERVER_ROOT_SAFE_URL+"player/missionProgress";
        this.SERVER_PLAYER_INFO_URL = this.SERVER_ROOT_SAFE_URL+"player";
        this.SERVER_TASKS_URL = this.SERVER_ROOT_SAFE_URL+"tasks";
        this.SERVER_ALL_MISSION_PROGRESS_URL = this.SERVER_ROOT_SAFE_URL+"player/allMissionsProgress";

        this.SERVER_MISSIONS_URL = SERVER_ROOT_SAFE_URL+"myMissions";

        SERVER_IMAGE_URL = SERVER_ROOT_SAFE_URL+"downloadFile/";
        SERVER_MISSION_IMAGES_URL = SERVER_IMAGE_URL+"missionImage";// + "missions/";
        SERVER_MISSION_ICONS_URL = SERVER_IMAGE_URL+"missionIcon";// + "icons/";
    }
}
