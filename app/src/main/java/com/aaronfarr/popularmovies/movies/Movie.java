package com.aaronfarr.popularmovies.movies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class Movie implements Parcelable {
    private int mId;
    private String mTitle;
    private int mVotes;
    private String mSynopsis;
    private double mRating;
    private String mRelease;
    private String mPoster;
    private String mBackdrop;

    // Parcelable - Unwrap Parcel
    public Movie(Parcel in) {
        this.mId = in.readInt();
        this.mTitle = in.readString();
        this.mVotes = in.readInt();
        this.mSynopsis = in.readString();
        this.mRating = in.readDouble();
        this.mPoster = in.readString();
        this.mBackdrop = in.readString();
        this.mRelease = in.readString();
    }

    // Constructor
    public Movie(JSONObject json ) throws JSONException {
        final String MOVIE_ID_NODE = "id";
        final String MOVIE_TITLE_NODE = "title";
        final String MOVIE_VOTES_NODE = "vote_count";
        final String MOVIE_OVERVIEW_NODE = "overview";
        final String MOVIE_RATING_NODE = "vote_average";
        final String MOVIE_POSTER_NODE = "poster_path";
        final String MOVIE_BACKDROP_NODE = "backdrop_path";
        final String MOVIE_RELEASE_NODE = "release_date";
        this.mId = json.getInt(MOVIE_ID_NODE);
        this.mTitle = json.getString(MOVIE_TITLE_NODE);
        this.mVotes = json.getInt(MOVIE_VOTES_NODE);
        this.mSynopsis = json.getString(MOVIE_OVERVIEW_NODE);
        this.mRating = json.getDouble(MOVIE_RATING_NODE);
        this.mPoster = json.getString(MOVIE_POSTER_NODE);
        this.mBackdrop = json.getString(MOVIE_BACKDROP_NODE);
        this.mRelease = json.getString(MOVIE_RELEASE_NODE);
    }

    // Retrieve the full movie poster image URL
    public String getPoster( int width ) {
        String strWidth = String.valueOf(width);
        if(null != mPoster) {
            return "http://image.tmdb.org/t/p/w"
                    .concat(strWidth)
                    .concat("/")
                    .concat(mPoster);
        }
        return "";
    }

    // Retrieve the full movie backdrop image URL
    public String getBackdrop() {
        if( null == mBackdrop ) return "";
        String baseURL = "http://image.tmdb.org/t/p/w1280/";
        return baseURL.concat(mBackdrop);
    }
    public String getTitle() {
        return null != mTitle ? mTitle : "";
    }
    public String getSynopsis() {
        return null != mSynopsis ? mSynopsis : "";
    }
    public double getRating() {
        return mRating;
    }
    public String getVotes() {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(mVotes);
    }
    public int getVoteCount() {
        return mVotes;
    }
    public String getYear() {
        String [] dateParts = mRelease.split("-");
        if( null == dateParts[0] ) {
            return "";
        } else {
            return dateParts[0];
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable - Wrap Parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mTitle);
        parcel.writeInt(mVotes);
        parcel.writeString(mSynopsis);
        parcel.writeDouble(mRating);
        parcel.writeString(mPoster);
        parcel.writeString(mBackdrop);
        parcel.writeString(mRelease);
    }

    // Parcel Creator
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}