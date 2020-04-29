package com.example.order;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class admin_Home extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match

    private CardView Rl_form, Rl_history, Rl_list, Rl_test;
    public StorageReference fbStorage;
    public String UID;
    private FirebaseAuth fbAuth;

    // TODO: Rename and change types of parameters

    public admin_Home() {
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
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        fbAuth = FirebaseAuth.getInstance();
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Rl_form = view.findViewById(R.id.CV_BTN_form);
        Rl_history = view.findViewById(R.id.CV_BTN_history);
        Rl_list = view.findViewById(R.id.CV_BTN_list);
  //      Rl_test = view.findViewById(R.id.CV_active_home);

        fbStorage = FirebaseStorage.getInstance().getReference();

        Rl_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getActivity(), form_admin.class);
                //startActivity(intent);
            }
        });

        Rl_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), history_agenda_SPR.class);
                startActivity(intent);
            }
        });

        Rl_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), handle_list_admin.class);
                startActivity(intent);
            }
        });



        return view;
    }



}

