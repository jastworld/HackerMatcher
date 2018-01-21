package com.hackermatcher.hackermatcher.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hackermatcher.hackermatcher.R;

public class Visualize extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebase_auth_listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualize);

        //Firebase stuff
        firebaseAuth = FirebaseAuth.getInstance();
        firebase_auth_listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    Intent intent = new Intent(Visualize.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        };




    }

    public void viewProfile(View view){
        System.out.println(firebaseAuth.getCurrentUser().getEmail());
        startActivity(new Intent(Visualize.this,Profile.class));
        finish();

    }

    public void logoutV(View view){
        firebaseAuth.signOut();
        FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(Visualize.this,LoginActivity.class));

                }else{
                    Toast.makeText(Visualize.this,"Logout was unsuccessful", Toast.LENGTH_LONG).show();
                }
            }
        };
        firebaseAuth.addAuthStateListener(listener);
    }
}
