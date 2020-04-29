package com.example.order;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class adminDetail extends AppCompatActivity {

	private Toolbar toolbar;
	private TextView tv_type, tv_name, tv_email;
	private DatabaseReference userReference;
	private String id_user = null, UUIID, name_st, type_st, email_st;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin_detail);

		toolbar = findViewById(R.id.toolbar);
		tv_email = findViewById(R.id.Tv_Email_adm_detail);
		tv_name = findViewById(R.id.Tv_Name_adm_detail);
		tv_type = findViewById(R.id.Tv_Type_adm_detail);
		userReference = FirebaseDatabase.getInstance().getReference().child("user");

		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("Detail");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);



	}

}
