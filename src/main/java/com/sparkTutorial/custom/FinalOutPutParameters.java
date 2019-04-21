package com.sparkTutorial.custom;

import java.io.Serializable;


public class FinalOutPutParameters implements Serializable {
    
    String accNum;
    Integer frequency;
    Integer minTimeGap;
    Integer maxTimeGap;

    public FinalOutPutParameters() {
    }

    public FinalOutPutParameters(final String accNum, final Integer minTimeGap, final Integer maxTimeGap, final Integer frequency) {
        this.accNum = accNum;
        this.minTimeGap = minTimeGap;
        this.maxTimeGap = maxTimeGap;
        this.frequency = frequency;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(final String accNum) {
        this.accNum = accNum;
    }

    public Integer getMinTimeGap() {
        return minTimeGap;
    }

    public void setMinTimeGap(final Integer minTimeGap) {
        this.minTimeGap = minTimeGap;
    }

    public Integer getMaxTimeGap() {
        return maxTimeGap;
    }

    public void setMaxTimeGap(final Integer maxTimeGap) {
        this.maxTimeGap = maxTimeGap;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(final Integer frequency) {
        this.frequency = frequency;
    }

    @Override public String toString() {
        return "{" +
                "accNum='" + accNum + '\'' +
                ", freq=" + frequency +
                ", minTimeGap=" + minTimeGap +
                ", maxTimeGap=" + maxTimeGap +
                '}';
    }
}
