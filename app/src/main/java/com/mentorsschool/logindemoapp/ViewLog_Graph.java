package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    //Declare variables for the view
    private TextView txtName;
    private Button btnText;
    private Button btnHome;

    //Declare firebase objects
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUser;
    private FirebaseAuth firebaseAuth;

    private User u;             //Temporary user
    private String nameStr;     //Patient's name

    Bundle b;   //Bundle to store the user's information for passing

    //Temporary values
    private String uid = null;
    int lowLevel, highLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //create the view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log__graph);

        //Set up values from the view
        txtName = findViewById(R.id.txtNameSpot2);
        btnText = findViewById(R.id.btnGoToText);
        btnHome = findViewById(R.id.btnGoToHome);

        //Set up firebase objects
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        //Get the user ID from the intent bundle if it is sent
        b = getIntent().getExtras();
        if(b != null)
            uid = b.getString("uid");
        else
            uid = user.getUid();

        //Finish setting up firebase objects using the uid
        mDatabase = FirebaseDatabase.getInstance().getReference("logs").child(uid);
        mUser = FirebaseDatabase.getInstance().getReference("users").child(uid);


        Query mQueryRef = mDatabase;    //Query the database for later

        mUser.addListenerForSingleValueEvent(new ValueEventListener() { //Listen to the database to get user info
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                u = dataSnapshot2.getValue(User.class); //Story the user temporarily

                //Pull info from the user
                lowLevel = u.lowLevel;
                highLevel = u.highLevel;
                nameStr = u.name;

                txtName.setText(nameStr);   //Set their name
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mQueryRef.orderByKey()  //Query the database for the last 10 logs
                .limitToLast(10)    //Can be changed
                .addListenerForSingleValueEvent(new ValueEventListener() {  //Listen to the database
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        ArrayList<Log> logList = new ArrayList<>(); //Declare a new list of logs
                        GraphView graph = findViewById(R.id.graph); //Find the graph view

                        for(DataSnapshot ds : dataSnapshot.getChildren()){  //For each log it pulls back, add it to the Arraylist
                            Log logChild = ds.getValue(Log.class);
                            logList.add(logChild);
                        }

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{  //Declare a line graph for the log series

                        });

                        LineGraphSeries<DataPoint> low = new LineGraphSeries<>(new DataPoint[]{     //Declare a line graph for the low level line

                        });

                        LineGraphSeries<DataPoint> high = new LineGraphSeries<>(new DataPoint[]{    //Declare a line graph for the high level line

                        });


                        Iterator<Log> it1 = logList.iterator(); //Declare an iterator for the logList
                        int i = 0;
                        while(it1.hasNext())    //Loop through the logs
                        {
                            Log currentLog = it1.next();    //get the log
                            series.appendData(new DataPoint(i, Integer.parseInt(currentLog.level)), true, logList.size()+1);    //Set the log value on its line
                            low.appendData(new DataPoint(i, lowLevel), true, logList.size()+1);     //Set a point for the low level at the same X value
                            high.appendData(new DataPoint(i, highLevel), true, logList.size()+1);   //Set a point for the high level at the same X value
                            i++;
                        }

                        series.setDrawDataPoints(true);     //Set the graph to draw data points for the logs
                        graph.addSeries(series);            //Adds the logs to the graph

                        //Set the line color and thickness for the high and low level lines
                        low.setColor(Color.RED);
                        low.setThickness(3);
                        high.setColor(Color.RED);
                        high.setThickness(3);

                        //Add the high and low level lines to the graph
                        graph.addSeries(low);
                        graph.addSeries(high);

                        graph.getViewport().setYAxisBoundsManual(true); //Set the graph to manual Y axis to allow for manual control over sizing
                        graph.getViewport().setMinY(40);                //Set the minimum Y value to be shown
                        graph.getViewport().setMaxY(170);               //Set the maximum Y value to be shown
                        graph.getViewport().setScalable(true);          //Set the graph to be scalable
                        //graph.getViewport().setScrollable(true);      //Set the graph to be scrollable
                        

                        series.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {    //If the user clicks on a data point, show it in a Toast
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
            public void onClick(View view) {    //If the user click he text button, go back to that view
                Intent intent = new Intent(ViewLog_Graph.this, ViewLog_Text.class);     //New intent to go back

                //Puts the user's ID in a bundle and pass it along in the intent
                b = new Bundle();
                b.putString("uid",uid);
                intent.putExtras(b);
                startActivity(intent);  //Go to the text view

            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //If the user clicks to go home
                startActivity(new Intent(ViewLog_Graph.this, Home.class));
            }
        });

}
}
