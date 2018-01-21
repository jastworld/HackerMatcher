package com.hackermatcher.hackermatcher.Models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jastworld on 1/20/18.
 */

public class Team {
    private final String TAG = "DebugHackerTeam";

    private List<String> hackerIDs;
    private String teamName;
    private int teamID;
    private String hackathonID;
    private int maxHackers;
    private String teamDescription;
    private Date date_created;

    public Team(int teamID, String teamName, List<Hacker> hackers, int maxHackers, String hackathonID) {
        this.teamID = teamID;
        this.teamName = teamName;
        hackerIDs = new ArrayList<String>();
        this.maxHackers = maxHackers;
        this.hackathonID = hackathonID;
    }



    public boolean addHacker(String hackerID){
        if(hackerIDs.size()>=maxHackers){
            hackerIDs.add(hackerID);
            return true;
        }else{
            return false;
        }

    }
}
