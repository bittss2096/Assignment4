package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login_Activity extends AppCompatActivity {

	public FirebaseAuth mAuth;
	public FirebaseFirestore db;

	AppCompatEditText email, password;
	LinearLayout btn_login;
	AppCompatTextView btn_register;
	SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sessionManager = new SessionManager(getApplicationContext());

		if (sessionManager.isLoggedIn()) {
			Intent i = new Intent(Login_Activity.this, MainActivity.class);
			startActivity(i);
		} else {

		}

		setContentView(R.layout.activity_login);

		mAuth = FirebaseAuth.getInstance();
		db = FirebaseFirestore.getInstance();

		email = findViewById(R.id.et_email);
		password = findViewById(R.id.et_pass);
		btn_login = findViewById(R.id.btn_login);
		btn_register = findViewById(R.id.btn_register);

		btn_register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(Login_Activity.this, RegisterActivity.class);
				startActivity(i);
			}
		});

		btn_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {
					if (email.getText().toString().trim().isEmpty()) {
						email.setError("Required Field");
					}
					if (password.getText().toString().trim().isEmpty()) {
						password.setError("Required Field");
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

		mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
			@Override
			public void onComplete(@NonNull Task<AuthResult> task) {
				if (task.isSuccessful()) {

					sessionManager.createLoginSession(userEmail);
					Toast.makeText(Login_Activity.this, "Login Successfull", Toast.LENGTH_SHORT).show();

					Intent i = new Intent(Login_Activity.this, MainActivity.class);
					startActivity(i);
					finish();
				} else {
					// Handle failure
					String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

					switch (errorCode) {
						case "ERROR_INVALID_EMAIL":
							// The email address is badly formatted.
							email.setError("Invalid email format.");
							//Toast.makeText(Login_Activity.this, , Toast.LENGTH_SHORT).show();
							break;

						case "ERROR_WRONG_PASSWORD":
							// The password is invalid or the user does not have a password.
							password.setError("Incorrect password.");
							//Toast.makeText(Login_Activity.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
							break;

						case "ERROR_USER_NOT_FOUND":
							// There is no user corresponding to this identifier. The user may have been deleted.
							//	email.setError("Invalid email format.");
							Toast.makeText(Login_Activity.this, "No account found with this email. Register User first", Toast.LENGTH_SHORT).show();
							break;

						case "ERROR_USER_DISABLED":
							// The user account has been disabled by an administrator.
							Toast.makeText(Login_Activity.this, "This account has been disabled.", Toast.LENGTH_SHORT).show();
							break;

						default:
							Toast.makeText(Login_Activity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
							break;
					}
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		finishAffinity();
		finish();
		super.onBackPressed();
	}
}