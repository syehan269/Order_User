package com.example.order;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class edit_profile extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout Rl_edit_email, RL_edit_name, Rl_edit_pass;
    private DatabaseReference databaseReference;
    private TextView TV_name, TV_email, TV_pass;
    private ArrayList<String> arrayKategori = new ArrayList<String>();
    private ProgressDialog progressDialog;
    private FirebaseUser user;
    private FirebaseUser fbAuth;
    private TextView spn_edit_profile;
    private String email, pass, name;
    private MaterialButton btn_delete;
    private MaterialAlertDialogBuilder alertDialogBuilder;
    private String[] levelUser = new String[]{"Admin", "Super User", "User"};
    private String[] DepartAdapter = new String[] {"Administration","Finance-Accounting","HR-GA","IT","Marketing","Primary","Secondary"};
    private MaterialAlertDialogBuilder dialogBuilder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        toolbar = findViewById(R.id.toolbar);
        spn_edit_profile = findViewById(R.id.SPN_editUSR);
        Rl_edit_email = findViewById(R.id.RL_editUSR_email);
        RL_edit_name = findViewById(R.id.RL_editUSR_Name);
        Rl_edit_pass = findViewById(R.id.RL_editUSR_pass);
        TV_email = findViewById(R.id.TV_edituSR_email);
        TV_pass = findViewById(R.id.TV_edituSR_pass);
        TV_name = findViewById(R.id.TV_edituSR_name);
        btn_delete = findViewById(R.id.btn_delete_editUSR);
        fbAuth = FirebaseAuth.getInstance().getCurrentUser();
        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        alertDialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        
        email = TV_email.getText().toString().trim();
        pass = TV_pass.getText().toString().trim();
        name = TV_name.getText().toString().trim();
        
        user = FirebaseAuth.getInstance().getCurrentUser();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Rl_edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEmail();
            }
        });

        Rl_edit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPass();
            }
        });

        RL_edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editName();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        setProfile();
        spn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialog();
                dialogBuilder.show();
            }
        });
        displayDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_edit_profile:
                // save
                progressOnDialog();

                saveProfile();
                updateEmail();
                authPass();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveProfile() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        String setName = TV_name.getText().toString().trim();
        String setDepart = spn_edit_profile.getText().toString().trim();
        String setEmail = TV_email.getText().toString().trim();
        String setPass = TV_pass.getText().toString().trim();

        progressDialog.setTitle("Update");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        try {
            databaseReference.child("userName").setValue(setName);
            databaseReference.child("depart").setValue(setDepart);
            databaseReference.child("email").setValue(setEmail);
            databaseReference.child("pass").setValue(setPass);
            //databaseReference.child("topic").setValue("T_"+setDepart);
            unsubTopic();

            //Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e){

            progressDialog.dismiss();
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void editEmail(){
        MaterialAlertDialogBuilder alertDialogBuilder =
                new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        alertDialogBuilder.setTitle("Edit email");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_category , null);

        alertDialogBuilder.setView(view);

        final EditText getemail = view.findViewById(R.id.dialog_category_ET);
        getemail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        alertDialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                email = getemail.getText().toString().trim();

                TV_email.setText(email);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialogBuilder.show();
    }

    private void updateEmail() {

        final String getEmail = TV_email.getText().toString().trim();

        fbAuth.updateEmail(getEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                          //                Toast.makeText(edit_profile.this, "Updated *email", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                //        Toast.makeText(edit_profile.this, "Error *Email", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void editName() {
        MaterialAlertDialogBuilder alertDialogBuilder =
                new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        alertDialogBuilder.setTitle("Edit name");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_category , null);

        alertDialogBuilder.setView(view);

        final EditText getname = view.findViewById(R.id.dialog_category_ET);

        alertDialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                name = getname.getText().toString().trim();

                TV_name.setText(name);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialogBuilder.show();

    }

    private void authPass() {

        final String getPass= TV_pass.getText().toString().trim();

        FirebaseAuth.getInstance().getCurrentUser().updatePassword(getPass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        //Toast.makeText(edit_profile.this, "update *pass", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              //  Toast.makeText(edit_profile.this, "error *pass", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPass(){

        MaterialAlertDialogBuilder alertDialogBuilder =
                new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        alertDialogBuilder.setTitle("Edit password");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.dialog_category , null);

        alertDialogBuilder.setView(view);

        final EditText getPass = view.findViewById(R.id.dialog_category_ET);

        alertDialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                pass = getPass.getText().toString().trim();

                TV_pass.setText(pass);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialogBuilder.show();

    }

    private void setProfile() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getName = (String) dataSnapshot.child("userName").getValue();
                String getEmail = (String) dataSnapshot.child("email").getValue();
                String getDepart = (String) dataSnapshot.child("depart").getValue();
                String getPass = (String) dataSnapshot.child("pass").getValue();

                TV_name.setText(getName);
                TV_email.setText(getEmail);
                TV_pass.setText(getPass);
                spn_edit_profile.setText(getDepart);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void deleteData() {
        try {

            FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(edit_profile.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(edit_profile.this, login.class);
                            startActivity(intent);
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteACC() {

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(edit_profile.this, "Failed Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showDialog(){
        alertDialogBuilder.setTitle("Delete Account");
        alertDialogBuilder.setMessage("Are you sure ?").
                setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressOnDialog();
                        deleteACC();
                        deleteData();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.show();
    }

    private void progressOnDialog(){
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    private void unsubTopic(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference getRef = FirebaseDatabase.getInstance().getReference("user").child(uid);

        getRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String getTopic = (String) dataSnapshot.child("topic").getValue();
                final String getDepart = (String) dataSnapshot.child("depart").getValue();

                FirebaseMessaging.getInstance().unsubscribeFromTopic(getTopic)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(edit_profile.this, "Cleared "+getTopic, Toast.LENGTH_SHORT).show();

                                FirebaseMessaging.getInstance().subscribeToTopic("T_"+getDepart)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Toast.makeText(edit_profile.this, "Sub to T_"+getDepart, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(edit_profile.this, "ERROR logout: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayDialog() {
        dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);
        final String data = "dialog_depart";

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View lView = layoutInflater.inflate(R.layout.dialog_depart_pro, null);
        final RecyclerView rv_depart = lView.findViewById(R.id.RV_depart_pro);

        rv_depart.setLayoutManager(new LinearLayoutManager(edit_profile.this));

        dialogBuilder.setView(lView);
        Log.d(data, "setting layout success");

        DatabaseReference queryDepart = FirebaseDatabase.getInstance().getReference().child("Type");
        Log.d(data,"setting data path");

        FirebaseRecyclerAdapter<category, edit_profile.ViewHolderCategory> firebaseAdapter =
                new FirebaseRecyclerAdapter<category, edit_profile.ViewHolderCategory>(
                        category.class,
                        R.layout.list_depart_pro,
                        edit_profile.ViewHolderCategory.class,
                        queryDepart
                ) {
                    @Override
                    protected void populateViewHolder(edit_profile.ViewHolderCategory viewHolder, category model, int i) {
                        Log.d(data, "adapter created");
                        viewHolder.setCategory(model.getCategory());
                        Log.d(data, "set the data");

                        final String key = getRef(i).getKey();
                        final String value = model.getCategory();

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(edit_profile.this, "Change to "+ value, Toast.LENGTH_SHORT).show();
                                spn_edit_profile.setText(value);

                            }
                        });
                    }
                };

        rv_depart.setAdapter(firebaseAdapter);

        dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

    }

    public static class ViewHolderCategory extends RecyclerView.ViewHolder{

        View view;

        public ViewHolderCategory(View itemView){
            super(itemView);
            view = itemView;
        }

        public void setCategory(String Category){
            TextView kategori = view.findViewById(R.id.CB_depart_pro);
            kategori.setText(Category);

        }

    }
    
}
