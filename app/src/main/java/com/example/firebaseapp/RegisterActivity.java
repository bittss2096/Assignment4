package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
	public FirebaseAuth mAuth;
	public FirebaseFirestore db;

	AppCompatEditText email, password, cpass;
	LinearLayout btn_register;
	ImageView back_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();

		email = findViewById(R.id.et_email);
		password = findViewById(R.id.et_pass);
		cpass = findViewById(R.id.et_confirmpass);
		btn_register = findViewById(R.id.btn_register);
		back_button = findViewById(R.id.back_button);

		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});

		btn_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()
						|| cpass.getText().toString().trim().isEmpty()) {
					if (email.getText().toString().trim().isEmpty()) {
						email.setError("Required Field");
					}
					if (password.getText().toString().trim().isEmpty()) {
						password.setError("Required Field");
					}
					if (cpass.getText().toString().trim().isEmpty()) {
						cpass.setError("Required Field");
					}

				} else {
					validate_user();
				}


			}
		});


	}

	public void validate_user() {
		String userEmail = email.getText().toString().trim();
		String userPassword = password.getText().toString().trim();
		String confirmPassword = cpass.getText().toString().trim();

		if (!userPassword.equals(confirmPassword)) {
			Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
		} else {
			mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
				@Override
				public void onComplete(@NonNull Task<AuthResult> task) {
					if (task.isSuccessful()) {
						Toast.makeText(RegisterActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(RegisterActivity.this, Login_Activity.class);
						startActivity(i);
						finish();
						// Registration success, redirect to login or main screen
					} else {
						// Handle failure
					}
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}