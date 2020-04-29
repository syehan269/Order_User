package com.example.order;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class adminRequest extends AppCompatActivity {

    private RecyclerView RV_ADM_request;
    private ProgressBar progressBar_adm_user;
    private Toolbar toolbar;
    private Query queryRequest;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<handleAdapter, ViewHolderRequest>
                firebaseRecyclerComp, firebaseRecyclerAvial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        toolbar = findViewById(R.id.toolbar);
        progressBar_adm_user = findViewById(R.id.progressBar_adm_req);
        RV_ADM_request = findViewById(R.id.RV_ADM_req);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("Form");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);

        RV_ADM_request.setLayoutManager(linearLayoutManager);

        progressBar_adm_user.setVisibility(View.VISIBLE);

        displayCompleted();

    }

    private void displayCompleted(){
        queryRequest = databaseReference.orderByChild("status").equalTo("YES");

        firebaseRecyclerComp =
                new FirebaseRecyclerAdapter<handleAdapter, ViewHolderRequest>(
                        handleAdapter.class,
                        R.layout.recycleview,
                        ViewHolderRequest.class,
                        queryRequest
                ) {
                    @Override
                    protected void populateViewHolder( ViewHolderRequest viewHolderRequest, handleAdapter handleAdapter, int i) {
                        progressBar_adm_user.setVisibility(View.GONE);
                        final String getKey = getRef(i).getKey();
                        final String titleDep = handleAdapter.getNname();

                        viewHolderRequest.setTitleADMREQ(handleAdapter.getNname());
                        viewHolderRequest.setApproveADMREQ(handleAdapter.getApproval());
                        viewHolderRequest.setDepartADMREQ(handleAdapter.getCCategory());
                        viewHolderRequest.setStatusO();

                        viewHolderRequest.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(adminRequest.this, preview_request.class);
                                intent.putExtra("id_request", getKey);
                                startActivity(intent);
                            }
                        });

                        viewHolderRequest.view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                //Toast.makeText(adminRequest.this, "Delete", Toast.LENGTH_SHORT).show();

                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(adminRequest.this,
                                        R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                                aleBuilder.setTitle("Delete");
                                aleBuilder.setMessage("Delete "+ titleDep+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child(getKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(adminRequest.this, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                aleBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                aleBuilder.show();

                                return false;
                            }
                        });

                    }
                };

        RV_ADM_request.setAdapter(firebaseRecyclerComp);
    }

    private void displayAvial(){
        queryRequest = databaseReference.orderByChild("status").equalTo("NO");

        firebaseRecyclerAvial =
                new FirebaseRecyclerAdapter<handleAdapter, ViewHolderRequest>(
                        handleAdapter.class,
                        R.layout.recycleview,
                        ViewHolderRequest.class,
                        queryRequest
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderRequest viewHolderRequest, handleAdapter handleAdapter, int i) {
                        progressBar_adm_user.setVisibility(View.GONE);
                        final String getKey = getRef(i).getKey();
                        final String titleDep = handleAdapter.getNname();

                        viewHolderRequest.setTitleADMREQ(handleAdapter.getNname());
                        viewHolderRequest.setApproveADMREQ(handleAdapter.getApproval());
                        viewHolderRequest.setDepartADMREQ(handleAdapter.getCCategory());
                        viewHolderRequest.setStatusX();

                        viewHolderRequest.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(adminRequest.this, preview_request.class);
                                intent.putExtra("id_request", getKey);
                                startActivity(intent);
                            }
                        });

                        viewHolderRequest.view.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                //Toast.makeText(adminRequest.this, "Delete", Toast.LENGTH_SHORT).show();

                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(adminRequest.this,
                                        R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                                aleBuilder.setTitle("Delete");
                                aleBuilder.setMessage("Delete "+ titleDep+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child(getKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(adminRequest.this, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                aleBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                aleBuilder.show();

                                return false;
                            }
                        });

                    }
                };
        RV_ADM_request.swapAdapter(firebaseRecyclerAvial, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_adm_request, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.comp_ADM:
                displayCompleted();
            return true;

            case R.id.Avia_ADM:
                displayAvial();
            return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static class  ViewHolderRequest extends RecyclerView.ViewHolder {
        View view;

        public ViewHolderRequest(View itemView){
            super(itemView);
            view = itemView;
        }

        public void setTitleADMREQ(String title){
            TextView listTitle = view.findViewById(R.id.Tv_Title_hit);
            listTitle.setText(title);
        }

        public void setDepartADMREQ(String Ccategory){
            TextView depart_tv = view.findViewById(R.id.Tv_Departement_hit);
            depart_tv.setText(Ccategory);
        }

        public void setApproveADMREQ(String Aaprove){
            TextView approve_TV = view.findViewById(R.id.Approve_TV);
            approve_TV.setText(Aaprove);
        }

        private void setStatusX(){
            final ImageView handle_status = view.findViewById(R.id.status_handle);

            handle_status.setImageResource(R.drawable.baseline_error_outline_black_48dp);
            handle_status.setColorFilter(Color.RED);

        }

        private void setStatusO(){
            final ImageView handle_status = view.findViewById(R.id.status_handle);

            handle_status.setImageResource(R.drawable.outline_check_circle_black_48dp);
            handle_status.setColorFilter(Color.GREEN);

        }

    }
}
