package com.vatsaltechnosoft.mani.amritha.firebaseregistrationloginsample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener{

    AppCompatTextView textFirst, textLast, textEmail, textUserName, textPhoneNumber, textHobbies;

    AppCompatTextView fullName, gender, userType;

    private AppCompatActivity activity = DetailsActivity.this;

    Intent intent;

    AppCompatButton OK, Edit;

    String userId;

    static String email, intentEmail;

    FirebaseDatabase database ;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //going to previous activity

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        initObjects();

        intent = getIntent();

        getValues();

        initListeners();

    }

    private void initListeners() {
        OK.setOnClickListener(this);
        Edit.setOnClickListener(this);
    }

    private void getValues() {

        //Get User Id from intent

        userId = intent.getStringExtra("UserId");

        intentEmail = intent.getStringExtra("email");

        if (!userId.isEmpty()){
            getDetailUser();
        }

    }

    private void getDetailUser() {
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

//                user.getHobbies();

                textFirst.setText(user.getFirstName());

                textLast.setText(user.getLastName());

                String name = user.getFirstName() + " " + user.getLastName();

                fullName.setText(name);

                textEmail.setText(user.getEmail());

                textUserName.setText(user.getUserName());

                email = user.getEmail();

                textPhoneNumber.setText(user.getPhoneNumber());

                gender.setText(user.getGender());

                userType.setText(user.getUserType());

                textHobbies.setText(user.getHobbies());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initObjects() {

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Users");
    }

    private void initViews() {

        textFirst = findViewById(R.id.text_first_name);

        textLast = findViewById(R.id.text_last_name);

        textEmail = findViewById(R.id.text_email);

        textUserName = findViewById(R.id.text_user_name);

        textPhoneNumber = findViewById(R.id.textViewPhoneText);

        textHobbies = findViewById(R.id.text_hobbies);

        fullName = findViewById(R.id.textViewFullName);

        gender = findViewById(R.id.textViewGender);

        userType = findViewById(R.id.textViewUserType);

        OK = findViewById(R.id.textViewOk);

        Edit = findViewById(R.id.textViewEdit);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.textViewOk:
                finish();
                break;

            case R.id.textViewEdit:
                postDataToSQLite();
                break;
        }
    }

    private void postDataToSQLite() {
        if (intentEmail.matches(email)) {
            Intent intent = new Intent(DetailsActivity.this, EditActivity.class);

            intent.putExtra("UserId", userId);

            startActivity(intent);
        }
    }
}
