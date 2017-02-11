package com.github.c4d3r.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.c4d3r.popularmovies.adapter.MovieReviewAdapter;
import com.github.c4d3r.popularmovies.adapter.MovieTrailerAdapter;
import com.github.c4d3r.popularmovies.data.MovieContract;
import com.github.c4d3r.popularmovies.model.Movie;
import com.github.c4d3r.popularmovies.model.MovieReview;
import com.github.c4d3r.popularmovies.model.MovieTrailer;
import com.github.c4d3r.popularmovies.model.Response;
import com.github.c4d3r.popularmovies.service.MovieService;
import com.github.c4d3r.popularmovies.util.NetworkUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Maxim on 02/02/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private Movie _movie;
    private List<MovieReview> _reviews = new ArrayList<>();
    private List<MovieTrailer> _trailers = new ArrayList<>();

    private TextView _txtTitle;
    private TextView _txtScore;
    private TextView _txtPlot;
    private TextView _txtRelease;
    private ImageView _imgPoster;

    private ListView _lvReviews;
    private RecyclerView _lvTrailers;

    private MovieReviewAdapter _movieReviewAdapter;
    private MovieTrailerAdapter _movieTrailerAdapter;

    private boolean _isFavourite = false;

    private static final int ID_FAVOURITE_LOADER = 300; // loader ids start at 300

    private Uri _uri;

    private Menu _menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        _movieReviewAdapter = new MovieReviewAdapter(this, _reviews);
        _movieTrailerAdapter = new MovieTrailerAdapter(new MovieTrailerAdapter.MovieTrailerAdapterOnClickListener() {
            @Override
            public void onView(MovieTrailer selectedTrailer) {
                // launch intent to trailer
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(selectedTrailer.getUrl())
                ));
            }
        });
        _movieTrailerAdapter.setTrailers(_trailers);

        _txtTitle = (TextView)findViewById(R.id.txt_movie_title);
        _txtScore = (TextView)findViewById(R.id.txt_movie_rating);
        _txtPlot = (TextView)findViewById(R.id.txt_movie_plot);
        _txtRelease = (TextView)findViewById(R.id.txt_movie_release);
        _imgPoster = (ImageView)findViewById(R.id.img_movie_poster);

        _movie = (Movie)getIntent().getExtras().getParcelable("movie");

        setTitle(_movie.getTitle());

        _txtTitle.setText(_movie.getTitle());
        _txtScore.setText(String.format("%s: %.2f", getApplicationContext().getString(R.string.movie_detail_score), _movie.getVoteAverage()));
        _txtPlot.setText(_movie.getOverview());
        _txtRelease.setText((new SimpleDateFormat("dd/MM/yyyy").format(_movie.getReleaseDate())));

        Glide.with(this).load(_movie.getPosterPath()).fitCenter().into(_imgPoster);

        _lvReviews = (ListView) findViewById(R.id.lv_reviews);
        _lvReviews.setAdapter(_movieReviewAdapter);

        _lvTrailers = (RecyclerView)findViewById(R.id.lv_trailers);
        _lvTrailers.setLayoutManager(new LinearLayoutManager(this));
        _lvTrailers.setAdapter(_movieTrailerAdapter);


        MovieService service = new MovieService();
        loadReviews(service.getRepository().listReviews(_movie.getId()));
        loadTrailers(service.getRepository().listTrailers(_movie.getId()));

        _isFavourite = isFavourite();
    }

    public void loadReviews(Call<Response<MovieReview>> movies) {
        if(!NetworkUtil.isOnline(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please make sure you have internet access!", Toast.LENGTH_LONG).show();
            return;
        }

        movies.enqueue(new Callback<Response<MovieReview>>() {
            @Override
            public void onResponse(Call<Response<MovieReview>> call, retrofit2.Response<Response<MovieReview>> response) {
                _reviews.clear();
                _reviews.addAll(response.body().getResults());
                _movieReviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response<MovieReview>> call, Throwable t) {
                Log.e("DetailActivity", t.getMessage());
            }
        });
    }

    public void loadTrailers(Call<Response<MovieTrailer>> movies) {
        if(!NetworkUtil.isOnline(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please make sure you have internet access!", Toast.LENGTH_LONG).show();
            return;
        }

        movies.enqueue(new Callback<Response<MovieTrailer>>() {
            @Override
            public void onResponse(Call<Response<MovieTrailer>> call, retrofit2.Response<Response<MovieTrailer>> response) {
                _trailers.clear();
                _trailers.addAll(response.body().getResults());
                _movieTrailerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Response<MovieTrailer>> call, Throwable t) {
                Log.e("DetailActivity", t.getMessage());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);
        this._menu = menu;
        MenuItem item = menu.findItem(R.id.menu_item_favourite);
        item.setTitle(_isFavourite ?
                getApplicationContext().getString(R.string.menu_item_unfavourite_label) :
                getApplicationContext().getString(R.string.menu_item_favourite_label));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_item_favourite:
                if(isFavourite()) {
                    removeFavourite();
                } else {
                    makeFavourite();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void makeFavourite() {

        ContentValues favouriteValue = new ContentValues();
        favouriteValue.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_TITLE, _movie.getTitle());
        favouriteValue.put(MovieContract.FavouriteEntry.COLUMN_MOVIE_ID, _movie.getId());

        getApplicationContext().getContentResolver().insert(
                MovieContract.FavouriteEntry.CONTENT_URI,
                favouriteValue
        );

        _menu.findItem(R.id.menu_item_favourite)
                .setTitle(getApplicationContext().getResources().getString(R.string.menu_item_unfavourite_label));
    }

    public void removeFavourite() {
        getApplicationContext().getContentResolver().delete(
                Uri.parse(MovieContract.FavouriteEntry.CONTENT_URI + "/" + Integer.toString(_movie.getId())),
                null,
                null
        );

        _menu.findItem(R.id.menu_item_favourite)
                .setTitle(getApplicationContext().getResources().getString(R.string.menu_item_favourite_label));
    }

    public boolean isFavourite() {
        String[] proj = {MovieContract.FavouriteEntry.COLUMN_MOVIE_ID};
        Uri favouriteUri = MovieContract.FavouriteEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(_movie.getId())).build();
        Cursor c = getApplicationContext().getContentResolver().query(favouriteUri, proj, null, null, null);
        return c.getCount() > 0;
    }
}
