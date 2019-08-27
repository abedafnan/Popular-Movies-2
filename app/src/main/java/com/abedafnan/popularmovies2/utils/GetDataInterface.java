package com.abedafnan.popularmovies2.utils;

import com.abedafnan.popularmovies2.models.MovieResponse;
import com.abedafnan.popularmovies2.models.ReviewResponse;
import com.abedafnan.popularmovies2.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataInterface {

    @GET("popular")
    Call<MovieResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
