package com.aaronfarr.popularmovies;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aaronfarr.popularmovies.Movies.Movie;
import com.aaronfarr.popularmovies.Movies.Movies;
import com.aaronfarr.popularmovies.Network.NetworkUtilities;
import com.aaronfarr.popularmovies.Network.RestApi;
import com.aaronfarr.popularmovies.View.RecyclerViewAdapter;
import com.squareup.leakcanary.LeakCanary;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.ExecutionException;
import butterknife.BindString;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterOnClickHandler {
    @BindString(R.string.extra_index) String strExtraIndex;
    @BindString(R.string.the_movie_db_api_key) String strApiKey;
    private boolean mMovieDataLoaded;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Movies mMovies;
    private int mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ----- Leak Canary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(getApplication());
        // ----- Butter Knife
        ButterKnife.bind(this);
        // ----- Attempt to load movie data
        mMovieDataLoaded = false;
        mMovies = new Movies();
        loadMovieData();
    }

    private void loadMovieData() {
        Context context = this;
        // ----------- Initiate "loading" state UI
        final ProgressBar progressBar = findViewById(R.id.pb_data_loading);
        final TextView textView = findViewById(R.id.tv_refresh_message);
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.INVISIBLE);
        // ----------- Check Connectivity
        boolean connectivity = NetworkUtilities.checkConnectivity(context);
        if( connectivity ) {
            try {
                String api_url = "https://api.themoviedb.org/3/movie/popular?api_key=".concat(strApiKey);
                JSONObject popularMoviesJSON  = RestApi.get(api_url);
                mMovies.parseJSON( popularMoviesJSON );
                mMovieDataLoaded = true;
                // ----------- Update UI
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                // ----------- Load Recycler View
                createMovieGrid();
            } catch (ExecutionException | InterruptedException e) {
                displayDialog(
                        getString(R.string.dialog_title_interrupted_exception),
                        getString(R.string.dialog_description_interrupted_exception)
                );
            } catch (JSONException e) {
                displayDialog(
                        getString(R.string.dialog_title_parse_exception),
                        getString(R.string.dialog_description_parse_exception)
                );
            }
        } else {
            displayDialog(
                    getString(R.string.dialog_title_no_connection),
                    getString(R.string.dialog_description_no_connection)
            );
        }
    }

    private void displayDialog( String title, String description ) {
        Context context = this;
        final ProgressBar progressBar = findViewById(R.id.pb_data_loading);
        final TextView textView = findViewById(R.id.tv_refresh_message);
        // ----------- Hide the progress bar
        progressBar.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.VISIBLE);
        // ----------- Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(description)
                .setTitle(title);
        builder.setPositiveButton(R.string.dialog_retry, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                loadMovieData();
            }
        });
        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.VISIBLE);
                dialogInterface.cancel();
            }
        });
        // ----------- Display Dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.menu_refresh:
                loadMovieData();
                return true;
            case R.id.menu_popular:
                mFilter = mMovies.MOVIES_FILTER_POPULAR;
                updateMovieGrid();
                return true;
            case R.id.menu_top_rated:
                mFilter = mMovies.MOVIES_FILTER_TOP_RATED;
                updateMovieGrid();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem refresh = menu.findItem(R.id.menu_refresh);
        MenuItem popular = menu.findItem(R.id.menu_popular);
        MenuItem top_rated = menu.findItem(R.id.menu_top_rated);
        popular.setVisible(mMovieDataLoaded);
        top_rated.setVisible(mMovieDataLoaded);
        refresh.setVisible(!mMovieDataLoaded);
        return super.onPrepareOptionsMenu(menu);
    }

    private void createMovieGrid() {
        Context context = this;
        GridLayoutManager layoutManager =
                new GridLayoutManager(context, numberOfColumns());
        RecyclerView mRecyclerView = findViewById(R.id.rv_movies);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerViewAdapter(context, mMovies.getMovies(), this);
        mRecyclerView.setAdapter(recyclerViewAdapter);
    }
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
    private void updateMovieGrid() {
        if( mFilter > 0 ) mMovies.filter(mFilter);
        if( null != recyclerViewAdapter )
            recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(int position) {
        Context context = this;
        Intent intent = new Intent(context, DetailActivity.class);
        Movie movie = mMovies.get(position);
        intent.putExtra(strExtraIndex, movie);
        startActivity( intent );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateMovieGrid();
    }
}