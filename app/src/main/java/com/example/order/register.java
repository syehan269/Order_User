package com.example.order;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class register extends AppCompatActivity implements View.OnClickListener {

    private EditText etPass, etEmail, etUsername;
    private FirebaseAuth fbAuth;
    private Button register_btn;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;
    private ArrayAdapter<String> adapter;
    private DatabaseReference databaseReference;
    private String[] levelUser = new String[]{"Admin", "Super User", "User"};
    private ArrayList<String> departString = new ArrayList<String>();
    private MaterialBetterSpinner SPN_level, SPN_regis;

    private static final String Tag = register.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fbAuth = FirebaseAuth.getInstance();
        register_btn =  findViewById(R.id.btn_register);
        etPass = findViewById(R.id.et_pass);
        etUsername = findViewById(R.id.et_user);
        etEmail = findViewById(R.id.et_e_mail);
        SPN_regis = findViewById(R.id.spn_register);
        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        toolbar = findViewById(R.id.toolbar);
        SPN_level = findViewById(R.id.level_spin);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ArrayAdapter<String>(register.this, android.R.layout.simple_spinner_dropdown_item, levelUser);
        SPN_level.setAdapter(adapter);

        listCategory();

        register_btn.setOnClickListener(this);

    }

    private  void  registerUser(){
        final String username = etUsername.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPass.getText().toString().trim();
        final String depart = SPN_regis.getText().toString();
        final String level = SPN_level.getText().toString();
        //final String confirm = etConfirm.getText().toString().trim();
        final String type = "user";

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "Field Empty",Toast.LENGTH_SHORT ).show();
        }

        else {

            progressDialog.setTitle("Register ");
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();

            fbAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                //jika register berhasil
                                userList user = new userList(username, email, password, depart, level);
                                FirebaseDatabase.getInstance().getReference("user")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            subToTopic();
                                            Toast.makeText(register.this, "Register Success", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(register.this, adminControl.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                            }
                            else{
                                progressDialog.dismiss();
                            }
                        }

                    });

        }

    }

    private void listCategory() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Type");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String category = dataSnapshot1.child("Category").getValue().toString();
                    departString.add(category);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(register.this, R.layout.support_simple_spinner_dropdown_item, departString);
                SPN_regis.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(register.this, "ERROR reg "+ databaseError, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void subToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("T_Account")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(register.this, "Topic SUB", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick (View view) {

        if (view == register_btn){
            registerUser();
        }

    }

}
