package com.hackermatcher.hackermatcher.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hackermatcher.hackermatcher.R;


public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebase_auth_listener;
    private EditText login_email,login_password;
    private Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_email = (EditText)findViewById(R.id.login_email);
        login_password = (EditText)findViewById(R.id.login_password);


        login_button = (Button) findViewById(R.id.login_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebase_auth_listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null) {
                    Intent intent = new Intent(LoginActivity.this, Visualize.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        if(login_email.getText().toString().equals("") || login_password.getText().toString().equals("")){

            Toast.makeText(LoginActivity.this,"Login was unsuccessful: Empty space", Toast.LENGTH_LONG).show();
        }
        else{
            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = login_email.getText().toString();
                    String password = login_password.getText().toString();
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login was unsuccessful", Toast.LENGTH_LONG).show();

                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebase_auth_listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebase_auth_listener);
    }
    public void signup(View view){
        startActivity(new Intent(LoginActivity.this,SignUp.class));

    }
}
