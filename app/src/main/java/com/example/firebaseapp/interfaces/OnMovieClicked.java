package com.example.firebaseapp.interfaces;


import com.example.firebaseapp.models.MovieDetails;

import java.util.List;

public interface OnMovieClicked
{
	public void on_movie_clicked(String id);

	public void on_delete_movie(String id);

	void on_edit_movie(String movieId, List<MovieDetails> movieList, int position);
}
