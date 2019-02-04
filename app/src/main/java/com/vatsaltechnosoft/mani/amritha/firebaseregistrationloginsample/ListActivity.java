package com.vatsaltechnosoft.mani.amritha.firebaseregistrationloginsample;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    FirebaseDatabase database;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<User, userViewHolder> adapter;
    Intent intent;
    String email;
    Button password;
    FirebaseAuth mAuth;
    SharedPreferences prf;
    private RecyclerView recyclerViewUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mAuth = FirebaseAuth.getInstance();

        prf = getSharedPreferences("user_details", MODE_PRIVATE);

        intent = new Intent(ListActivity.this, MainActivity.class);

        initViews();

        initObjects();


        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog openDialog = new Dialog(ListActivity.this);

                openDialog.setContentView(R.layout.activity_my_dialog_fragment);//setting layout for dialog

                openDialog.show();//showing dialog

                //finding views for TextViews

                final EditText updateList = openDialog.findViewById(R.id.changePassword);

                Button saveChanges = openDialog.findViewById(R.id.save_button);

                Button cancel = openDialog.findViewById(R.id.cancel_button);

                //On Click Event for TextViews

                saveChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        user.updatePassword(updateList.getText().toString());

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
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();

                SharedPreferences.Editor editor = prf.edit();
                editor.clear();
                editor.commit();
                startActivity(intent);

                finish();

                break;
        }

        return true;
    }

    private void initViews() {

        password = findViewById(R.id.change_password);
        recyclerViewUsers = findViewById(R.id.recycler_view_users);

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Users");
    }

    private void initObjects() {

        layoutManager = new LinearLayoutManager(this);

        recyclerViewUsers.setLayoutManager(layoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);

        loadData();
    }

    private void loadData() {

        email = prf.getString("email", null);


        adapter = new FirebaseRecyclerAdapter<User, userViewHolder>(User.class,
                R.layout.list_recycler, userViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(userViewHolder viewHolder, final User model, int position) {
                viewHolder.textViewFullName.setText(model.getFirstName() + " " + model.getLastName());

                viewHolder.textViewRadioButton.setText(model.getGender());

                viewHolder.textViewCheckBox.setText(model.getHobbies());

                viewHolder.textViewPhoneNumber.setText(model.getPhoneNumber());

                final User user = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, final int position, boolean isLongClick) {
                        //creating the dialog clicking List View

                        final Dialog openDialog = new Dialog(ListActivity.this);

                        openDialog.setContentView(R.layout.update_delete_layout);//setting layout for dialog

                        openDialog.show();//showing dialog

                        //finding views for TextViews

                        TextView updateList = openDialog.findViewById(R.id.update_text_view);

                        TextView deleteList = openDialog.findViewById(R.id.delete_text_view);

                        //On Click Event for TextViews

                        updateList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(ListActivity.this, DetailsActivity.class);

                                i.putExtra("UserId", adapter.getRef(position).getKey());

                                i.putExtra("email", email);

                                startActivity(i);
                                openDialog.dismiss();

                                notifyDataSetChanged();
                            }
                        });

                        deleteList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(adapter.getRef(position).getKey()).removeValue();

                                openDialog.dismiss();
                            }
                        });

                    }


                });
            }
        };

        recyclerViewUsers.setAdapter(adapter);

    }

}