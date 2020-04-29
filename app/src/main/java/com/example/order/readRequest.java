package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class readRequest extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView TV_given, TV_desc, TV_subject, TV_date, TV_depart;
    private DatabaseReference databaseReference;
    private String fbUser, fbKey, getGiven, getDepart, getSubject, getDesc, getDate;
    private Query queryRead;
    //private Button ;
    private FloatingActionButton BTN_read_comp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_request);

        toolbar = findViewById(R.id.toolbar);
        TV_depart = findViewById(R.id.TV_read_depart);
        TV_given = findViewById(R.id.TV_read_given);
        TV_desc = findViewById(R.id.TV_read_desc);
        TV_subject = findViewById(R.id.TV_read_subject);
        TV_date = findViewById(R.id.TV_read_date);
        BTN_read_comp = findViewById(R.id.BTN_comp_read);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fbUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Form");

        fbKey = getIntent().getExtras().getString("id_list");

        queryRead = databaseReference.child(fbKey);

        BTN_read_comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        showStatus();

    }

    private void getData(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user")
                .child(FirebaseAuth.getInstance().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String Name = (String) dataSnapshot.child("userName").getValue();
                //Toast.makeText(readRequest.this, Name, Toast.LENGTH_SHORT).show();

                updateStatus(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateStatus(String name) {

        try {
            databaseReference.child(fbKey).child("status").setValue("YES");
            databaseReference.child(fbKey).child("queryHandle").setValue(getDepart + "_YES");
            databaseReference.child(fbKey).child("complete").setValue(name);

            Intent intent = new Intent(readRequest.this, MainActivity.class);
            startActivity(intent);

        } catch (Exception e){
            e.printStackTrace();
        }
        Toast.makeText(readRequest.this, "Updated", Toast.LENGTH_SHORT).show();
    }

    private void showStatus() {

        queryRead.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDepart = (String) dataSnapshot.child("category").getValue();
                getDesc = (String) dataSnapshot.child("description").getValue();
                getGiven = (String) dataSnapshot.child("name").getValue();
                getSubject = (String) dataSnapshot.child("request").getValue();
                getDate = (String) dataSnapshot.child("date").getValue();

                TV_depart.setText(getDepart);
                TV_desc.setText(getDesc);
                TV_given.setText(getGiven);
                TV_subject.setText(getSubject);
                TV_date.setText(getDate);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(readRequest.this, "ERROR: "+ databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}