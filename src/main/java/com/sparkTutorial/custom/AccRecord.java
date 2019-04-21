package com.sparkTutorial.custom;

import java.io.Serializable;
import java.time.LocalDateTime;


public class AccRecord implements Serializable {
    
    String accNum;
    LocalDateTime timeStamp;

    public AccRecord() {
    }

    public AccRecord(final String accNum, final LocalDateTime timeStamp) {
        this.accNum = accNum;
        this.timeStamp = timeStamp;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(final String accNum) {
        this.accNum = accNum;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(final LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override public String toString() {
        return "{" +
                "accNum='" + accNum + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
