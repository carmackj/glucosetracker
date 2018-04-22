package com.mentorsschool.logindemoapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ViewPatients extends AppCompatActivity {


    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private DatabaseReference mUsers;
    private FirebaseAuth firebaseAuth;
    Query mQueryRef;

    private TextView box;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);

        mDatabase = FirebaseDatabase.getInstance().getReference("logs");
        mUsers = FirebaseDatabase.getInstance().getReference("users");

        box = findViewById(R.id.txtBox);
        box.setText("");

        mQueryRef = mUsers;

        mQueryRef.orderByKey()
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<User> userList = new ArrayList<>();

                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            User user = ds.getValue(User.class);
                            userList.add(user);
                        }

                        Iterator<User> it2 = userList.iterator();
                        while(it2.hasNext())
                        {
                            User currentUser = it2.next();

                            box.append(currentUser.name+"\n");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
