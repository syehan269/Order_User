package com.example.order;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    private Button login_btn;
    private TextView forget_tv;
    private EditText email_et, password_et;
    private FirebaseAuth fbAuth;
    private DatabaseReference reference, userReference;
    private String getEmail, getPassword;
    private Toolbar toolbar;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_btn = findViewById(R.id.login_btn);
        forget_tv = findViewById(R.id.forget_tv);
        email_et = findViewById(R.id.et_e_mail);
        password_et = findViewById(R.id.et_password);
        toolbar = findViewById(R.id.toolbar);

        fbAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        //Crashlytics.log("APP start");

        //FirebaseApp.initializeApp(this);

        //sessionAccount();

        forget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, forget.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEmail = email_et.getText().toString().trim();
                getPassword = password_et.getText().toString().trim();

                if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)){
                    Toast.makeText(login.this, "Field(s) Empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    progressDialog.setTitle("Login");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    fbAuth.signInWithEmailAndPassword(getEmail, getPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        //Crashlytics.log("login success");
                                        onAuthsuccess(task.getResult().getUser());
                                    }
                                    else {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        sessionAccount();
    }

    private void onAuthsuccess(FirebaseUser user){
        
        if (user != null ){
            reference = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid()).child("level");

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String type = dataSnapshot.getValue(String.class);

                     if (type.equals("User")){

                         //Crashlytics.log("user login as user");
                        startActivity(new Intent(login.this, MainActivity.class));
                        Toast.makeText(login.this, "Welcome", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                     else {
                         progressDialog.dismiss();
                         //Crashlytics.log("user failed causer account is invalid");
                         Toast.makeText(login.this, "Invalid User", Toast.LENGTH_SHORT).show();
                     }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    private void sessionAccount(){

        if (FirebaseAuth.getInstance().getCurrentUser() != null ){
            //Crashlytics.log("User already login");
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            //Toast.makeText(login.this, "LOL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
            alertDialogBuilder.setTitle("Exit")
                    .setMessage("Are you sure ?")
                    .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //FirebaseAuth.getInstance().signOut();
                            //Intent intent = new Intent(MainActivity.this, login.class);
                            //startActivity(intent);

                            moveTaskToBack(true);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alertDialogBuilder.show();
        }

        return super.onKeyDown(keyCode, event);

    }
}
