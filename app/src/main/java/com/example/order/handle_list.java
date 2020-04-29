package com.example.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class handle_list extends AppCompatActivity {

    private Toolbar toolbar;
    private String departement, fbUser, depart, queryHandle, from;
    private DatabaseReference handleReference, userReference;
    private RecyclerView listView;
    private TextView empty_TV;
    private FirebaseAuth fbAuth;
    private ProgressBar progressBar;
    private MaterialAlertDialogBuilder dialogBuilder;
    private RadioGroup RG_list;
    private Query queryAll, queryDepartTI, getQueryDepartHR, getQueryDepartADM, getQueryDepartPRI,
                    getQueryDepartSEC, getQueryDepartMAR, getQueryDepartFIN;
    private FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle> firebaseRecyclerAdapterAll, firebaseRecyclerAdapterTI,
                    firebaseRecyclerAdapterHR, firebaseRecyclerAdapterMar, firebaseRecyclerAdapterAdm, firebaseRecyclerAdapterSec,
                    firebaseRecyclerAdapterPri, firebaseRecyclerAdapterFin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_list);

        toolbar = findViewById(R.id.toolbar);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser().getUid();

        progressBar = findViewById(R.id.progress_handle);
        empty_TV = findViewById(R.id.Empty_handle);

        handleReference = FirebaseDatabase.getInstance().getReference("Form");

        queryDepartTI = handleReference.orderByChild("queryHandle").equalTo("IT_NO");
        getQueryDepartADM = handleReference.orderByChild("queryHandle").equalTo("Administration_NO");
        getQueryDepartFIN = handleReference.orderByChild("queryHandle").equalTo("Finance-Accounting_NO");
        getQueryDepartMAR = handleReference.orderByChild("queryHandle").equalTo("Marketing_NO");
        getQueryDepartPRI = handleReference.orderByChild("queryHandle").equalTo("Primary_NO");
        getQueryDepartSEC = handleReference.orderByChild("queryHandle").equalTo("Secondary_NO");
        getQueryDepartHR = handleReference.orderByChild("queryHandle").equalTo("HR-GA_NO");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Handle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.RV_handle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        listView.setLayoutManager(linearLayoutManager);

        progressBar.setVisibility(View.VISIBLE);
        //getFrom();

    }

    @Override
    protected void onStart() {
        super.onStart();

        getFrom();
        //displayList();
    }

    /*private int countItem(){
        int count = 0;

        if (firebaseRecyclerAdapter != null){
            count = firebaseRecyclerAdapter.getItemCount();
        }

        return count;
    }
*/
    /*
    private void setEmptyHandler() {
        if (countItem() > 0){
            Toast.makeText(handle_list.this, "COUNT: "+countItem(), Toast.LENGTH_SHORT).show();
        }

        else if (countItem() == 0){
            empty_TV.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        }
    }
*/

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_handle, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.handle_available:
                //displayList();
                return true;

            case R.id.handle_departement:
                dialogBuilder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                LayoutInflater layoutInflater = LayoutInflater.from(this);
                final View lView = layoutInflater.inflate(R.layout.dialog_depart_list, null);
                RG_list = lView.findViewById(R.id.Radio_group);

                dialogBuilder.setView(lView);

                RG_list.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.RB_it:
                                displayListIt();
                                break;

                            case R.id.RB_admin:
                                displaListAdm();
                                break;

                            case R.id.RB_fin:
                                displayListFin();
                                break;

                            case R.id.RB_hrga:
                                displayListHR();
                                break;

                            case R.id.RB_pri:
                                displayListPri();
                                break;

                            case R.id.RB_sec:
                                displayListSec();
                                break;

                            case R.id.RB_mar:
                                displayMar();
                                break;
                        }
                    }
                });
                dialogBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialogBuilder.show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    private void displayMar() {
        firebaseRecyclerAdapterMar = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                getQueryDepartMAR
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterMar, true);
    }

    private void displayListSec() {
        firebaseRecyclerAdapterSec = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                getQueryDepartSEC
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterSec, true);
    }

    private void displayListPri() {
        firebaseRecyclerAdapterPri = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                getQueryDepartPRI
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterPri, true);
    }

    private void displayListFin() {
        firebaseRecyclerAdapterFin = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                getQueryDepartFIN
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterFin, true);
    }

    private void displaListAdm() {
        firebaseRecyclerAdapterAdm = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                getQueryDepartADM
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterAdm, true);
    }

    private void displayList(String getfrom) {
        queryAll = handleReference.orderByChild("queryHandle").equalTo(from+"_NO");

        //Toast.makeText(this, "query: "+getfrom+"_NO", Toast.LENGTH_SHORT).show();
        firebaseRecyclerAdapterAll = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                queryAll
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, final handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setName(handleAdapter.getNname());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                //setEmptyHandler();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });

            }
        };
        listView.setAdapter(firebaseRecyclerAdapterAll);
    }

    private void displayListIt(){
        firebaseRecyclerAdapterTI = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                queryDepartTI
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });
            }
        };

        listView.swapAdapter(firebaseRecyclerAdapterTI, true);
    }

    private void displayListHR(){
        firebaseRecyclerAdapterHR = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandle>(
                handleAdapter.class,
                R.layout.recycleview,
                ViewHolderHandle.class,
                getQueryDepartHR
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandle viewHolderHandle, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandle.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandle.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandle.setApprove(handleAdapter.getApproval());
                viewHolderHandle.setStatusList();

                viewHolderHandle.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list.this, readRequest.class);
                        intent.putExtra("id_list", getKeyList);
                        startActivity(intent);

                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterHR, true);
    }

    public static class ViewHolderHandle extends RecyclerView.ViewHolder{
        View mView;

        public ViewHolderHandle(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setDepartHandle(String Ccategory){
            TextView depart_tv = mView.findViewById(R.id.Tv_Departement_hit);
            depart_tv.setText(Ccategory);
        }

        public void setApprove(String Aaprove){
            TextView approve_TV = mView.findViewById(R.id.Approve_TV);
            approve_TV.setText(Aaprove);
        }

        public void setTItleHandle(String NName){
            TextView name_tv = mView.findViewById(R.id.Tv_Title_hit);
            name_tv.setText(NName);
        }

        private void setStatusList(){
            final ImageView handle_status = mView.findViewById(R.id.status_handle);
            final DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference().child("Form");
        }

        public void setName(String name){
            TextView name_TV = mView.findViewById(R.id.Tv_Departement_hit);
            name_TV.setText(name);
        }

    }

    private void getFrom(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user")
                .child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                from = (String) dataSnapshot.child("depart").getValue();
                //Toast.makeText(handle_list.this, "From: "+from, Toast.LENGTH_SHORT).show();
                displayList(from);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
