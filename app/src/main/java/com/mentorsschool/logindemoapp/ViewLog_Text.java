package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class ViewLog_Text extends AppCompatActivity {

    //Declares fields on the level
    private TextView box;
    private TextView txtName;
    private Button btnGraph;
    private Button btnHome;
    private Integer currentLevel;

    //Declares firebase objects
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private FirebaseAuth firebaseAuth;

    private User u; //Temporary user holding

    //User information
    int lowLevel, highLevel;
    private String nameStr;

    //Bundle for passing user info along
    Bundle b;
    private String uid = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log);

        //Set up firebase objects
        firebaseAuth = FirebaseAuth.getInstance();

        //Sets the objects on the view
        box = findViewById(R.id.txtLogView);
        btnGraph = findViewById(R.id.btnGoToGraph);
        btnHome = findViewById(R.id.btnGoToHome);
        txtName = findViewById(R.id.txtNameSpot);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //Gets the user ID from the bundle if it is passed in, if not get their UID from firebase auth
        b = getIntent().getExtras();
        if(b != null)
            uid = b.getString("uid");
        else
            uid = user.getUid();

        //Finish configuring the firebase info
        mDatabase = FirebaseDatabase.getInstance().getReference("logs").child(uid);
        mUser = FirebaseDatabase.getInstance().getReference("users").child(uid);

        box.setText("");    //Sets the log box to blank

        Query mQueryRef = mDatabase;    //Queries the DB

        mUser.addListenerForSingleValueEvent(new ValueEventListener() { //Listens for a user
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                u = dataSnapshot2.getValue(User.class); //Store the user

                //get info from the user
                lowLevel = u.lowLevel;
                highLevel = u.highLevel;
                nameStr = u.name;

                txtName.setText(nameStr);   //Set their name
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mQueryRef.orderByKey()  //Query the database for the last X number of logs
                .limitToLast(10)    //Last number of logs, can be changed
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Log> logList = new ArrayList<>();     //Arraylist of logs

                for(DataSnapshot ds : dataSnapshot.getChildren()){  //For each log, put it in the array list
                    Log logChild = ds.getValue(Log.class);
                    logList.add(logChild);
                }

                Collections.reverse(logList);   //Reverse the list to show newest logs first

                Iterator<Log> it2 = logList.iterator(); //new iterator from the list
                while(it2.hasNext())    //Loop through the arraylist
                {
                    Log currentLog = it2.next();    //get current log
                    currentLevel = Integer.parseInt(currentLog.level);  //Get the level of the log

                    SpannableStringBuilder str = new SpannableStringBuilder(currentLevel.toString());   //New string builder for fomatting
                    final ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);             //Defines the color red
                    final StyleSpan bold = new StyleSpan(Typeface.BOLD);                            //Defines the bold text


                    if(currentLevel > highLevel) {  //If the current level is above the user's set high limit color it red
                        str.setSpan(red, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else if(currentLevel < lowLevel) {  //If the current level is below the user's set low level, color it red
                        str.setSpan(red, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {

                    }
                    str.setSpan(bold, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //Make the log bold


                    //Append the log to the box
                    box.append(currentLog.dateStr);
                    box.append(" at ");
                    box.append(currentLog.timeStr);
                    box.append("\n");
                    box.append(currentLog.time1);
                    box.append(" ");
                    box.append(currentLog.time2);
                    box.append(" - ");
                    box.append(str);
                    box.append("\n\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //If the user clicks on the graph button
                Intent intent = new Intent(ViewLog_Text.this, ViewLog_Graph.class);     //New intent to go to the graph view

                //Build a bundle to pass the UID
                b = new Bundle();
                b.putString("uid",uid);
                intent.putExtras(b);
                startActivity(intent);  //Go to the graph view
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //If the user clicks the go home button
                startActivity(new Intent(ViewLog_Text.this, Home.class));   //Go to the home view
            }
        });
    }
}