package com.hackermatcher.hackermatcher.Models;

import android.support.annotation.Keep;

/**
 * Created by jastworld on 1/20/18.
 */
@Keep
public class Interest {
    private String interest;

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public int getInterestLevel() {
        return interestLevel;
    }

    public void setInterestLevel(int interestLevel) {
        this.interestLevel = interestLevel;
    }

    private int interestLevel;


    public Interest(){
        this.interest = interest;
    }

    public Interest(String interest, int interestLevel){
        this.interest = interest;
        this.interestLevel = interestLevel;
    }
}
