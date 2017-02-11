package com.github.c4d3r.popularmovies.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.c4d3r.popularmovies.R;
import com.github.c4d3r.popularmovies.model.MovieTrailer;

import java.util.List;

/**
 * Created by Maxim on 10/02/2017.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    private List<MovieTrailer> _trailers;

    final private MovieTrailerAdapterOnClickListener _onClickListener;

    public MovieTrailerAdapter(MovieTrailerAdapterOnClickListener onClickListener) {
        _onClickListener = onClickListener;
    }

    public interface MovieTrailerAdapterOnClickListener {
        void onView(MovieTrailer selectedTrailer);
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView title;
        public final Button trailerLink;

        public MovieTrailerViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_trailer_title);
            trailerLink = (Button) view.findViewById(R.id.btn_trailer_link);
            trailerLink.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            _onClickListener.onView(_trailers.get(getAdapterPosition()));
        }
    }

    @Override
    public MovieTrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.item_trailer, viewGroup, false);
        view.setFocusable(true);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerViewHolder holder, int position) {
        MovieTrailer trailer = _trailers.get(position);
        holder.title.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if(null == _trailers) return 0;
        return _trailers.size();
    }

    public void setTrailers(List<MovieTrailer> trailers) {
        _trailers = trailers;
        notifyDataSetChanged();
    }
}
