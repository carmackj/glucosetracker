package com.mentorsschool.logindemoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText userName, userPassword, userEmail, lowLevel, highLevel;
    private Button regButton;
    private CompoundButton switchType;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private String user_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    //upload data to the database
                    //String user_name = userName.getText().toString().trim();
                    String user_email = userEmail.getText().toString().trim();
                    String user_password = userPassword.getText().toString().trim();


                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                FirebaseDatabase database = FirebaseDatabase.getInstance();     //Sets up the Database
                                //DatabaseReference users = database.getReference("users").child(firebaseAuth.getCurrentUser().getUid());
                                DatabaseReference users = database.getReference("users");
                                DatabaseReference doctors = database.getReference("doctors");

                                String user_name = userName.getText().toString().trim();
                                int user_lowLevel = Integer.parseInt(lowLevel.getText().toString().trim());
                                int user_highLevel = Integer.parseInt(highLevel.getText().toString().trim());

                                if(switchType.isChecked()) {
                                    user_type = "Doctor";
                                    doctors.child(firebaseAuth.getCurrentUser().getUid()).setValue(new Doctor(user_name));
                                }
                                else
                                    user_type="Patient";

                                users.child(firebaseAuth.getCurrentUser().getUid()).setValue(new User(user_name, user_type, user_lowLevel, user_highLevel));

                                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });
    }

    private void setupUIViews()
    {
        userName = (EditText)findViewById(R.id.etUserName);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        regButton= (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
        switchType = findViewById(R.id.swType);
        lowLevel = findViewById(R.id.txtLowLevel);
        highLevel = findViewById(R.id.txtHighLevel);

    }

    private Boolean validate()
    {
        Boolean result = false;
        String name = userName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty())
        {
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }
        return result;
    }
}
