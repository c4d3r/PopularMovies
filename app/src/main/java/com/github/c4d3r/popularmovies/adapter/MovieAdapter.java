package com.github.c4d3r.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.c4d3r.popularmovies.R;
import com.github.c4d3r.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Maxim on 01/02/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context _context;
    private List<Movie> _movies;
    private LayoutInflater _inflater;

    public MovieAdapter(Activity context, List<Movie> movies) {
        super(context, 0, movies);
        this._context = context;
        this._movies = movies;
        this._inflater = LayoutInflater.from(context);
    }

    // view lookup cache
    private static class ViewHolder {
        ImageView poster;
        TextView title;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = _inflater.inflate(R.layout.movie_item, parent, false);

            viewHolder.poster = (ImageView) convertView.findViewById(R.id.movie_poster);
            viewHolder.title = (TextView) convertView.findViewById(R.id.movie_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(_context).load(movie.getPosterPath()).fitCenter().into(viewHolder.poster);
        viewHolder.title.setText(movie.getTitle());

        return convertView;
    }
}
