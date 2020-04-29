package com.example.order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class prot_A extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView_lev;
    private DatabaseReference databaseReference;
    private Button BTN_add;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prot);

        toolbar = findViewById(R.id.toolbar);
        recyclerView_lev = findViewById(R.id.recycle_lev);
        BTN_add = findViewById(R.id.add_level);
        progressBar = findViewById(R.id.progressBar_adm_level);

        recyclerView_lev.setHasFixedSize(true);
        recyclerView_lev.setLayoutManager(new LinearLayoutManager(prot_A.this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Level");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Level");

        BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_level();
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        displayList();
    }

    private void displayList() {
    }

    private void add_level() {

        final AlertDialog.Builder alertDialog =
                new AlertDialog.Builder(prot_A.this, R.style.Theme_AppCompat_Dialog_Alert);

        LayoutInflater layoutInflater = LayoutInflater.from(prot_A.this);
        final View view = layoutInflater.inflate(R.layout.dialog_category, null);

        alertDialog.setView(view);
        alertDialog.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String level;
                EditText level_Et = view.findViewById(R.id.dialog_category_ET);
                level = level_Et.getText().toString().trim();
                databaseReference.push().child("type").setValue(level);
            }
        });

        alertDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();

    }
}
