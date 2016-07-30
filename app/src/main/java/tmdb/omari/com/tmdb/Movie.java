package tmdb.omari.com.tmdb;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;


public class Movie implements Parcelable {

    static  int    id;
    static String title;
    static String original_title;
    static String overview;
    static String popularity;
    static String release_date;
    static String poster_path;
    static String backdrop_path;
    static Boolean fav;
    static String rating;
    static String vote_count;
    static  String tagline;
    static  String status;
    static  String youtube2;
    static ArrayList<String> comments;


    static String poster_pathF;


    public Movie() {
    }
    private Movie(Parcel parcel){
        this.id = parcel.readInt();
       // this.title = parcel.readString();
        this.original_title = parcel.readString();
        this.backdrop_path = parcel.readString();
        this.poster_path = parcel.readString();
        this.overview = parcel.readString();
        this.rating = parcel.readString();
        this.release_date = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getTitle() {
        Log.v("set", "*****get  title*****");
        return title;

    }

    public void setTitle(String title) {

        this.title = title;
        Log.v("set", "*****set  title*****");
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public Boolean getfav() {
        return fav;
    }

    public void setFav(Boolean fav) {
        this.fav = fav;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }





    public String getYoutube() {
        return status;
    }

    public void setYoutube(String status) {
        this.status = status;
    }

    public String getYoutube2() {
        return youtube2;
    }

    public void setYoutube2(String youtube2) {
        this.youtube2 = youtube2;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeString(this.original_title);
        dest.writeString(this.backdrop_path);
        dest.writeString(this.poster_path);
        dest.writeString(this.overview);
        dest.writeString(this.rating);
        dest.writeString(this.release_date);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };



    public String getPoster_pathF() {

        return poster_pathF;
    }

    public void setPoster_pathF(String poster_pathF) {
        this.poster_pathF = poster_pathF;
    }



}