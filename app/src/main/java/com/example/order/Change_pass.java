package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Change_pass extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText Et_pass;
    private Button btn_confirm;
    private String getPass;
    private FirebaseUser getUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        toolbar = findViewById(R.id.toolbar);
        Et_pass = findViewById(R.id.ET_change_Password);
        btn_confirm = findViewById(R.id.BTN_CHANGE_PASS);
        getPass = Et_pass.getText().toString().trim();
        getUser = FirebaseAuth.getInstance().getCurrentUser();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change password");

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    updatePassword();
                    authPass();

            }
        });

    }

    private void authPass() {
        final String getPass = Et_pass.getText().toString().trim();

        getUser.updatePassword(getPass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(Change_pass.this, "Updated", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Change_pass.this, "Failed updating", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassword() {
      final String getPas = Et_pass.getText().toString().trim();

        try {
            FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance()
                    .getCurrentUser().getUid()).child("pass").setValue(getPas);

            Intent intent = new Intent(Change_pass.this, edit_profile.class);
            startActivity(intent);
            finish();
            Toast.makeText(Change_pass.this, "Updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
