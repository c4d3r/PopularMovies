package com.github.c4d3r.popularmovies.service;

import com.github.c4d3r.popularmovies.repository.MovieInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maxim on 01/02/2017.
 */

public class MovieService {

    private static final String API_KEY = "YOUR API KEY";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private MovieInterface repository;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public MovieService() {

        // add interceptor to add api_key
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                HttpUrl url = chain.request().url()
                        .newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build();
                Request request = chain.request().newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        repository = retrofit.create(MovieInterface.class);

    }

    public MovieInterface getRepository() {
        return repository;
    }
}
