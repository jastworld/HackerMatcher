package com.hackermatcher.hackermatcher.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hackermatcher.hackermatcher.Controller.HackerMatcherController;
import com.hackermatcher.hackermatcher.Models.Hacker;
import com.hackermatcher.hackermatcher.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Profile extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener firebase_auth_listener;
    private TextView pname,pemail,pschool,pusername,pinterest,pskills;
    FirebaseStorage mStorage;
    StorageReference storageReference;
    //Variables
    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;
    private String imageURL;
    Hacker hacker = HackerMatcherController.getInstance();
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Firebase stuff
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebase_auth_listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user==null) {
                    Intent intent = new Intent(Profile.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        pname = (TextView)findViewById(R.id.my_name);
        pemail = (TextView)findViewById(R.id.my_email);
        pschool = (TextView)findViewById(R.id.my_school);
        pusername = (TextView)findViewById(R.id.my_username);
        pinterest = (TextView)findViewById(R.id.my_interests);
        pskills = (TextView)findViewById(R.id.my_school);
        btnChoose = (Button)findViewById(R.id.choose_image);
        btnUpload = (Button)findViewById(R.id.upload_image);
        imageView = (ImageView)findViewById(R.id.avatar);



        final String user_id = firebaseAuth.getCurrentUser().getUid();


        //DatabaseReference user_db = FirebaseDatabase.getInstance().getReference().child("Hackers").child(user_id);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Hackers");//.orderByChild("username").equalTo(user_id);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Map user = (Map) ds.getValue();
                    if(firebaseAuth.getCurrentUser().getUid().equals(user.get("UUID"))){
                        Glide.with(getApplicationContext()).load(user.get("image_location")).into(imageView);
                        pname.setText(user.get("first_name")+" "+user.get("last_name"));
                        pemail.setText(firebaseAuth.getCurrentUser().getEmail()+"");
                        pschool.setText(user.get("school_name")+"");
                        pinterest.setText("Doing stuff");
                        pusername.setText(user.get("school_name")+"");
                        pskills.setText("karate");
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });




    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ firebaseAuth.getCurrentUser().getUid().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            final Uri downloadUri = taskSnapshot.getDownloadUrl();

                            DatabaseReference hackerRef = databaseReference.getRef();
                            hackerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    hacker.setImageLocation(downloadUri);

                                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                        Map user = (Map) ds.getValue();
                                        if(firebaseAuth.getCurrentUser().getUid().equals(user.get("UUID"))){
                                            user.put("image_location",hacker.getImageLocation().toString());
                                            FirebaseDatabase.getInstance().getReference().child("Hackers").child(firebaseAuth.getCurrentUser().getUid()).updateChildren(user);
                                        }
                                    }
                                    /////
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            Toast.makeText(Profile.this, "Upload", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Profile.this, "Failed "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+ (int)progress);
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData()!= null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                //imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
