package com.laskoski.f.felipe.cidadania_inteligente.model;

import com.laskoski.f.felipe.cidadania_inteligente.activity.LocationTaskActivity;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by Felipe on 11/25/2017.
 */

public class LocationTask extends AbstractTask {
    private String description;
    public static Double SIZE_XLARGE = 0.003;
    public static Double SIZE_LARGE = 0.002;
    public static Double SIZE_DEFAULT = 0.001;
    public static Double SIZE_SMALL = 0.0005;

    /**
     *
     * @param title - Title of the task
     * @param description
     * @param address format location in the map
     */
    public LocationTask(String title, String description, String address, List<String> answers, Integer correctAnswer, Integer timeToAnswer) {
        this.description = description;
        this.title = title;
        this.address = address;
        this.destinationSize = SIZE_DEFAULT;
    }


    /**
     *
     * @param title - Title of the task
     * @param description
     * @param address format location in the map
     */
    public LocationTask(String title, String description, String address) {
        this.description = description;
        this.title = title;
        this.address = address;
        this.destinationSize = SIZE_DEFAULT;

    }

    public LocationTask(){

    }

    private void checkIfParametersAreValid(List<String> answers, Integer correctAnswer){
//        if(answers.size() < 2 || answers.size() > 6 || (correctAnswer < 1 || correctAnswer > 6))
//            throw new InvalidParameterException("Number of answers must be between 2 and 6!");
//        if(correctAnswer > answers.size() )
//            throw new InvalidParameterException("Correct Answer doesn't exist!");
    }
    public String getDescription() {
        return description;
    }

    @Override
    public String getType() {
        return "Localização";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getDestinationSize() {
        return destinationSize;
    }

    public void setDestinationSize(Double destinationSize) {
        this.destinationSize = destinationSize;
    }
    private String address;
    private Double destinationSize;


    @Override
    public Class<?> getActivityClass() {
        return LocationTaskActivity.class;
    }
}
