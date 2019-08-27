package com.abedafnan.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abedafnan.popularmovies2.adapters.MoviesAdapter;
import com.abedafnan.popularmovies2.adapters.ReviewsAdapter;
import com.abedafnan.popularmovies2.adapters.TrailersAdapter;
import com.abedafnan.popularmovies2.models.Movie;
import com.abedafnan.popularmovies2.models.MovieResponse;
import com.abedafnan.popularmovies2.models.Review;
import com.abedafnan.popularmovies2.models.ReviewResponse;
import com.abedafnan.popularmovies2.models.Trailer;
import com.abedafnan.popularmovies2.models.TrailerResponse;
import com.abedafnan.popularmovies2.utils.GetDataInterface;
import com.abedafnan.popularmovies2.utils.NetworkUtils;
import com.abedafnan.popularmovies2.utils.RetrofitClient;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    private List<Trailer> mTrailers;
    private List<Review> mReviews;

    private TrailersAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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
    }

    public void populateUI(Movie movie) {
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
            Call<TrailerResponse> call = service.getMovieTrailers(id, Constants.API_KEY);
            call.enqueue(new Callback<TrailerResponse>() {
                @Override
                public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {

                    TrailerResponse trailerResponse = response.body();
                    if (trailerResponse != null) {
                        mTrailers.clear();
                        mTrailers.addAll(trailerResponse.getResults());
                        mTrailerAdapter.notifyDataSetChanged();
                    }
                    Log.d("NETWORK", "NULL response");
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
            Call<ReviewResponse> call = service.getMovieReviews(id, Constants.API_KEY);
            call.enqueue(new Callback<ReviewResponse>() {
                @Override
                public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {

                    ReviewResponse reviewResponse = response.body();
                    if (reviewResponse != null) {
                        mReviews.clear();
                        mReviews.addAll(reviewResponse.getResults());
                        mReviewAdapter.notifyDataSetChanged();
                    }
                    Log.d("NETWORK", "NULL response");
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
