package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

public class admin_editUSR extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputEditText TI_name, TI_pass;
    private String[] levelUser = new String[]{"Admin", "Super User", "User"};
    private String updateKey, getName, getPass, getDepart, getLevel, setName, setPass, setDepart, setLevel;
    private MaterialBetterSpinner BS_depart, BS_level;
    private DatabaseReference departReference, userReference;
    private ArrayList<String> departString = new ArrayList<String>();
    private Query queryUpdate;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_usr);

        toolbar = findViewById(R.id.toolbar);
        TI_name = findViewById(R.id.ET_update_name);
        TI_pass = findViewById(R.id.ET_update_pass);
        BS_level = findViewById(R.id.SPN_update_level);
        BS_depart = findViewById(R.id.SPN_update_depart);
        progressDialog = new ProgressDialog(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit user");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        departReference = FirebaseDatabase.getInstance().getReference("Type");
        userReference = FirebaseDatabase.getInstance().getReference("user");
        updateKey = getIntent().getExtras().getString("id_USR_update");
        queryUpdate = userReference.child(updateKey);

        //setAdapter level spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(admin_editUSR.this, android.R.layout.simple_spinner_dropdown_item, levelUser);
        BS_level.setAdapter(adapter);

        listCategory();
        setForm();

    }

    private void listCategory() {
        departReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String category = dataSnapshot1.child("Category").getValue().toString();
                    departString.add(category);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(admin_editUSR.this, android.R.layout.simple_spinner_dropdown_item, departString);
                BS_depart.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(admin_editUSR.this, "ERROR reg "+ databaseError, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setForm() {

        queryUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                getPass = (String) dataSnapshot.child("pass").getValue();
                getName = (String) dataSnapshot.child("userName").getValue();
                getLevel = (String) dataSnapshot.child("level").getValue();
                getDepart = (String) dataSnapshot.child("depart").getValue();

                BS_depart.setText(getDepart);
                BS_level.setText(getLevel);
                TI_name.setText(getName);
                TI_pass.setText(getPass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveChange() {
        setDepart = BS_depart.getText().toString();
        setLevel = BS_level.getText().toString();
        setName = TI_name.getText().toString().trim();
        setPass = TI_pass.getText().toString().trim();

        progressDialog.setTitle("Updating");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        try {

            userReference.child(updateKey).child("depart").setValue(setDepart);
            userReference.child(updateKey).child("level").setValue(setLevel);
            userReference.child(updateKey).child("userName").setValue(setName);
            userReference.child(updateKey).child("pass").setValue(setPass);

            Intent intent = new Intent(admin_editUSR.this, adminControl.class);
            startActivity(intent);
            finish();
            Toast.makeText(admin_editUSR.this, "Updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e){
            progressDialog.dismiss();
            Toast.makeText(this, "ERROR update: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_profile, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit_profile:
                saveChange();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
