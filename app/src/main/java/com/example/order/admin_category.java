package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.ULocale;
import android.os.Bundle;
import android.telephony.mbms.StreamingServiceInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class admin_category extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView_cat;
    private Query queryDepart;
    private DatabaseReference databaseReference;
    private ProgressBar proggress_bar;
    private FloatingActionButton FAB_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        toolbar = findViewById(R.id.toolbar);
        recyclerView_cat = findViewById(R.id.recycle_cat);
        FAB_category = findViewById(R.id.FAB_category);
        proggress_bar = findViewById(R.id.progressBar_adm_category);

        recyclerView_cat.setHasFixedSize(true);
        recyclerView_cat.setLayoutManager(new LinearLayoutManager(admin_category.this));

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Type");
        queryDepart = databaseReference.orderByPriority();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Departement");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FAB_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        //FAB animation
        recyclerView_cat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 ){
                    FAB_category.hide();
                }

                else if (dy < 0 ){
                    FAB_category.show();
                }
            }
        });

        proggress_bar.setVisibility(View.VISIBLE);

        //displayList();
        displayCategory();

    }

    private void displayCategory(){

        FirebaseRecyclerAdapter<category, ViewHolderCategory> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<category, ViewHolderCategory>(
                        category.class,
                        R.layout.list_category,
                        ViewHolderCategory.class,
                        queryDepart
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderCategory viewHolderCategory, category model, int i) {

                        proggress_bar.setVisibility(View.GONE);
                        viewHolderCategory.setCategory(model.getCategory());

                        final String Uid = getRef(i).getKey();
                        final String titleDep = model.getCategory();
/*
                        viewHolderCategory.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final MaterialAlertDialogBuilder alertDialogBuilder =
                                        new MaterialAlertDialogBuilder(admin_category.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                                alertDialogBuilder.setTitle("Edit");

                                LayoutInflater layoutInflater = LayoutInflater.from(admin_category.this);
                                final View xView = layoutInflater.inflate(R.layout.dialog_category, null);

                                alertDialogBuilder.setView(xView);
                                alertDialogBuilder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String getDepart;
                                        EditText ET_dialog = xView.findViewById(R.id.dialog_category_ET);
                                        ET_dialog.setHint("Edit...");
                                        getDepart = ET_dialog.getText().toString().trim();

                                        databaseReference.child(Uid).child("Category").setValue(getDepart)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(admin_category.this, "updated", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                alertDialogBuilder.show();
                            }
                        });
*/
                    }
                };

        recyclerView_cat.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ViewHolderCategory extends RecyclerView.ViewHolder{

        View view;

        public ViewHolderCategory(View itemView){
            super(itemView);
            view = itemView;

        }

        public void setCategory(String Category){
            TextView kategori = view.findViewById(R.id.Tv_category_adm_list);
            kategori.setText(Category);
        }

    }

    private void add() {

        final MaterialAlertDialogBuilder alerBuilder =
                new MaterialAlertDialogBuilder(admin_category.this, R.style.ThemeOverlay_AppCompat_Dialog_Alert);

        LayoutInflater layoutInflater = LayoutInflater.from(admin_category.this);
        final View view = layoutInflater.inflate(R.layout.dialog_category,null);

        alerBuilder.setTitle("Add");
        alerBuilder.setView(view);
        alerBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String categoryName;
                EditText categoryInput = view.findViewById(R.id.dialog_category_ET);
                categoryInput.setHint("Add...");
                categoryName = categoryInput.getText().toString().trim();
                databaseReference.push().child("Category").setValue(categoryName);
            }
        });
        alerBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alerBuilder.show();
    }

}
