package com.github.c4d3r.popularmovies.repository;

import com.github.c4d3r.popularmovies.model.MovieResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Maxim on 01/02/2017.
 */

public interface MovieInterface {

    @GET("movie/popular")
    Call<MovieResponse> listPopularMovies();

    @GET("movie/top_rated")
    Call<MovieResponse> listTopRatedMovies();

}
