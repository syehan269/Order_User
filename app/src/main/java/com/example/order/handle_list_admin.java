package com.example.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class handle_list_admin extends AppCompatActivity {

    private Toolbar toolbar;
    private String departement, fbUser, fbKey;
    private DatabaseReference handleReference, userReference;
    private RecyclerView listView;
    private TextView empty_TV;
    private FirebaseAuth fbAuth;
    private ProgressBar progressBar;
    private StorageReference storageReference;
    private MaterialAlertDialogBuilder alertDialogBuilder;
    private Query queryListAll, queryListAdm, queryListHR,queryListFin, queryListSec, queryListPri, queryListIt, queryListMar,
                        queryListApp, queryListDis;
    private FirebaseRecyclerAdapter<handleAdapter, handle_list_admin.ViewHolderHandleADM> firebaseRecyclerAdapterAll, firebaseRecyclerAdapterAdm,
                        firebaseRecyclerAdapterHR, firebaseRecyclerAdapterIT, firebaseRecyclerAdapterMar, firebaseRecyclerAdapterFin,
                        firebaseRecyclerAdapterSec, firebaseRecyclerAdapterPri, firebaseRecyclerAdapterApp, firebaseRecyclerAdapterDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_list_admin);

        toolbar = findViewById(R.id.toolbar);

        fbAuth = FirebaseAuth.getInstance();
        fbUser = fbAuth.getCurrentUser().getUid();

        progressBar = findViewById(R.id.progress_handle);
        empty_TV = findViewById(R.id.Empty_handle);
        storageReference = FirebaseStorage.getInstance().getReference();

        handleReference = FirebaseDatabase.getInstance().getReference("Form");
        userReference = FirebaseDatabase.getInstance().getReference().child("user").child(fbUser).child("level");

        queryListApp = handleReference.orderByChild("approval").equalTo("Approved");
        queryListDis = handleReference.orderByChild("approval").equalTo("Disapproved");

        queryListAll = handleReference.orderByChild("status").equalTo("NO");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Handle");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.RV_handle);
        listView.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onStart() {
        super.onStart();

        displayList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_handle_spr, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.avial:
                displayList();
                return false;

            case R.id.disapprove:
                displayListDis();
                return false;

            case R.id.approve:
                displayListApp();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayListApp() {
        firebaseRecyclerAdapterApp = new FirebaseRecyclerAdapter<handleAdapter, handle_list_admin.ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListApp
        ) {
            @Override
            protected void populateViewHolder(handle_list_admin.ViewHolderHandleADM viewHolderHandleADM, final handleAdapter handleAdapter, int i) {

                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });

            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterApp, true);
    }

    private void displayListDis() {
        firebaseRecyclerAdapterDis = new FirebaseRecyclerAdapter<handleAdapter, handle_list_admin.ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListDis
        ) {
            @Override
            protected void populateViewHolder(handle_list_admin.ViewHolderHandleADM viewHolderHandleADM, final handleAdapter handleAdapter, int i) {

                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });

            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterDis, true);
    }

    private void displayListPri() {
        firebaseRecyclerAdapterPri = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListPri
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterPri, true);
    }

    private void displayListHR() {
        firebaseRecyclerAdapterHR = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListHR
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterHR, true);
    }

    private void displayListFin() {
        firebaseRecyclerAdapterFin = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListFin
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterFin, true);
    }

    private void displaListAdm() {
        firebaseRecyclerAdapterAdm = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListAdm
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterAdm, true);
    }

    private void displayListIt() {
        firebaseRecyclerAdapterIT = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListIt
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterIT, true);
    }

    private void displayListSec() {
        firebaseRecyclerAdapterSec = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListSec
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterSec, true);
    }

    private void displayMar() {
        firebaseRecyclerAdapterMar = new FirebaseRecyclerAdapter<handleAdapter, ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListMar
        ) {
            @Override
            protected void populateViewHolder(ViewHolderHandleADM viewHolderHandleADM, handleAdapter handleAdapter, int i) {
                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });
            }
        };
        listView.swapAdapter(firebaseRecyclerAdapterMar, true);
    }

    private void displayList() {

        firebaseRecyclerAdapterAll = new FirebaseRecyclerAdapter<handleAdapter, handle_list_admin.ViewHolderHandleADM>(
                handleAdapter.class,
                R.layout.recycleview,
                handle_list_admin.ViewHolderHandleADM.class,
                queryListAll
        ) {
            @Override
            protected void populateViewHolder(handle_list_admin.ViewHolderHandleADM viewHolderHandleADM, final handleAdapter handleAdapter, int i) {

                progressBar.setVisibility(View.GONE);
                final String getKeyList = getRef(i).getKey();

                viewHolderHandleADM.setDepartHandle(handleAdapter.getCCategory());
                viewHolderHandleADM.setTItleHandle(handleAdapter.getRRequest());
                viewHolderHandleADM.setApprove(handleAdapter.getApproval());

                viewHolderHandleADM.setStatusList();

                //setEmptyHandler();

                viewHolderHandleADM.lView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(handle_list_admin.this, read_request_SPR.class);
                        intent.putExtra("id_list_SPR", getKeyList);
                        startActivity(intent);
                    }
                });

            }
        };

        listView.setAdapter(firebaseRecyclerAdapterAll);
    }

    public static class ViewHolderHandleADM extends RecyclerView.ViewHolder{
        View lView;

        public ViewHolderHandleADM(View itemView){
            super(itemView);
            lView = itemView;
        }

        public void setDepartHandle(String Ccategory){
            TextView depart_tv = lView.findViewById(R.id.Tv_Departement_hit);
            depart_tv.setText(Ccategory);
        }

        public void setApprove(String Aaprove){
            TextView approve_TV = lView.findViewById(R.id.Approve_TV);
            approve_TV.setText(Aaprove);
        }

        public void setTItleHandle(String NName){
            TextView name_tv = lView.findViewById(R.id.Tv_Title_hit);
            name_tv.setText(NName);
        }

        private void setStatusList(){
            final ImageView handle_status = lView.findViewById(R.id.status_handle);
            final DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference().child("Form");

            handle_status.setImageResource(R.drawable.baseline_error_outline_black_48dp);
            handle_status.setColorFilter(Color.RED);

        }

    }

}
