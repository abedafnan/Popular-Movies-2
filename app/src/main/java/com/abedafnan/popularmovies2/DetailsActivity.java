package com.abedafnan.popularmovies2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abedafnan.popularmovies2.adapters.ReviewsAdapter;
import com.abedafnan.popularmovies2.adapters.TrailersAdapter;
import com.abedafnan.popularmovies2.data.AppDatabase;
import com.abedafnan.popularmovies2.data.MovieEntity;
import com.abedafnan.popularmovies2.models.Movie;
import com.abedafnan.popularmovies2.models.Review;
import com.abedafnan.popularmovies2.models.ReviewResponse;
import com.abedafnan.popularmovies2.models.Trailer;
import com.abedafnan.popularmovies2.models.TrailerResponse;
import com.abedafnan.popularmovies2.api.GetDataInterface;
import com.abedafnan.popularmovies2.utils.AppExecutors;
import com.abedafnan.popularmovies2.utils.NetworkUtils;
import com.abedafnan.popularmovies2.api.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Movie mMovie;

    private List<Trailer> mTrailers;
    private List<Review> mReviews;

    private TrailersAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewAdapter;

    private AppDatabase mDatabase;
    private List<MovieEntity> favoriteMovies;
    private MovieEntity mMovieToDelete;

    private static final String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mDatabase = AppDatabase.getInstance(this.getApplication());

        fab = findViewById(R.id.fav_fab);

        // Get the intent from MainActivity
        Intent intent = getIntent();
        if (intent != null) {
            mMovie = (Movie) intent.getSerializableExtra("Movie");
        }

        // Populate movie details
        populateUI(mMovie);

        // Generate RecyclerViews
        generateTrailersList();
        generateReviewsList();

        // Load Trailers and Reviews
        showTrailers(mMovie.getId());
        showReviews(mMovie.getId());

        setUpViewModel();
    }

    private void setUpViewModel() {
        // Declare a ViewModel variable and initialize it
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getFavourites().observe(this, new Observer<List<MovieEntity>>() {
            @Override
            public void onChanged(@Nullable final List<MovieEntity> taskEntries) {
                // Store the list of favorite movies to be used in
                // finding out if the movie is a favorite
                favoriteMovies = taskEntries;
            }
        });
    }

    private void populateUI(Movie movie) {
        ImageView poster = findViewById(R.id.iv_movie_poster);
        TextView title = findViewById(R.id.tv_movie_title);
        TextView date = findViewById(R.id.tv_release_date);
        TextView rating = findViewById(R.id.tv_user_rating);
        TextView synopsis = findViewById(R.id.tv_synopsis);

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + movie.getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.drawable.placeholder).into(poster);

        title.setText(movie.getOriginalTitle());
        date.setText(movie.getReleaseDate());
        rating.setText(movie.getVoteAverage() + "/10");
        synopsis.setText(movie.getOverview());
    }

    // Will be called when the favourite button is clicked
    public void favourite(View view) {
        if (isFavorite()) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.getDao().deleteFav(mMovieToDelete);
                }
            });

            Toast.makeText(DetailsActivity.this,
                    "Removed from favorites", Toast.LENGTH_SHORT).show();

        } else {
            String title = mMovie.getOriginalTitle();
            int id = mMovie.getId();
            final MovieEntity newFav = new MovieEntity(id, title);

            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDatabase.getDao().insertFav(newFav);
                }
            });

            Toast.makeText(DetailsActivity.this,
                    "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFavorite() {
        if (favoriteMovies != null) {
            Log.e("List<MovieEntity>", "GOOD");
            for (int i = 0; i < favoriteMovies.size(); i++) {
                // The movie id exist in the favorites list
                if (favoriteMovies.get(i).getId() == mMovie.getId()) {
                    mMovieToDelete = favoriteMovies.get(i);
                    return true;
                }
            }
        } else {
            Log.e("List<MovieEntity>", "favs is null");
        }

        return false;
    }

    private void generateTrailersList() {
        mTrailers = new ArrayList<>();
        RecyclerView trailersRecycler = findViewById(R.id.rv_trailers);
        trailersRecycler.setHasFixedSize(true);

        // Set the adapter
        mTrailerAdapter = new TrailersAdapter(this, mTrailers);
        trailersRecycler.setAdapter(mTrailerAdapter);

        // Set the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayout.HORIZONTAL, false);
        trailersRecycler.setLayoutManager(layoutManager);
    }

    private void generateReviewsList() {
        mReviews = new ArrayList<>();
        RecyclerView reviewsRecycler = findViewById(R.id.rv_reviews);
        reviewsRecycler.setHasFixedSize(true);

        // Set the adapter
        mReviewAdapter = new ReviewsAdapter(mReviews);
        reviewsRecycler.setAdapter(mReviewAdapter);

        // Set the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayout.HORIZONTAL, false);
        reviewsRecycler.setLayoutManager(layoutManager);
    }

    private void showTrailers(int id) {
        if (NetworkUtils.hasNetworkConnection(this)) {
            GetDataInterface service = RetrofitClient.getRetrofitInstance().create(GetDataInterface.class);
            Call<TrailerResponse> call = service.getMovieTrailers(id, API_KEY);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {

                    TrailerResponse trailerResponse = response.body();
                    if (trailerResponse != null) {
                        mTrailers.clear();
                        mTrailers.addAll(trailerResponse.getResults());
                        mTrailerAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<TrailerResponse> call, Throwable t) {
                    Log.e("onFailure", t.toString());
                    Toast.makeText(DetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showReviews(int id) {
        if (NetworkUtils.hasNetworkConnection(this)) {
            GetDataInterface service = RetrofitClient.getRetrofitInstance().create(GetDataInterface.class);
            Call<ReviewResponse> call = service.getMovieReviews(id, API_KEY);
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {

                    ReviewResponse reviewResponse = response.body();
                    if (reviewResponse != null) {
                        mReviews.clear();
                        mReviews.addAll(reviewResponse.getResults());
                        mReviewAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onFailure(Call<ReviewResponse> call, Throwable t) {
                    Log.e("onFailure", t.toString());
                    Toast.makeText(DetailsActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(DetailsActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
