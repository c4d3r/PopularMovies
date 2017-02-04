package com.github.c4d3r.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.c4d3r.popularmovies.model.Movie;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Maxim on 02/02/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private Movie _movie;

    private TextView _txtTitle;
    private TextView _txtScore;
    private TextView _txtPlot;
    private TextView _txtRelease;
    private ImageView _imgPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
    }



}
