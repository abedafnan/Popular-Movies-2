package com.abedafnan.popularmovies2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.abedafnan.popularmovies2.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent != null) {
            mMovie = (Movie) intent.getSerializableExtra("Movie");
        }

        ImageView poster = findViewById(R.id.iv_movie_poster);
        TextView title = findViewById(R.id.tv_movie_title);
        TextView date = findViewById(R.id.tv_release_date);
        TextView rating = findViewById(R.id.tv_user_rating);
        TextView synopsis = findViewById(R.id.tv_synopsis);

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + mMovie.getPosterPath();
        Picasso.get().load(posterUrl).placeholder(R.drawable.placeholder).into(poster);

        title.setText(mMovie.getOriginalTitle());
        date.setText(mMovie.getReleaseDate());
        rating.setText(mMovie.getVoteAverage() + "/10");
        synopsis.setText(mMovie.getOverview());

    }
}
