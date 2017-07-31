package com.aaronfarr.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.aaronfarr.popularmovies.movies.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @BindString(R.string.extra_index) String strExtraIndex;
    @BindView(R.id.iv_detail_movie_poster) ImageView ivPoster;
    @BindView(R.id.iv_movie_backdrop) ImageView ivBackdrop;
    @BindView(R.id.tv_detail_rating) TextView tvMovieRating;
    @BindView(R.id.tv_detail_movie_title) TextView tvMovieTitle;
    @BindView(R.id.tv_detail_movie_synopsis) TextView tvMovieSynopsis;
    @BindView(R.id.tv_detail_movie_date) TextView tvMovieDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        // -----------
        Bundle extras = getIntent().getExtras();
        Movie movie = extras.getParcelable(strExtraIndex);
        // -----------
        int width = 500;
        String poster = movie.getPoster(width);
        Picasso.with(this)
                .load(poster)
                .error(R.drawable.poster_placeholder)
                .placeholder(R.drawable.poster_placeholder)
                .into(ivPoster);
        // -----------
        String backdrop = movie.getBackdrop();
        Picasso.with(this)
                .load(backdrop)
                .error(R.drawable.backdrop_placeholder)
                .placeholder(R.drawable.backdrop_placeholder)
                .into(ivBackdrop);
        // -----------
        tvMovieRating.setText( String.valueOf(movie.getRating()) );
        tvMovieTitle.setText( movie.getTitle() );
        tvMovieSynopsis.setText( movie.getSynopsis() );
        tvMovieDate.setText(
                getString(R.string.detail_release)
                .concat(movie.getYear())
                .concat(getString(R.string.detail_interpunct))
                .concat(movie.getVotes())
                .concat(getString(R.string.detail_votes))
        );
    }
}