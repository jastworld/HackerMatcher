package com.hackermatcher.hackermatcher.Models;

import android.net.Uri;
import android.support.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jastworld on 1/20/18.
 */
@Keep
@IgnoreExtraProperties
public class Hacker {

    private String first_name;
    private String last_name;
    private String school;
    private String email;
    private String username;

    public Uri getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(Uri imageLocation) {
        this.imageLocation = imageLocation;
    }

    private Uri imageLocation;
    private List<Interest> interests = new ArrayList<>();
    private List<Skill> skills;
    public Hacker() {

    }

    public Hacker(String first_name, String last_name, String school,
                  String email, List<Interest> interests, String username,
                  List<Skill> skills) {
        this.username = username;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.school = school;
        this.interests = interests;
        this.skills = skills;

    }


    public String get_first_name() {
        return first_name;
    }

    public void set_first_name(String first_name) {
        this.first_name = first_name;
    }

    public String get_last_name() {
        return last_name;
    }

    public void set_last_name(String last_name) {
        this.last_name = last_name;
    }

    public String get_school() {
        return school;
    }

    public void set_school(String school) {
        this.school = school;
    }

    public String get_email() {
        return email;
    }

    public void set_email(String email) {
        this.email = email;
    }

    public String get_username() {
        return username;
    }

    public void set_username(String username) {
        this.username = username;
    }

    public List<Interest> getInterests() {
        return interests;
    }

    public void add_interest(Interest interest) {
        interests.add(interest);
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }



}
