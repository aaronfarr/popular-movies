package com.aaronfarr.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.aaronfarr.popularmovies.Movies.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // -----------
        Bundle extras = getIntent().getExtras();
        Movie movie = extras.getParcelable(getString(R.string.extra_index));
        // -----------
        int width = 500;
        String poster = movie.getPoster(width);
        ImageView iv_poster = findViewById(R.id.iv_detail_movie_poster);
        Picasso.with(this)
                .load(poster)
                .into(iv_poster);
        // -----------
        String backdrop = movie.getBackdrop();
        ImageView iv_backdrop = findViewById(R.id.iv_movie_backdrop);
        Picasso.with(this)
                .load(backdrop)
                .into(iv_backdrop);
        // -----------
        TextView tv_movie_rating = findViewById(R.id.tv_detail_rating);
        tv_movie_rating.setText( String.valueOf(movie.getRating()) );
        TextView tv_movie_title = findViewById(R.id.tv_detail_movie_title);
        tv_movie_title.setText( movie.getTitle() );
        //TextView tv_movie_votes = findViewById(R.id.tv_detail_votes);
        //tv_movie_votes.setText( String.valueOf(movie.getVotes()).concat(" ").concat(getString(R.string.detail_votes)) );
        TextView tv_movie_synopsis = findViewById(R.id.tv_detail_movie_synopsis);
        tv_movie_synopsis.setText( movie.getSynopsis() );
        TextView tv_movie_date = findViewById(R.id.tv_detail_movie_date);
        tv_movie_date.setText(
                getString(R.string.detail_release)
                .concat(movie.getYear())
                .concat(getString(R.string.detail_interpunct))
                .concat(movie.getVotes())
                .concat(getString(R.string.detail_votes))
        );
    }
}