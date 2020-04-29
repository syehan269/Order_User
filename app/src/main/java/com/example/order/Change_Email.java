package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Change_Email extends AppCompatActivity {

    private FirebaseUser fbAuth;
    private DatabaseReference databaseReference;
    private EditText ET_change_email;
    private Button BTN_change_Email;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private String getEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__email);

        toolbar = findViewById(R.id.toolbar);
        fbAuth = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        ET_change_email = findViewById(R.id.ET_change_Email);
        BTN_change_Email = findViewById(R.id.BTN_CHANGE_EMAIL);
        progressDialog = new ProgressDialog(Change_Email.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BTN_change_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    setProgressDialog();
                    updateEmail();
                    emailDatabase();

            }
        });

    }

    private void updateEmail() {

        fbAuth.updateEmail(getEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
              //              Toast.makeText(Change_Email.this, "Updated !", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Change_Email.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void emailDatabase(){
        final String getEmail = ET_change_email.getText().toString().trim();

        try {

            FirebaseDatabase.getInstance().getReference("user").child(fbAuth.getUid()).child("email")
                    .setValue(getEmail);

            Intent intent = new Intent(Change_Email.this, edit_profile.class);
            startActivity(intent);

            Toast.makeText(Change_Email.this, "Updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setProgressDialog(){
        progressDialog.setTitle("Update");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

}
