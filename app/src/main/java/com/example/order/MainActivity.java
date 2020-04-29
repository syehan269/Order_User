package com.example.order;

import android.app.ProgressDialog;
            import android.content.Context;
            import android.content.DialogInterface;
            import android.content.Intent;
            import androidx.annotation.NonNull;
            import com.google.android.gms.tasks.OnCompleteListener;
            import com.google.android.gms.tasks.OnFailureListener;
            import com.google.android.gms.tasks.OnSuccessListener;
            import com.google.android.gms.tasks.Task;
            import com.google.android.material.bottomnavigation.BottomNavigationView;
            import com.google.android.material.dialog.MaterialAlertDialogBuilder;
            import com.google.firebase.FirebaseApp;
            import com.google.firebase.auth.FirebaseAuth;
            import com.google.firebase.auth.FirebaseUser;
            import com.google.firebase.database.DataSnapshot;
            import com.google.firebase.database.DatabaseError;
            import com.google.firebase.database.DatabaseReference;
            import com.google.firebase.database.FirebaseDatabase;
            import com.google.firebase.database.ValueEventListener;
            import com.google.firebase.iid.FirebaseInstanceId;
            import com.google.firebase.iid.InstanceIdResult;
            import com.google.firebase.messaging.FirebaseMessaging;

            import androidx.appcompat.app.AlertDialog;
            import androidx.appcompat.widget.Toolbar;
            import androidx.fragment.app.FragmentTransaction;
            import androidx.appcompat.app.AppCompatActivity;
            import androidx.fragment.app.Fragment;

            import android.nfc.Tag;
            import android.os.Bundle;
            import android.util.Log;
            import android.view.KeyEvent;
            import android.view.LayoutInflater;
            import android.view.Menu;
            import android.view.MenuInflater;
            import android.view.MenuItem;
            import android.view.View;
            import android.widget.Toast;
            import java.util.HashMap;
            import java.util.Map;

public class MainActivity extends AppCompatActivity {

        private Toolbar toolbar;
        private FirebaseAuth.AuthStateListener authStateListener;
        private BottomNavigationView bottomNavigationView;
        private FirebaseUser user;
        private ProgressDialog progressDialog;
        private MaterialAlertDialogBuilder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        user = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        //menampilkan halaman fragment baru
        getFragmentpage(new home());

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User.ver");

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null){
                }
            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        getToken();
        subToTopic();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){
  //          back_dialog();

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

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_bar, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.about:
                // about
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
                LayoutInflater inflater =LayoutInflater.from(this);
                View view = inflater.inflate(R.layout.dialog_about, null);
                alertDialogBuilder.setView(view);

                alertDialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialogBuilder.show();

                return true;
            case R.id.logout:
                // logout
                unsubTopic();
                FirebaseAuth.getInstance().signOut();
                Intent logout_int = new Intent(MainActivity.this, login.class);
                startActivity(logout_int);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment;

            //menampilkan fragment
            switch (item.getItemId()){

                case R.id.action_home:
                    fragment = new home();
                    getFragmentpage(fragment);
                    return true;

                case R.id.action_profile:
                    fragment = new profile();
                    getFragmentpage(fragment);
                    return true;

            }
            return false;

        }
    };

    private void getFragmentpage(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.page_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getToken() {

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()){
                            Log.d("FAGGOT", "Failed");
                            return;
                        }

                        String token = task.getResult().getToken();
                        String msg = getString(R.string.msg_token, token);

                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                        Log.d("Hello Fagg", msg);
                    }
                });
    }

    private void subToTopic(){
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference subRef = FirebaseDatabase.getInstance().getReference("user").child(Uid);

        subRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String setTopic = (String) dataSnapshot.child("depart").getValue();

                FirebaseMessaging.getInstance().subscribeToTopic("T_"+setTopic)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                subRef.child("topic").setValue("T_"+setTopic);

                                //Toast.makeText(MainActivity.this, "Sub To "+ setTopic, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(MainActivity.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
/*
        FirebaseMessaging.getInstance().subscribeToTopic("T_Account")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        //Toast.makeText(MainActivity.this, "Notification will show up", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
*/
    }

    private void unsubTopic(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference getRef = FirebaseDatabase.getInstance().getReference("user").child(uid);

        getRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String getTopic = (String) dataSnapshot.child("topic").getValue();

                FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopic)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
//                                Toast.makeText(MainActivity.this, "Cleared "+getTopic, Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "ERROR logout: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}