package com.hackermatcher.hackermatcher.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.hackermatcher.hackermatcher.Models.Hacker;

import java.util.List;

/**
 * Created by jastworld on 1/20/18.
 */

public class HackerMatcherController {

    final static Hacker hacker = new Hacker();

    public static Hacker getInstance() {return hacker;}
}
