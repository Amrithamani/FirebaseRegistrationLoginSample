package com.vatsaltechnosoft.mani.amritha.firebaseregistrationloginsample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatButton buttonUpdate;

    AppCompatEditText firstName, lastName, userType, userName, hobbies;
    String textFirst, textLast, textEmail, textUserName, textUserType, textPhoneNumber, gender, textHobbies;

    Intent intent;

    String userId;

    FirebaseDatabase database;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        intent = getIntent();

        initViews();

        initObjects();

        initListeners();

        getValues();

    }

    private void getValues() {

        //Get User Id from intent

        userId = intent.getStringExtra("UserId");


        if (!userId.isEmpty()) {
            getDetailUser();
        }
    }

    private void getDetailUser() {

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                textFirst = user.getFirstName();

                textLast = user.getLastName();

                textUserName = user.getUserName();

                gender = user.getGender();

                textPhoneNumber = user.getPhoneNumber();

                textHobbies = user.getHobbies();

                textUserType = user.getUserType();

                textEmail = user.getEmail();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initListeners() {
        buttonUpdate.setOnClickListener(this);

    }

    private void initObjects() {
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Users");
    }

    private void initViews() {
        firstName = findViewById(R.id.textViewFirstName);

        lastName = findViewById(R.id.textViewLastName);

        userType = findViewById(R.id.textViewuserType);

        userName = findViewById(R.id.textViewUserName);

        hobbies = findViewById(R.id.textViewHobbies);

        buttonUpdate = findViewById(R.id.textViewUpdate);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewUpdate:
                postDataToSQLite();
                finish();
                break;
        }
    }

    private void postDataToSQLite() {
        databaseReference = databaseReference.child(userId);

        User user = new User();

        if (firstName.getText().toString().isEmpty()) {
            user.setFirstName(textFirst);
        } else {
            user.setFirstName(firstName.getText().toString());
        }

        if (lastName.getText().toString().isEmpty()) {
            user.setLastName(textLast);
        } else {
            user.setLastName(lastName.getText().toString());
        }

        if (userName.getText().toString().isEmpty()) {
            user.setUserName(textUserName);
        } else {
            user.setUserName(userName.getText().toString());
        }

        user.setEmail(textEmail);


        if (hobbies.getText().toString().isEmpty()) {
            user.setHobbies(textHobbies);
        } else {
            user.setHobbies(hobbies.getText().toString());
        }

        user.setPhoneNumber(textPhoneNumber);

        user.setGender(gender);

        if (userType.getText().toString().isEmpty()) {
            user.setUserType(textUserType);
        } else {
            user.setUserType(userType.getText().toString());
        }

        databaseReference.setValue(user);

    }
}
