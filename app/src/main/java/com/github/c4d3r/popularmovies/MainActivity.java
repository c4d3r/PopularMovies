package com.github.c4d3r.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.c4d3r.popularmovies.adapter.MovieAdapter;
import com.github.c4d3r.popularmovies.model.Movie;
import com.github.c4d3r.popularmovies.model.Response;
import com.github.c4d3r.popularmovies.service.MovieService;
import com.github.c4d3r.popularmovies.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Recommended Dependency injection
 * Butterknife (http://jakewharton.github.io/butterknife/)
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView _gvMovies;
    private MovieService _movieService;
    private MovieAdapter _movieAdapter;
    private List<Movie> _movies = new ArrayList<>();
    private int _currentFilter = R.id.menu_filter_popular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Activity activity = this;


        _movieAdapter = new MovieAdapter(new MovieAdapter.MovieAdapterOnClickHandler() {
            @Override
            public void onClick(Movie selectedMovie) {
                Log.d("MainActivity", "Clicked movie");
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra("movie", selectedMovie);
                startActivity(intent);
            }
        });

        _movieAdapter.setMovies(_movies);

        _gvMovies = (RecyclerView) findViewById(R.id.gvMovies);
        _gvMovies.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        _gvMovies.setAdapter(_movieAdapter);

        // load movies
        Log.d("MainActivity", "Loading movies...");
        _movieService = new MovieService();
        getMovies(_movieService.getRepository().listPopularMovies());

    }

    public void getMovies(Call<Response<Movie>> movies) {

        if(!NetworkUtil.isOnline(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please make sure you have internet access!", Toast.LENGTH_LONG).show();
            return;
        }

        movies.enqueue(new Callback<Response<Movie>>() {
            @Override
            public void onResponse(Call<Response<Movie>> call, retrofit2.Response<Response<Movie>> response) {
                _movies.clear();
                _movies.addAll(response.body().getResults());
                _movieAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response<Movie>> call, Throwable t) {
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
