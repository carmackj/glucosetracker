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

    private TextView box;
    private Button btnGraph;
    private Button btnHome;
    private Integer currentLevel;

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;

    private FirebaseAuth firebaseAuth;

    int lowLevel, highLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log);


        firebaseAuth = FirebaseAuth.getInstance();

        box = findViewById(R.id.txtLogView);
        btnGraph = findViewById(R.id.btnGoToGraph);
        btnHome = findViewById(R.id.btnGoToHome);

        mDatabase = FirebaseDatabase.getInstance().getReference("logs").child(firebaseAuth.getCurrentUser().getUid());
        mUser = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        user = FirebaseAuth.getInstance().getCurrentUser();


        box.setText("");

        Query mQueryRef = mDatabase;

        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                lowLevel = dataSnapshot2.child("lowLevel").getValue(Integer.class);
                highLevel = dataSnapshot2.child("highLevel").getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mQueryRef.orderByKey()
                .limitToLast(10)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Log> logList = new ArrayList<>();



                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Log logChild = ds.getValue(Log.class);
                    logList.add(logChild);
                }

                //series.appendData(new DataPoint(logNum, Integer.parseInt(currentLog.level)), true, logList.size());

                Collections.reverse(logList);


                Iterator<Log> it2 = logList.iterator();
                while(it2.hasNext())
                {
                    Log currentLog = it2.next();

                    currentLevel = Integer.parseInt(currentLog.level);

                    SpannableStringBuilder str = new SpannableStringBuilder(currentLevel.toString());

                    final ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
                    final StyleSpan bold = new StyleSpan(Typeface.BOLD);



                    if(currentLevel > highLevel) {
                        //str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        str.setSpan(red, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else if(currentLevel < lowLevel) {
                        //str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        str.setSpan(red, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    else {
                        //str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    str.setSpan(bold, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


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




                    /*
                    box.append(currentLog.dateStr);
                    box.append(": ");
                    box.append(currentLog.time1);
                    box.append(" ");
                    box.append(currentLog.time2);
                    box.append(" - ");
                    box.append(currentLog.level);
                    box.append("\n\n");
                    */

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ViewLog_Text.this, ViewLog_Graph.class));
                startActivity(new Intent(ViewLog_Text.this, ViewLog_Graph.class));
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ViewLog_Text.this, ViewLog_Graph.class));
                startActivity(new Intent(ViewLog_Text.this, Home.class));
            }
        });

    }

}
