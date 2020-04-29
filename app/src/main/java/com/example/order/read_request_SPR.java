package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class read_request_SPR extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private TextView TV_name_SPR, TV_date_SPR, TV_depart_SPR, TV_subject_SPR, TV_desc_SPR, TV_STATUS_SPR;
    private MaterialCheckBox CB_READ_SPR;
    private String fbkey, getDepart, getName, getDate, getDesc, getSubject, getStatus, getApprove;
    private Query queryRead_SPR;
    private MaterialButton mat_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_request__spr);

        toolbar = findViewById(R.id.toolbar);
        TV_name_SPR = findViewById(R.id.TV_name_SPR);
        TV_date_SPR = findViewById(R.id.TV_date_SPR);
        TV_depart_SPR = findViewById(R.id.TV_depart_SPR);
        TV_desc_SPR = findViewById(R.id.TV_desc_SPR);
        TV_subject_SPR = findViewById(R.id.TV_subject_SPR);
        TV_STATUS_SPR = findViewById(R.id.TV_status_SPR);
        CB_READ_SPR = findViewById(R.id.CB_read_SPR);
        mat_submit = findViewById(R.id.BTN_SPR_submit);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Form");

        fbkey = getIntent().getExtras().getString("id_list_SPR");

        queryRead_SPR = databaseReference.child(fbkey);

        showData();

        mat_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CB_READ_SPR.isChecked()){
                    try {
                        databaseReference.child(fbkey).child("approval").setValue("Approved");
                        Intent intent = new Intent(read_request_SPR.this, admin_Home.class);
                        Toast.makeText(read_request_SPR.this, "Approved", Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        databaseReference.child(fbkey).child("approval").setValue("Disapproved");
                        Intent intent = new Intent(read_request_SPR.this, admin_Home.class);
                        Toast.makeText(read_request_SPR.this, "Canceled", Toast.LENGTH_SHORT).show();
                        startActivity(intent);

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private void showData() {

        queryRead_SPR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getDate = (String) dataSnapshot.child("date").getValue();
                getSubject = (String) dataSnapshot.child("request").getValue();
                getDesc = (String) dataSnapshot.child("description").getValue();
                getDepart= (String) dataSnapshot.child("category").getValue();
                getName = (String) dataSnapshot.child("name").getValue();
                getStatus = (String) dataSnapshot.child("status").getValue();
                getApprove = (String) dataSnapshot.child("approval").getValue();

                TV_date_SPR.setText(getDate);
                TV_depart_SPR.setText(getDepart);
                TV_desc_SPR.setText(getDesc);
                TV_name_SPR.setText(getName);
                TV_subject_SPR.setText(getSubject);

                getApproval();
                getStatusRequest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(read_request_SPR.this, "ERROR: "+databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getStatusRequest() {
        if (getStatus.equals("YES")){
            TV_STATUS_SPR.setText(R.string.status_YES);
        }
        else if (getStatus.equals("NO")){
            TV_STATUS_SPR.setText(R.string.status_NO);
        }
    }

    private void getApproval() {
        if (getApprove.equals("YES") || getApprove.equals("Approved")){
            CB_READ_SPR.setChecked(true);
        }
        else if (getApprove.equals("NO") || getApprove.equals("Disapproved")){
            CB_READ_SPR.setChecked(false);
        }
    }
}
