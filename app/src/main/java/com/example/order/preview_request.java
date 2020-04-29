package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class preview_request extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private Toolbar toolbar;
    private String fbKey, getName, getDesc, getDepart, getDate, getTitle, getApprove, getSatus ;
    private TextView tv_name, tv_depart, tv_date, tv_title, tv_desc, tv_approval, tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_request);

        toolbar = findViewById(R.id.toolbar);
        tv_date = findViewById(R.id.TV_date_req);
        tv_depart = findViewById(R.id.TV_depart_req);
        tv_desc = findViewById(R.id.TV_desc_req);
        tv_name = findViewById(R.id.TV_name_req);
        tv_title = findViewById(R.id.TV_subject_req);
        tv_status = findViewById(R.id.status_read_req);
        tv_approval = findViewById(R.id.approve_read_req);
        fbKey = getIntent().getExtras().getString("id_request");

        databaseReference = FirebaseDatabase.getInstance().getReference("Form").child(fbKey);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Preview");

        setPreview();

    }

    private void setPreview() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getName = (String) dataSnapshot.child("name").getValue();
                getDepart = (String) dataSnapshot.child("category").getValue();
                getApprove = (String) dataSnapshot.child("approval").getValue();
                getDate = (String) dataSnapshot.child("date").getValue();
                getDesc = (String) dataSnapshot.child("description").getValue();
                getSatus = (String) dataSnapshot.child("status").getValue();
                getTitle = (String) dataSnapshot.child("request").getValue();

                //tv_approval.setText(getApprove);
                tv_date.setText(getDate);
                tv_depart.setText(getDepart);
                tv_desc.setText(getDesc);
                tv_title.setText(getTitle);
                //tv_status.setText(getSatus);
                tv_name.setText(getName);

                tv_approval.setText(getApprove);

                if (getSatus.equals("NO")){
                    tv_status.setText(R.string.status_NO);
                }
                else if (getSatus.equals("YES")){
                    tv_status.setText(R.string.status_YES);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
