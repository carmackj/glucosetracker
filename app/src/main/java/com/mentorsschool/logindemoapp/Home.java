package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private TextView name;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private DatabaseReference mDB;
    private DatabaseReference mType;

    private Button btnAddLog;
    private Button btnViewLogs;
    private Button logout;
    private Button viewPts;

    private String userType;
    private String userID;

    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        firebaseAuth = FirebaseAuth.getInstance();
        mUser = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        mDB = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        viewPts = findViewById(R.id.btnViewPts);

        /*
        mDB.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               userType = dataSnapshot.child("users").child(user.getUid().toString()).child("type").getValue(String.class);
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
        });
        */

        mDB.child("users").child(firebaseAuth.getCurrentUser().getUid()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userType = dataSnapshot.getValue(String.class);

                name = findViewById(R.id.txtName);
                name.setText(user.getEmail());
                name.append(userType);

                if(userType.equals("Doctor"))
                {
                    viewPts.setVisibility(View.VISIBLE);
                }
                else{
                    viewPts.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });














        btnAddLog = findViewById(R.id.btnGoToAddLog);
        btnViewLogs = findViewById(R.id.btnGoToViewLogs);

        btnAddLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ViewLog_Text.this, ViewLog_Graph.class));
                startActivity(new Intent(Home.this, LogGlucoseActivity.class));
            }
        });

        btnViewLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ViewLog_Text.this, ViewLog_Graph.class));
                startActivity(new Intent(Home.this, ViewLog_Text.class));
            }
        });

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(Home.this, MainActivity.class));
            }
        });

        viewPts.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, ViewPatients.class));
            }
        });
    }
}
