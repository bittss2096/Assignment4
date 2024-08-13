package com.example.firebaseapp.adapters;

import static com.example.firebaseapp.AddActivity.DB_NAME;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firebaseapp.R;
import com.example.firebaseapp.interfaces.OnMovieClicked;
import com.example.firebaseapp.models.MovieDetails;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class AllMoviesAdapter extends RecyclerView.Adapter<AllMoviesAdapter.ViewHolder> {

	public Context context;
	public List<MovieDetails> movie_list;

	OnMovieClicked on_movie_clicked;
	public Dialog discard_dialog;


	public AllMoviesAdapter(Context context, List<MovieDetails> movie_list, OnMovieClicked on_movie_clicked) {
		this.context = context;
		this.movie_list = movie_list;
		this.on_movie_clicked = on_movie_clicked;
		Log.e("daalbhaat", "movies_list  " + movie_list.size());
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allmovies, parent, false);
		ViewHolder card = new ViewHolder(v);
		return card;
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width / 2, width / 2);
//        holder.fl_main.setLayoutParams(params);

		final MovieDetails movie_details = movie_list.get(position);


		String title = movie_details.getTitle();
		String studio_yr = movie_details.getStudio();
		String critics_rating = movie_details.getCriticsRating();
		String short_desc = movie_details.getShortDescription();

		String movie_poster = movie_details.getImage();


		holder.tv_movie_title.setSelected(true);
		holder.tv_movie_title.setText(title);
		holder.tv_id.setText(studio_yr);
		holder.tv_type.setText(critics_rating);
		holder.tv_desc.setText(short_desc);

		Glide.with(context).load(Uri.parse(movie_poster)).placeholder(R.drawable.clapperboard).into(holder.iv_thumbnail);

		holder.fl_main.setOnClickListener(new View.OnClickListener() {
			@Override

			public void onClick(View v) {
				//on_movie_clicked.on_movie_clicked(imdb_rating);
			}
		});


		holder.iv_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MovieDetails movieToEdit = movie_list.get(position);
				String movieId = movieToEdit.getDocumentId();
				on_movie_clicked.on_edit_movie(movieId,movie_list,position);

			}
		});

		holder.tv_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{

				MovieDetails movieToEdit = movie_list.get(position);
				String movieId = movieToEdit.getDocumentId();


				show_delete_dialog(movieId,position);


			}
		});


	}


	@Override
	public int getItemCount() {

		return movie_list.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		TextView tv_movie_title, tv_id, tv_type, tv_desc, tv_delete;
		ImageView iv_thumbnail, iv_edit, iv_delete;

		FrameLayout fl_main;

		public ViewHolder(View itemView) {
			super(itemView);

			fl_main = itemView.findViewById(R.id.fl_main);
			iv_thumbnail = itemView.findViewById(R.id.iv_thumbnail);
			tv_movie_title = itemView.findViewById(R.id.tv_movie_title);
			tv_id = itemView.findViewById(R.id.tv_studio);
			tv_type = itemView.findViewById(R.id.tv_type);
			tv_desc = itemView.findViewById(R.id.tv_desc);
			iv_edit = itemView.findViewById(R.id.iv_edit);
			tv_delete = itemView.findViewById(R.id.tv_delete);


		}
	}

	private void show_delete_dialog(String movieID, int position) {

		discard_dialog = new Dialog(context);
		discard_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		discard_dialog.setContentView(R.layout.dialog_alertdialog);
		discard_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		discard_dialog.setCanceledOnTouchOutside(true);


		TextView yes = discard_dialog.findViewById(R.id.tv_yes);
		TextView no = discard_dialog.findViewById(R.id.tv_cancel);


		yes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				FirebaseFirestore db = FirebaseFirestore.getInstance();
				db.collection(DB_NAME).document(movieID)
						.delete()
						.addOnSuccessListener(aVoid -> {
							// Remove the movie from the list and notify the adapter
							movie_list.remove(position);
							notifyItemRemoved(position);
							notifyItemRangeChanged(position, movie_list.size());
							Toast.makeText(context, "Data Deleted", Toast.LENGTH_SHORT).show();
						})
						.addOnFailureListener(e -> {
							// Handle the failure
						});
				discard_dialog.dismiss();
			}
		});

		no.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				discard_dialog.dismiss();
			}
		});


//        discard_dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		discard_dialog.show();

	}

}
