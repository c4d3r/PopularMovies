package com.github.c4d3r.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.github.c4d3r.popularmovies.adapter.MovieAdapter;
import com.github.c4d3r.popularmovies.model.Movie;
import com.github.c4d3r.popularmovies.model.MovieResponse;
import com.github.c4d3r.popularmovies.service.MovieService;
import com.github.c4d3r.popularmovies.util.NetworkUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private GridView _gvMovies;
    private MovieService _movieService;
    private MovieAdapter _movieAdapter;
    private List<Movie> _movies = new ArrayList<>();
    private int _currentFilter = R.id.menu_filter_popular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _gvMovies = (GridView)findViewById(R.id.gvMovies);

        // load movies
        Log.d("MainActivity", "Loading movies...");
        _movieService = new MovieService();
        getMovies(_movieService.getRepository().listPopularMovies());

        _movieAdapter = new MovieAdapter(this, _movies);
        _gvMovies.setAdapter(_movieAdapter);
    }

    public void getMovies(Call<MovieResponse> movies) {

        if(!NetworkUtil.isOnline(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please make sure you have internet access!", Toast.LENGTH_LONG).show();
            return;
        }

        final Activity activity = this;

        movies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                _movies.clear();
                _movies.addAll(response.body().getResults());
                _movieAdapter.notifyDataSetChanged();

                _gvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(activity, DetailActivity.class);
                        Movie selectedMovie = _movies.get(i);
                        intent.putExtra("movie", selectedMovie);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("MainActivity", t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == _currentFilter) return super.onOptionsItemSelected(item);

        _currentFilter = id;

        switch(id) {
            case R.id.menu_filter_popular:
                getMovies(_movieService.getRepository().listPopularMovies());
                break;
            case R.id.menu_filter_topRated:
                getMovies(_movieService.getRepository().listTopRatedMovies());
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
