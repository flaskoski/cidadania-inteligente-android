package com.laskoski.f.felipe.cidadania_inteligente.featureAdmin;

import android.content.Context;
import android.util.Log;

import com.laskoski.f.felipe.cidadania_inteligente.connection.AssetsPropertyReader;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by Felipe on 10/8/2018.
 * Uses application.properties
 */

public class ToggleRouter implements Serializable {
    Hashtable<String, Boolean> featureConfig = new Hashtable<>();
    private static Properties applicationProperties;


    public ToggleRouter(Context context){
        AssetsPropertyReader assetsPropertyReader = new AssetsPropertyReader(context);
        this.applicationProperties = assetsPropertyReader.getProperties("application.properties");
    }

    public void setAllFeatures(){
        Enumeration<?> e = applicationProperties.propertyNames();
        while (e.hasMoreElements()) {
            String key = (String) e.nextElement();
            if(key.startsWith("feature.")) {
                Boolean value = Boolean.valueOf(applicationProperties.getProperty(key));
                featureConfig.put(key, value);
            }
        }
    }

    void setFeature(String feature, Boolean isEnabled){
        featureConfig.put(feature, isEnabled);
    }
    Boolean featureIsEnabled(String feature){
        return featureConfig.get(feature);
    }
}
