package com.github.c4d3r.popularmovies.repository;

import com.github.c4d3r.popularmovies.model.Movie;
import com.github.c4d3r.popularmovies.model.MovieReview;
import com.github.c4d3r.popularmovies.model.MovieTrailer;
import com.github.c4d3r.popularmovies.model.Response;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Maxim on 01/02/2017.
 */

public interface MovieInterface {

    @GET("movie/popular")
    Call<Response<Movie>> listPopularMovies();

    @GET("movie/top_rated")
    Call<Response<Movie>> listTopRatedMovies();

    @GET("movie/{id}/reviews")
    Call<Response<MovieReview>> listReviews(@Path("id") int id);

    @GET("movie/{id}/videos")
    Call<Response<MovieTrailer>> listTrailers(@Path("id") int id);

}
