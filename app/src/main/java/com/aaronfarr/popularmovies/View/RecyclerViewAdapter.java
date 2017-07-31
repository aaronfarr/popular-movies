package com.aaronfarr.popularmovies.View;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.aaronfarr.popularmovies.Movies.Movie;
import com.aaronfarr.popularmovies.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewItem> {

    private Context context;
    private ArrayList<Movie> movies = new ArrayList<>();
    private final RecyclerViewAdapterOnClickHandler mClickHandler;

    public RecyclerViewAdapter( Context context, ArrayList<Movie> filteredMovies, RecyclerViewAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.movies = filteredMovies;
        this.mClickHandler = clickHandler;
    }

    @Override
    public RecyclerViewItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,null);
        RecyclerViewItem item = new RecyclerViewItem(itemView);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerViewItem holder, int position) {
        Movie movie = this.movies.get(position);
        int intPosterWidth = 500;
        Picasso.with(context)
                .load(movie.getPoster(intPosterWidth))
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                .into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface RecyclerViewAdapterOnClickHandler {
        void onClick(int position);
    }

    public class RecyclerViewItem extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_movie_poster) ImageView moviePoster;

        public RecyclerViewItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}