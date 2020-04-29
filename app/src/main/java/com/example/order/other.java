package com.example.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class other extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private Button btn_delete;
    private FirebaseUser user;
    private MaterialAlertDialogBuilder alertDialogBuilder;

    public other() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view = inflater.inflate(R.layout.fragment_other, container, false);

      btn_delete = view.findViewById(R.id.BTN_delete_ACC);
      user = FirebaseAuth.getInstance().getCurrentUser();
      alertDialogBuilder = new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);

      btn_delete.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              showDialog();
          }
      });

      return view;
    }

    private void showDialog(){
        alertDialogBuilder.setTitle("Delete Account");
        alertDialogBuilder.setMessage("Are you sure ?").
                setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteACC();
                        deleteData();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.show();
    }

    private void deleteData() {
        try {

            FirebaseDatabase.getInstance().getReference("user").child(user.getUid()).removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getActivity(), "Account Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), login.class);
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
                Toast.makeText(getActivity(), "Failed Delete", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
