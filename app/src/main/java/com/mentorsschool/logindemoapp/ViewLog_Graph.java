package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static android.app.PendingIntent.getActivity;

public class ViewLog_Graph extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;

    private FirebaseAuth firebaseAuth;


    private Button btnText;
    private Button btnHome;

    int lowLevel, highLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log__graph);


        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("logs").child(firebaseAuth.getCurrentUser().getUid());
        mUser = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid());
        user = FirebaseAuth.getInstance().getCurrentUser();

        Query mQueryRef = mDatabase;

        btnText = findViewById(R.id.btnGoToText);
        btnHome = findViewById(R.id.btnGoToHome);


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
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        ArrayList<Log> logList = new ArrayList<>();
                        GraphView graph = findViewById(R.id.graph);

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Log logChild = ds.getValue(Log.class);
                            logList.add(logChild);
                        }

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                        });

                        LineGraphSeries<DataPoint> low = new LineGraphSeries<>(new DataPoint[]{

                        });

                        LineGraphSeries<DataPoint> high = new LineGraphSeries<>(new DataPoint[]{

                        });


                        Iterator<Log> it1 = logList.iterator();
                        int i = 0;
                        while(it1.hasNext())
                        {
                            Log currentLog = it1.next();
                            series.appendData(new DataPoint(i, Integer.parseInt(currentLog.level)), true, logList.size()+1);
                            low.appendData(new DataPoint(i, lowLevel), true, logList.size()+1);
                            high.appendData(new DataPoint(i, highLevel), true, logList.size()+1);
                            i++;
                        }

                        series.setDrawDataPoints(true);
                        graph.addSeries(series);

                        low.setColor(Color.RED);
                        low.setThickness(3);
                        high.setColor(Color.RED);
                        high.setThickness(3);

                        graph.addSeries(low);
                        graph.addSeries(high);

                        graph.getViewport().setYAxisBoundsManual(true);
                        graph.getViewport().setMinY(40);
                        graph.getViewport().setMaxY(170);
                        graph.getViewport().setScalable(true);
                        //graph.getViewport().setScrollable(true);

                        series.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                Toast.makeText(getApplicationContext(), "Clicked:  "+dataPoint.getY(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        btnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ViewLog_Text.this, ViewLog_Graph.class));
                startActivity(new Intent(ViewLog_Graph.this, ViewLog_Text.class));
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ViewLog_Text.this, ViewLog_Graph.class));
                startActivity(new Intent(ViewLog_Graph.this, Home.class));
            }
        });

}
}
