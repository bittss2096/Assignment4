package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebaseapp.models.MovieDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

	TextView tv_title;
	EditText et_movie, et_studio, et_cricrate;
	ImageView back_button;

	String document_id, movie_title, studio, cric_rating;
	int current_view;
	TextView tv_done;
	LinearLayout ll_view;
	FirebaseFirestore db;
	DocumentReference df;
	public static String DB_NAME = "Movies_DB";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		tv_title = findViewById(R.id.tv_title);
		back_button = findViewById(R.id.back_button);
		et_movie = findViewById(R.id.et_movie);
		et_studio = findViewById(R.id.et_studio);
		et_cricrate = findViewById(R.id.et_cricrate);
		ll_view = findViewById(R.id.ll_view);
		tv_done = findViewById(R.id.tv_done);


		document_id = getIntent().getStringExtra("doc_id");
		current_view = getIntent().getIntExtra("is_from_add", 0);

		db = FirebaseFirestore.getInstance();


		try {
			initialize_data();
		} catch (Exception e) {
			load_view_for_add();
			Log.e("in_preview", "exception " + e.getMessage());
		}


		tv_done.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (et_movie.getText().toString().trim().isEmpty() || et_studio.getText().toString().trim().isEmpty() ||
						et_cricrate.getText().toString().trim().isEmpty()) {
					if (et_movie.getText().toString().trim().isEmpty()) {
						et_movie.setError("Required Field");
					}
					if (et_studio.getText().toString().trim().isEmpty()) {
						et_studio.setError("Required Field");
					}
					if (et_cricrate.getText().toString().trim().isEmpty()) {
						et_cricrate.setError("Required Field");
					}

				} else {
					if (current_view == 1) {
						// logic for edit
						update_data_in_firebase();
					} else {
						// logic for add
						add_data_in_firebase();

					}
				}

			}
		});

		back_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
	}

	public void add_data_in_firebase() {

		String title = et_movie.getText().toString().trim();
		String studio = et_studio.getText().toString().trim();
		String cric_rate = et_cricrate.getText().toString().trim();

		//MovieDetails movie = new MovieDetails("",title,studio,cric_rate,"","");
		//MovieDetails movie = new MovieDetails();
		// Create a new movie document using a Map
		Map<String, Object> movie = new HashMap<>();
		movie.put("title", title);
		movie.put("studio", studio);
		movie.put("criticsRating", cric_rate);
		movie.put("image", "");
		movie.put("shortDescription", "");


		db.collection(DB_NAME)
				.add(movie)
				.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
					@Override
					public void onSuccess(DocumentReference documentReference) {
						Toast.makeText(AddActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(AddActivity.this, MainActivity.class);
						startActivity(i);
						finish();
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.e("daalbhat", "data_failed");
					}
				});
	}

	public void update_data_in_firebase() {
		df = db.collection(DB_NAME).document(document_id);

		df.update("title", et_movie.getText().toString().trim(),
						"criticsRating", et_cricrate.getText().toString().trim(),
						"studio", et_studio.getText().toString().trim()).
				addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void unused) {
						Toast.makeText(AddActivity.this, "Data Updated", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(AddActivity.this, MainActivity.class);
						startActivity(i);
						finish();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Log.e("daalbhat", "data_updation_failed");
					}
				});


	}


	public void initialize_data() {

		Log.e("daalbhat", "initialized_doc_id  " + document_id);
		df = db.collection(DB_NAME).document(document_id);
		Log.e("daalbhat", "initialized " + df.get().toString());


		df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
			@Override
			public void onComplete(@NonNull Task<DocumentSnapshot> task) {
				if (task.isSuccessful()) {
					DocumentSnapshot document = task.getResult();
					if (document.exists()) {

						MovieDetails movie = document.toObject(MovieDetails.class);
						movie_title = movie.getTitle();
						studio = movie.getStudio();
						cric_rating = movie.getCriticsRating();
						Log.e("daalbhat", "document exists " + movie_title);

						// display data

						tv_title.setText("Edit Details");
						tv_done.setText("Update Movie");

						et_movie.setText(movie_title);
						et_studio.setText(studio);
						et_cricrate.setText(cric_rating);


					} else {
						// No such document
						load_view_for_add();

						Log.e("daalbhat", "No such document");
					}
				} else {
					// Task failed with an exception
					Log.e("daalbhat", "get failed with ", task.getException());

					load_view_for_add();
				}
			}
		});
	}

	public void load_view_for_add() {
		movie_title = "";
		studio = "";
		cric_rating = "";

		// display data
		tv_title.setText("Add Details");
		tv_done.setText("Add Movie");

		et_movie.setText(movie_title);
		et_studio.setText(studio);
		et_cricrate.setText(cric_rating);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}