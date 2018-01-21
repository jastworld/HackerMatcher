package com.hackermatcher.hackermatcher.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackermatcher.hackermatcher.R;

public class Welcome extends AppCompatActivity {
    private EditText child;
    private Button addButton;
    private Button removeButton;
    private TextView textView,userProfile;
    private FirebaseAuth firebaseAuth;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        child = (EditText) findViewById(R.id.editName);
        addButton = (Button)findViewById(R.id.addButton);
        removeButton = (Button)findViewById(R.id.removeButton);
        textView = (TextView) findViewById(R.id.textView);
        userProfile =(TextView)findViewById(R.id.profile);
        logout = (Button)findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();

        final DatabaseReference  databaseReference = database.getReference("Person");


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String child_value = child.getText().toString();
                databaseReference.setValue(child_value);
            }
        });

        //userProfile.setText();
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.removeValue();
                /*startActivity(new Intent(Welcome.this,LoginActivity.class));
                finish();
                return;*/
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = String.valueOf(dataSnapshot.getValue());
                textView.setText(text);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void logout(){

        firebaseAuth.signOut();
        FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){
                   startActivity(new Intent(Welcome.this,LoginActivity.class));

                }else{
                    Toast.makeText(Welcome.this,"Logout was unsuccessful", Toast.LENGTH_LONG).show();

                }
            }
        };
        firebaseAuth.addAuthStateListener(listener);
    }
}
