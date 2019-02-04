package com.vatsaltechnosoft.mani.amritha.firebaseregistrationloginsample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * Created by Amritha on 7/27/18.
 */
public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = SignUp.this;

    NestedScrollView nestedScrollview;
    AppCompatEditText firstName, lastName, emailId, userName, passwordEditText, confirmPassword, phoneNumber;
    AppCompatRadioButton maleRadioButton, femaleRadioButton;
    AppCompatSpinner userType;
    RadioGroup radioGroup;
    AppCompatCheckBox readingBookHobbies, listeningMusicHobbies, sportsHobbies;


    AppCompatButton appCompatButtonRegister;
    AppCompatTextView appCompatTextViewLoginLink;
    Intent i;
    //our database reference object

    private String selectedItemText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.signup);

        //going to previous activity

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        initViews();

        initListeners();
    }

    private void initViews() {

        nestedScrollview = findViewById(R.id.nestedScrollView);

        firstName = findViewById(R.id.first_name_editText);

        lastName = findViewById(R.id.last_name_editText);

        emailId = findViewById(R.id.email_id_editText);

        userName = findViewById(R.id.user_name_editText);

        passwordEditText = findViewById(R.id.password_editText);

        confirmPassword = findViewById(R.id.confirm_password_editText);

        phoneNumber = findViewById(R.id.phone_number_editText);

        radioGroup = findViewById(R.id.group_id);

        maleRadioButton = findViewById(R.id.male_gender_radioButton);

        femaleRadioButton = findViewById(R.id.female_gender_radioButton);

        userType = findViewById(R.id.userType_spinner);

        readingBookHobbies = findViewById(R.id.reading_hobbies_checkbox);

        listeningMusicHobbies = findViewById(R.id.music_hobbies_checkbox);

        sportsHobbies = findViewById(R.id.sport_hobbies_checkbox);

        appCompatButtonRegister = findViewById(R.id.submit_button);

        appCompatTextViewLoginLink = findViewById(R.id.login_link);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.user_type, R.layout.support_simple_spinner_dropdown_item);

        userType.setAdapter(adapter);

        userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void initListeners() {

        appCompatButtonRegister.setOnClickListener(this);

        appCompatTextViewLoginLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.submit_button:
                postDataToSQLite();
                break;

            case R.id.login_link:
                finish();
                break;
        }
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */

    private void postDataToSQLite() {

        String FirstName = null, LastName = null, email = null, UserName = null,
                password = null, ConfirmPassword = null, PhoneNumber = null;

        FirstName = firstName.getText().toString().trim();

        LastName = lastName.getText().toString().trim();

        email = emailId.getText().toString().trim();

        UserName = userName.getText().toString().trim();

        password = passwordEditText.getText().toString().trim();

        ConfirmPassword = confirmPassword.getText().toString().trim();

        PhoneNumber = phoneNumber.getText().toString();

        if (FirstName.isEmpty()) {
            firstName.setError("Please provide First Name");
            firstName.requestFocus();
            return;
        }

        if (LastName.isEmpty()) {
            lastName.setError("Please provide First Name");
            lastName.requestFocus();
            return;
        }

        if (UserName.isEmpty()) {
            userName.setError("Please provide First Name");
            userName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            emailId.setError("Please provide a Email");
            emailId.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Please provide a Password");
            passwordEditText.requestFocus();
            return;
        }

        if (ConfirmPassword.isEmpty()) {
            confirmPassword.setError("Please provide a Confirm Password");
            confirmPassword.requestFocus();
            return;
        }

        if (!password.matches(ConfirmPassword)) {
            confirmPassword.setError("Password does not match");
            confirmPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError("please enter a valid email");
            emailId.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Minimum length of password should be 6");
            passwordEditText.requestFocus();
            return;
        }

        if (PhoneNumber.isEmpty()) {
            phoneNumber.setError("Please provide a Phone Number");
            phoneNumber.requestFocus();
            return;
        }

        if (PhoneNumber.length() < 10) {
            phoneNumber.setError("Enter a valid Phone number");
            phoneNumber.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User();

                            user.setFirstName(firstName.getText().toString().trim());
                            user.setLastName(lastName.getText().toString().trim());
                            user.setEmail(emailId.getText().toString());
                            user.setUserName(userName.getText().toString().trim());
                            user.setPhoneNumber(phoneNumber.getText().toString());

                            // get selected radio button from radioGroup
                            int selectedId = radioGroup.getCheckedRadioButtonId();

                            // find the radiobutton by returned id
                            RadioButton radioButton = findViewById(selectedId);

                            user.setGender(radioButton.getText().toString());

                            user.setUserType(selectedItemText);

                            if (readingBookHobbies.isChecked()) {
                                user.setHobbies(readingBookHobbies.getText().toString());
                            }

                            if (listeningMusicHobbies.isChecked()) {
                                user.setHobbies(listeningMusicHobbies.getText().toString());
                            }
                            if (sportsHobbies.isChecked()) {
                                user.setHobbies(sportsHobbies.getText().toString());
                            }

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(activity, "User Registered successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(activity, "You are already registered. ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }

}