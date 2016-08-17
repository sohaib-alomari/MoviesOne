package tmdb.omari.com.tmdb.Fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tmdb.omari.com.tmdb.Data.Movie;
import tmdb.omari.com.tmdb.Data.MovieProvider;
import tmdb.omari.com.tmdb.R;

public class DetailActivityFragment  extends Fragment    {


    public static String date;
    public static String rating;
    public static String overview;
    public static String title;
    public static String poster;
    public static String youtube;
    public static String youtube2;
    public static String review;
    public static ArrayList<String> comments;
    public static boolean favorite;
    public static Button fav;
    private ShareActionProvider mShareActionProvider;
    public static final String DETAIL_MOVIE = "DETAIL_MOVIE";
    public static final String TAG = DetailActivityFragment.class.getSimpleName();
    Context applicationContext;

    private String mMovie;
    public Movie movie;

    View rootView;

    public DetailActivityFragment(){

        setHasOptionsMenu(true);
    }

    private void setTitle(){
        movie=new Movie();

        TextView txt = (TextView) rootView.findViewById(R.id.title);
        txt.setText(movie.getTitle());
      // Log.v("title",  movie.getTitle());

    }

    private void setOverview()
    {

        TextView description =(TextView) rootView.findViewById(R.id.desc);
        description.setText("Details:"+"\n"+movie.getOverview() );
    }

   private void setDates()
    {

        TextView txt =(TextView) rootView.findViewById(R.id.date);
        txt.setText("Release Date: "+movie.getRelease_date() );

    }

    void setRating()
    {

        TextView txt =(TextView) rootView.findViewById(R.id.rating);
        txt.setText(movie.getRating() );

    }

    void favBtn(){

        if(!movie.getfav())
        {
            fav.setText("FAVORITE");
            fav.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

        }
        else{

            fav.setText("UNFAVORITE");
            fav.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
        }
    }

    private void setPoster()
    {

        ImageView img=(ImageView)rootView.findViewById(R.id.poster);

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path()).resize(
                MainFragment.width, (int)(MainFragment.width*1.5)).into(img);
    }

    void setTrailer()
    {
            youtube = movie.getYoutube();
    }
    void setTrailer2()
    {
        youtube2 = movie.getYoutube2();
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView= inflater.inflate(R.layout.activity_detail_activity_fragment, container, false);

        setTitle();
        Bundle arguments = getArguments();
        if (arguments != null) {
            if(arguments.containsKey("title")) {
                mMovie = arguments.getString("title");
                Log.v("arg", "*****arg  OK*****");
                title = mMovie;


            }
        }





        Intent i=getActivity().getIntent();


    try{

    }
    catch(Exception e)
    {

    }




        try {
            favBtn();
        }
        catch (Exception e)
        {

        }




        Button trailer=(Button)rootView.findViewById(R.id.trailer1);
        trailer.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent viewTrailer=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+DetailActivityFragment.youtube));
                startActivity(viewTrailer);

            }
        });


        Button trailer2=(Button)rootView.findViewById(R.id.trailer2);
        trailer2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent viewTrailer=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+DetailActivityFragment.youtube2));
                startActivity(viewTrailer);

            }
        });



fav = (Button)rootView.findViewById(R.id.favorite);
        fav.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //Do stuff here


                if(fav.getText().equals("FAVORITE"))
                {
                    //code to store movie data in database
                   fav.setText("UNFAVORITE");
                    fav.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);

                    ContentValues values = new ContentValues();
                    values.put(MovieProvider.NAME,movie.getPoster_path());
                    values.put(MovieProvider.OVERVIEW,movie.getOverview());
                    values.put(MovieProvider.RATING,movie.getRating());
                    values.put(MovieProvider.DATE,movie.getRelease_date());
                    values.put(MovieProvider.REVIEW,convertyListToString(comments));
                    values.put(MovieProvider.YOUTUBE1,DetailActivityFragment.youtube);
                    values.put(MovieProvider.YOUTUBE2,DetailActivityFragment.youtube2);
                    values.put(MovieProvider.TITLE,movie.getTitle());



                    getActivity().getContentResolver().insert(MovieProvider.CONTENT_URI, values);


                }
                else
                {
                   fav.setText("FAVORITE");
                    fav.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

                    getActivity().getContentResolver().delete(Uri.parse("content://tmdb.omari.com.tmdb.Movies/movies"),
                     "title=?",new String[]{movie.getTitle()});
                }



            }
        });



        return rootView;
    }

    public String convertyListToString(ArrayList<String> list)
    {
        String wString=" ";

        for (String s : list)
        {
            wString += s + "divider";
        }



        return wString;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            favBtn();
            setComments();
            setTitle();
            setPoster();
            setOverview();
            setDates();
            setRating();
            setTrailer();
            setTrailer2();

        }
        catch (Exception xception)
        {

        }






    }




    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        if(mShareActionProvider !=null)
        {
            mShareActionProvider.setShareIntent(createShareIntent());
        }

    }

    private Intent createShareIntent()
    {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this trailer for " + title + ": " +
                "https://www.youtube.com/watch?v=" + youtube);
        return shareIntent;
    }


   void setComments()
    {
        comments = movie.getComments();
        for(int k = 0; k<comments.size();k++)
        {
            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linear);
            View divider = new View(getActivity());
            TextView tv = new TextView(getActivity());
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(p);
            int paddingPixel = 10;
            float density = getActivity().getResources().getDisplayMetrics().density;
            int paddingDP = (int) (paddingPixel * density);
            tv.setPadding(0, paddingDP, 0, paddingDP);
            RelativeLayout.LayoutParams x = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            x.height = 1;
            divider.setLayoutParams(x);
            divider.setBackgroundColor(Color.WHITE);
            tv.setTextColor(Color.WHITE);
            tv.setText(comments.get(k));
            layout.addView(divider);
            layout.addView(tv);

            if(review == null)
            {
                review = comments.get(k);
            }
            else{
                review+="divider" + comments.get(k);
            }
        }

    }









}
