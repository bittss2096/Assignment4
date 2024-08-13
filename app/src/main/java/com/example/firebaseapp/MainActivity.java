package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.firebaseapp.adapters.AllMoviesAdapter;
import com.example.firebaseapp.interfaces.OnMovieClicked;
import com.example.firebaseapp.models.MovieDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	public FirebaseFirestore db;
	public List<MovieDetails> movies_list;

	RecyclerView rv_movieslist;
	ImageView iv_logout, iv_add;
	public AllMoviesAdapter all_movies_adapter;

	SessionManager sessionManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sessionManager = new SessionManager(getApplicationContext());

		rv_movieslist = findViewById(R.id.rv_movieslist);
		iv_logout = findViewById(R.id.iv_logout);
		iv_add = findViewById(R.id.iv_add);

		db = FirebaseFirestore.getInstance();
		movies_list = new ArrayList<>();

		rv_movieslist.setLayoutManager(new LinearLayoutManager(this));

		try {
			load_data();
		} catch (Exception e) {
			Log.e("daalbhat", "exception e" + e.getMessage());
		}


		iv_logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sessionManager.logoutUser();
				Toast.makeText(MainActivity.this, "User Successfully Logged out !!!\nYou need to Login Again", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(MainActivity.this, Login_Activity.class);
				startActivity(i);
				finish();
			}
		});

		iv_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(MainActivity.this, AddActivity.class);
				i.putExtra("is_from_add", 2);
				i.putExtra("doc_id", -1);
				startActivity(i);

			}
		});


	}

	public void load_data() {
		db.collection("Movies_DB")
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
					@Override
					public void onComplete(@NonNull Task<QuerySnapshot> task) {
						if (task.isSuccessful()) {
							movies_list.clear();
							for (DocumentSnapshot document : task.getResult()) {
								String documentId = document.getId();
								Log.e("daalbhaat", "document_id  " + documentId);
								MovieDetails movie = document.toObject(MovieDetails.class);

								movie.setDocumentId(documentId);
								movies_list.add(movie);
							}
							load_view();


							//adapter.notifyDataSetChanged();
						} else {
							Log.e("daalbhaat", "movies_list_else  " + task.getException().getMessage());
							// Handle error
						}
					}
				});
	}

	public void load_view() {
		if (movies_list != null) {
			all_movies_adapter = new AllMoviesAdapter(MainActivity.this, movies_list, onMovieClicked);
			rv_movieslist.setAdapter(all_movies_adapter);
		}

	}


	OnMovieClicked onMovieClicked = new OnMovieClicked() {
		@Override
		public void on_movie_clicked(String id) {

		}

		@Override
		public void on_edit_movie(String movieId, List<MovieDetails> movieList, int position) {
			Intent i = new Intent(MainActivity.this, AddActivity.class);
			i.putExtra("is_from_add", 1);
			i.putExtra("doc_id", movieId);
			startActivity(i);
		}

		@Override
		public void on_delete_movie(String id) {

		}
	};

	@Override
	public void onBackPressed() {
		finishAffinity();
		finish();
		super.onBackPressed();
	}
}