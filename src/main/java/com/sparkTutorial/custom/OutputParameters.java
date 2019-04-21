package com.sparkTutorial.custom;

import java.io.Serializable;


public class OutputParameters implements Serializable {

    String apiName;
    String accNum;
    String dateTime;
    String corrId;
    String channelName;

    public OutputParameters() {
    }

    public OutputParameters(final String apiName, final String accNum, final String dateTime, final String corrId,
            final String channelName) {
        this.apiName = apiName;
        this.accNum = accNum;
        this.dateTime = dateTime;
        this.corrId = corrId;
        this.channelName = channelName;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(final String apiName) {
        this.apiName = apiName;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(final String accNum) {
        this.accNum = accNum;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(final String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCorrId() {
        return corrId;
    }

    public void setCorrId(final String corrId) {
        this.corrId = corrId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(final String channelName) {
        this.channelName = channelName;
    }

    @Override public String toString() {
        return "OutputParameters{" +
                "apiName='" + apiName + '\'' +
                ", accNum='" + accNum + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", corrId='" + corrId + '\'' +
                ", channelName='" + channelName + '\'' +
                '}';
    }
}
