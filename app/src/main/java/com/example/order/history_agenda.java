package com.example.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class history_agenda extends AppCompatActivity {

    private Toolbar toolbar;
    private DatabaseReference historyReference;
    private RecyclerView listView;
    private FirebaseAuth fbAuth;
    private String firebaseUser;
    private Query queryHistory, queryDelete;
    private ProgressBar progressBar;
    private FirebaseRecyclerAdapter<historyAdapter, ViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_agenda);

        toolbar = findViewById(R.id.toolbar);
        fbAuth = FirebaseAuth.getInstance();
        firebaseUser = fbAuth.getCurrentUser().getUid();
        progressBar = findViewById(R.id.progress_hist);
        historyReference = FirebaseDatabase.getInstance().getReference().child("Form");

        //queryHistory = historyReference.orderByChild("uid").equalTo(firebaseUser);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.recycle_hist);
        listView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.GONE);

        readHistory();
    }

    private void readHistory() {

        String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
        queryHistory = historyReference.orderByChild("uid").equalTo(key);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<historyAdapter, history_agenda.ViewHolder>(
                historyAdapter.class,
                R.layout.recycleview,
                history_agenda.ViewHolder.class,
                queryHistory)
        {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, final historyAdapter historyAdapter, int i) {

                final String fbKey = getRef(i).getKey();
                final String titleDep = historyAdapter.getRequest();

                progressBar.setVisibility(View.GONE);

                viewHolder.setDepartList(historyAdapter.getcategory());
                viewHolder.setTitleList(historyAdapter.getRequest());
                viewHolder.setApprove(historyAdapter.getApproval());

                viewHolder.setStatushistory();

                viewHolder.hView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(history_agenda.this, preview_request.class);
                        intent.putExtra("id_request", fbKey);
                        startActivity(intent);
                    }
                });

                viewHolder.hView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //Toast.makeText(adminRequest.this, "Delete", Toast.LENGTH_SHORT).show();

                        MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(history_agenda.this,
                                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                        aleBuilder.setTitle("Delete");
                        aleBuilder.setMessage("Delete "+ titleDep+" ?");

                        aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                historyReference.child(fbKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(history_agenda.this, "Deleted", Toast.LENGTH_SHORT).show();
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
        listView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View hView;

        public ViewHolder(View itehView){
            super(itehView);
            hView = itehView;
        }

        public void setDepartList(String departement){
            TextView depart_tv = hView.findViewById(R.id.Tv_Departement_hit);
            depart_tv.setText(departement);
        }

        public void setTitleList(String title){
            TextView title_tv = hView.findViewById(R.id.Tv_Title_hit);
            title_tv.setText(title);
        }

        public void setApprove(String Aaprove){
            TextView approve_TV = hView.findViewById(R.id.Approve_TV);
            approve_TV.setText(Aaprove);
        }

        private void setStatushistory(){

            final ImageView handle_status = hView.findViewById(R.id.status_handle);
            final DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Form");
        }

    }

    private int countItem(){
        int count = 0;

        if (firebaseRecyclerAdapter != null){
            count = firebaseRecyclerAdapter.getItemCount();
        }

        return count;
    }

}
