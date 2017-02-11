package com.github.c4d3r.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.c4d3r.popularmovies.R;
import com.github.c4d3r.popularmovies.model.MovieReview;

import java.util.List;

/**
 * Created by Maxim on 10/02/2017.
 */

public class MovieReviewAdapter extends ArrayAdapter<MovieReview> {

    private List<MovieReview> _reviews;
    private LayoutInflater _inflater;
    private Context _context;

    public MovieReviewAdapter(Activity context, List<MovieReview> reviews) {
        super(context, 0, reviews);
        _reviews = reviews;
        this._inflater = LayoutInflater.from(context);
        this._context = context;
    }

    private static class ViewHolder {
        TextView author;
        TextView reviewText;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieReview review = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = _inflater.inflate(R.layout.item_review, parent, false);

            viewHolder.author = (TextView)convertView.findViewById(R.id.txt_review_author);
            viewHolder.reviewText = (TextView) convertView.findViewById(R.id.txt_review_text);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.author.setText(String.format(
                _context.getResources().getString(R.string.dynamic_review_author),
                review.getAuthor()));

        viewHolder.reviewText.setText(review.getContent());

        return convertView;
    }
}
