package com.github.c4d3r.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.c4d3r.popularmovies.R;
import com.github.c4d3r.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by Maxim on 01/02/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> _movies;

    final private MovieAdapterOnClickHandler _onClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie selectedMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler onClickHandler) {
        _onClickHandler = onClickHandler;
    }

    // view lookup cache
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView poster;
        public final TextView title;

        public MovieAdapterViewHolder(View view) {
            super(view);
            poster = (ImageView) view.findViewById(R.id.movie_poster);
            title = (TextView) view.findViewById(R.id.movie_title);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            _onClickHandler.onClick(_movies.get(getAdapterPosition()));
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.movie_item, viewGroup, false);
        view.setFocusable(true);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Movie movie = _movies.get(position);
        // poster is imageview so will have a context
        Glide.with(holder.poster.getContext()).load(movie.getPosterPath()).fitCenter().into(holder.poster);
        holder.title.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if(null == _movies) return 0;
        return _movies.size();
    }

    public void setMovies(List<Movie> movies) {
        _movies = movies;
        notifyDataSetChanged();
    }

}
