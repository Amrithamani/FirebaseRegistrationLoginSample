package com.vatsaltechnosoft.mani.amritha.firebaseregistrationloginsample;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final AppCompatActivity activity = MainActivity.this;
    Intent i;
    FirebaseAuth firebaseAuth;
    NestedScrollView nestedScrollView;

    AppCompatButton login;

    TextInputEditText emailEditText;

    TextInputEditText passwordEditText;

    TextInputLayout userLayout;

    TextInputLayout passwordLayout;

    AppCompatTextView registerText;

    FirebaseDatabase database;

    DatabaseReference databaseReference;

    User user;

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        user = new User();

        database = FirebaseDatabase.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference = database.getReference().child("Users");

        initViews();

        initObjects();
    }

    private void initViews() {

        nestedScrollView = findViewById(R.id.nestedScrollView);

        emailEditText = findViewById(R.id.login_email_editText);

        passwordEditText = findViewById(R.id.login_password_editText);

        login = findViewById(R.id.login_submit_button);

        userLayout = findViewById(R.id.textInputLayoutUserEmail);

        passwordLayout = findViewById(R.id.textInputLayoutPassword);

        registerText = findViewById(R.id.signup_button);

        pref = getSharedPreferences("user_details", MODE_PRIVATE);

    }

    private void initObjects() {


    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.login_submit_button:
                verifyFromFireBase();
                break;

            case R.id.signup_button:
                i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
                break;

            case R.id.password_button:
                missPassword();
                break;

        }
    }

    private void missPassword() {

        final Dialog openDialog = new Dialog(activity);

        openDialog.setContentView(R.layout.forget_password);//setting layout for dialog

        openDialog.show();//showing dialog

        //finding views for TextViews

        final EditText verifyEmail = openDialog.findViewById(R.id.email_password);

        Button saveChanges = openDialog.findViewById(R.id.save_new_button);

        Button cancel = openDialog.findViewById(R.id.cancel_new_button);

        //On Click Event for TextViews

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.sendPasswordResetEmail(verifyEmail.getText().toString()).addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity, "Reset successful", Toast.LENGTH_SHORT).show();
                    }
                });

                openDialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog.dismiss();
            }
        });

    }

    private void verifyFromFireBase() {
        String email = null, password = null;

        email = emailEditText.getText().toString().trim();

        password = passwordEditText.getText().toString().trim();

        if (email.isEmpty()) {
            emailEditText.setError("Please provide a Email");
            emailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passwordEditText.setError("Please provide a Password");
            passwordEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("please enter a valid email");
            emailEditText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Minimum length of password should be 6");
            passwordEditText.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", emailEditText.getText().toString().trim());
                    editor.commit();

                    i = new Intent(MainActivity.this, ListActivity.class);

                    if (pref.contains("email")) {
                        startActivity(i);
                    }

                } else {
                    Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
