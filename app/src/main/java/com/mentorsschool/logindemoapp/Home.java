package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    private TextView name;
    private FirebaseAuth firebaseAuth;

    private Button btnAddLog;
    private Button btnViewLogs;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        firebaseAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.txtName);
        name.setText(firebaseAuth.getCurrentUser().getEmail());

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
    }
}
