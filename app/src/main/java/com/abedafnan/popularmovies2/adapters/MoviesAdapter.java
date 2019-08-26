package com.abedafnan.popularmovies2.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.abedafnan.popularmovies2.R;
import com.abedafnan.popularmovies2.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> mMoviesList;
    private OnItemClickHandler mOnItemClickHandler;

    public MoviesAdapter(List<Movie> movies, OnItemClickHandler clickHandler) {
        mMoviesList = movies;
        mOnItemClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_movie, viewGroup, false);

        final MoviesViewHolder viewHolder = new MoviesViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickHandler.onItemClick(viewHolder.getPosition());

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder moviesViewHolder, int i) {
        Movie currentMovie = mMoviesList.get(i);

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + currentMovie.getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.mipmap.ic_launcher).into(moviesViewHolder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return mMoviesList.size();
    }

    public interface OnItemClickHandler {
        void onItemClick(int position);
    }

    class MoviesViewHolder extends RecyclerView.ViewHolder {

        ImageView posterImageView;

        MoviesViewHolder(@NonNull View itemView) {
            super(itemView);

            posterImageView = itemView.findViewById(R.id.imageView_poster);
        }
    }
}
