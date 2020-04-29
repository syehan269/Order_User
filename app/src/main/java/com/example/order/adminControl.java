package com.example.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.xml.sax.ErrorHandler;

import java.net.FileNameMap;

public class adminControl extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab_control_adm;
    private RecyclerView recyclerView_user;
    private DatabaseReference dataReference;
    private ProgressBar progressBar_adm_user;
    private Query queryUser;
    private FirebaseRecyclerAdapter<userList, ViewHolderUser> firebaseRecyclerAdapter, firebaseRecyclerAdapterSPR,
                                                                firebaseRecyclerAdapterADM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);
        toolbar = findViewById(R.id.toolbar);
        recyclerView_user = findViewById(R.id.recycle_admin);
        fab_control_adm = findViewById(R.id.FAB_control);
        progressBar_adm_user = findViewById(R.id.progressBar_adm_user);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView_user.setLayoutManager(linearLayoutManager);

        dataReference = FirebaseDatabase.getInstance().getReference("user");

        progressBar_adm_user.setVisibility(View.VISIBLE);

        fab_control_adm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminControl.this , register.class);
                startActivity(intent);
            }
        });

        //FAB animation
        recyclerView_user.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 ){
                    fab_control_adm.hide();
                }

                else if (dy < 0 ){
                    fab_control_adm.show();
                }
            }
        });

        displayUser();

    }
//bruh intent manual, gara gara bug
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(adminControl.this, adminHome.class);
            startActivity(intent);
        }

        return true;
    }

    //3 macem method gawe filter list account

    private void displayUser() {

        queryUser = dataReference.orderByChild("level").equalTo("User");

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<userList, ViewHolderUser>(
                        userList.class,
                        R.layout.list_adm_user,
                        ViewHolderUser.class,
                        queryUser
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderUser viewHolderUser, userList model, int i) {
                        progressBar_adm_user.setVisibility(View.GONE);

                        viewHolderUser.setUserName(model.getUserName());
                        viewHolderUser.setType(model.getLevel());
                        viewHolderUser.setEmail(model.getEmail());

                        final String userNAme = model.getUserName();
                        final String id_menu = getRef(i).getKey();

                        viewHolderUser.mview.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final MaterialAlertDialogBuilder dialogBuilder =
                                        new MaterialAlertDialogBuilder(adminControl.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                                LayoutInflater layoutInflater = LayoutInflater.from(adminControl.this);
                                final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                                dialogBuilder.setView(aView);

                                MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                                MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                                MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                                menu_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(adminControl.this);
                                        aleBuilder.setTitle("Delete User");
                                        aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                        aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dataReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(adminControl.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                        aleBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        });

                                        aleBuilder.show();
                                    }
                                });

                                menu_view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(adminControl.this, admin_detailUSR.class);
                                        intent.putExtra("id_USR_view", id_menu);
                                        startActivity(intent);
                                    }
                                });

                                menu_update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(adminControl.this, admin_editUSR.class);
                                        intent.putExtra("id_USR_update", id_menu);
                                        startActivity(intent);
                                    }
                                });

                                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                dialogBuilder.show();
                            }
                        });

                    }
                };
        recyclerView_user.setAdapter(firebaseRecyclerAdapter);
    }

    private void displaySuper(){
        queryUser = dataReference.orderByChild("level").equalTo("Super User");

        firebaseRecyclerAdapterSPR = new FirebaseRecyclerAdapter<userList, ViewHolderUser>(
                userList.class,
                R.layout.list_adm_user,
                ViewHolderUser.class,
                queryUser
        ) {
            @Override
            protected void populateViewHolder(ViewHolderUser viewHolderUser, userList userList, int i) {
                progressBar_adm_user.setVisibility(View.GONE);

                viewHolderUser.setUserName(userList.getUserName());
                viewHolderUser.setType(userList.getLevel());
                viewHolderUser.setEmail(userList.getEmail());

                final String userNAme = userList.getUserName();
                final String id_menu = getRef(i).getKey();

                viewHolderUser.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialAlertDialogBuilder dialogBuilder =
                                new MaterialAlertDialogBuilder(adminControl.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                        LayoutInflater layoutInflater = LayoutInflater.from(adminControl.this);
                        final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                        dialogBuilder.setView(aView);

                        MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                        MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                        MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                        menu_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(adminControl.this);
                                aleBuilder.setTitle("Delete User");
                                aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(adminControl.this, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                aleBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                aleBuilder.show();
                            }
                        });

                        menu_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(adminControl.this, admin_detailUSR.class);
                                intent.putExtra("id_USR_view", id_menu);
                                startActivity(intent);
                            }
                        });

                        menu_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(adminControl.this, admin_editUSR.class);
                                intent.putExtra("id_USR_update", id_menu);
                                startActivity(intent);
                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialogBuilder.show();
                    }
                });
            }
        };

        recyclerView_user.swapAdapter(firebaseRecyclerAdapterSPR, true);
    }

    private void displayAdmin(){
        queryUser = dataReference.orderByChild("level").equalTo("Admin");

        firebaseRecyclerAdapterADM = new FirebaseRecyclerAdapter<userList, ViewHolderUser>(
                userList.class,
                R.layout.list_adm_user,
                ViewHolderUser.class,
                queryUser
        ) {
            @Override
            protected void populateViewHolder(ViewHolderUser viewHolderUser, userList userList, int i) {
                progressBar_adm_user.setVisibility(View.GONE);

                viewHolderUser.setUserName(userList.getUserName());
                viewHolderUser.setType(userList.getLevel());
                viewHolderUser.setEmail(userList.getEmail());

                final String userNAme = userList.getUserName();
                final String id_menu = getRef(i).getKey();

                viewHolderUser.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final MaterialAlertDialogBuilder dialogBuilder =
                                new MaterialAlertDialogBuilder(adminControl.this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

                        LayoutInflater layoutInflater = LayoutInflater.from(adminControl.this);
                        final View aView = layoutInflater.inflate(R.layout.dialog_mng_adm, null);

                        dialogBuilder.setView(aView);

                        MaterialButton menu_delete = aView.findViewById(R.id.delete_adm);
                        MaterialButton menu_view = aView.findViewById(R.id.view_adm);
                        MaterialButton menu_update = aView.findViewById(R.id.update_adm);

                        menu_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MaterialAlertDialogBuilder aleBuilder = new MaterialAlertDialogBuilder(adminControl.this);
                                aleBuilder.setTitle("Delete User");
                                aleBuilder.setMessage("Delete "+ userNAme+" ?");

                                aleBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dataReference.child(id_menu).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(adminControl.this, "Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                aleBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });

                                aleBuilder.show();
                            }
                        });

                        menu_view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(adminControl.this, admin_detailUSR.class);
                                intent.putExtra("id_USR_view", id_menu);
                                startActivity(intent);
                            }
                        });

                        menu_update.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(adminControl.this, admin_editUSR.class);
                                intent.putExtra("id_USR_update", id_menu);
                                startActivity(intent);
                            }
                        });

                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialogBuilder.show();
                    }
                });
            }
        };
        recyclerView_user.swapAdapter(firebaseRecyclerAdapterADM, true);
    }

    public static class ViewHolderUser extends RecyclerView.ViewHolder{

        View mview;

        public ViewHolderUser(View itemView){
            super(itemView);
            mview = itemView;
        }

        public void setType(String type){
            TextView tYpe = mview.findViewById(R.id.Tv_type_adm_user);
            tYpe.setText(type);
        }
        public void setUserName(String userName){
            TextView uSerName = mview.findViewById(R.id.Tv_name_adm_user);
            uSerName.setText(userName);
        }
        public void setEmail(String email){
            TextView uSerName = mview.findViewById(R.id.Tv_email_adm_user);
            uSerName.setText(email);
        }

        public void setImageList(String URL){
            ImageView photo_img = mview.findViewById(R.id.IV_poto_User);
            Glide.with(photo_img.getContext())
                    .load(URL)
                    .into(photo_img);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_control_adm, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.show_admin:
                displayAdmin();

                return true;
            case R.id.show_super_user:
                displaySuper();

                return true;

            case R.id.show_user:
                displayUser();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}