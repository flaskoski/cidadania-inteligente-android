package com.laskoski.f.felipe.cidadania_inteligente.connection;

/**
 * Created by Felipe on 9/10/2018.
 */

public class ParallelRequestsManager {
//    public static final int NONE = 0;
//    public static final int ONE_OF_TWO = 1;
//    public static final int BOTH = 2;

    Integer numberOfRequestsToFinish;

    public ParallelRequestsManager(int numberOfRequestsToFinish) {
        this.numberOfRequestsToFinish = numberOfRequestsToFinish;
    }
    public  Integer getNumberOfRequestsToFinish() {
        return numberOfRequestsToFinish;
    }
    public void    setNumberOfRequestsToFinish(Integer numberOfRequestsToFinish) {
        this.numberOfRequestsToFinish = numberOfRequestsToFinish;
    }
    public Integer decreaseRemainingRequests() {
        if(numberOfRequestsToFinish > 0)
            numberOfRequestsToFinish--;
        return numberOfRequestsToFinish;
    }
    public Boolean isComplete(){
        return numberOfRequestsToFinish==0;
    }
}
