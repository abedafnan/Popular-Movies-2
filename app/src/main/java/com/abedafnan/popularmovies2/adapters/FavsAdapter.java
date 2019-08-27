package com.abedafnan.popularmovies2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abedafnan.popularmovies2.R;
import com.abedafnan.popularmovies2.data.MovieEntity;
import com.abedafnan.popularmovies2.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavsAdapter extends RecyclerView.Adapter<FavsAdapter.MoviesViewHolder> {

    private List<MovieEntity> mMoviesList;

    public FavsAdapter() {}

    public void setFavsList(List<MovieEntity> taskEntries) {
        mMoviesList = taskEntries;
        notifyDataSetChanged();
    }

    public List<MovieEntity> getFavsList() {
        return mMoviesList;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);

        final MoviesViewHolder viewHolder = new MoviesViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        MovieEntity currentMovie = mMoviesList.get(i);

        moviesViewHolder.movieTitleTv.setText(currentMovie.getName());
    }

    @Override
    public int getItemCount() {
        if (mMoviesList == null) {
            return 0;
        }
        return mMoviesList.size();
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        TextView movieTitleTv;

        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            movieTitleTv = itemView.findViewById(android.R.id.text1);
        }
    }
}
