package com.sparkTutorial.custom;

import java.io.Serializable;


public class AvgParameters implements Serializable {
    
    int freqOfAccount;
    long minTimeGap;
    long maxTimeGap;

    public AvgParameters() {
    }

    public AvgParameters(final int freqOfAccount, final long minTimeGap, final long maxTimeGap) {
        this.freqOfAccount = freqOfAccount;
        this.minTimeGap = minTimeGap;
        this.maxTimeGap = maxTimeGap;
    }

    public int getFreqOfAccount() {
        return freqOfAccount;
    }

    public void setFreqOfAccount(final int freqOfAccount) {
        this.freqOfAccount = freqOfAccount;
    }

    public long getMinTimeGap() {
        return minTimeGap;
    }

    public void setMinTimeGap(final long minTimeGap) {
        this.minTimeGap = minTimeGap;
    }

    public long getMaxTimeGap() {
        return maxTimeGap;
    }

    public void setMaxTimeGap(final long maxTimeGap) {
        this.maxTimeGap = maxTimeGap;
    }

    @Override public String toString() {
        return "AvgParameters{" +
                "freqOfAccount=" + freqOfAccount +
                ", minTimeGap=" + minTimeGap +
                ", maxTimeGap=" + maxTimeGap +
                '}';
    }
}
