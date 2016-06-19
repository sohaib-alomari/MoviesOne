package tmdb.omari.com.tmdb;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.activity_detail_activity_fragment, container, false);
        getActivity().setTitle("Movie Detail");

        Intent i=getActivity().getIntent();



        if(i != null && i.hasExtra("dates"))
        {
            date=i.getStringExtra("dates");
            TextView txt =(TextView) rootView.findViewById(R.id.date);
            txt.setText("Release Date: "+date );

        }

        if(i != null && i.hasExtra("rating"))
        {
            rating=i.getStringExtra("rating");
            TextView txt =(TextView) rootView.findViewById(R.id.rating);
            txt.setText(rating );

        }
        if(i != null && i.hasExtra("overview"))
        {
            overview=i.getStringExtra("overview");
            TextView description =(TextView) rootView.findViewById(R.id.desc);
            description.setText("Details:"+"\n"+overview );
        }
        if(i != null && i.hasExtra("title"))
        {
            title=i.getStringExtra("title");
            TextView txt =(TextView) rootView.findViewById(R.id.title);
            txt.setText(title );

        }
        if(i != null && i.hasExtra("poster"))
        {
            poster=i.getStringExtra("poster");
            ImageView img=(ImageView)rootView.findViewById(R.id.poster);

            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + poster).resize(
                    mainFragment.width, (int)(mainFragment.width*1.5)).into(img);

        }





        if(i !=null && i.hasExtra("youtube"))
        {
            youtube = i.getStringExtra("youtube");

        }
        if(i !=null && i.hasExtra("youtube2"))
        {
            youtube2 = i.getStringExtra(youtube2);
        }
        if(i !=null && i.hasExtra("comments"))
        {
            comments = i.getStringArrayListExtra("comments");
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
                divider.setBackgroundColor(Color.BLACK);

                tv.setText(comments.get(k));
                layout.addView(divider);
                layout.addView(tv);

                if(review == null)
                {
                    review = comments.get(k);
                }
                else{
                    review+="divider123" + comments.get(k);
                }
            }

        }
        fav = (Button)rootView.findViewById(R.id.favorite);
        if(i !=null && i.hasExtra("favorite"))
        {
            favorite = i.getBooleanExtra("favorite", false);
            if(!favorite)
            {
                fav.setText("FAVORITE");
                fav.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                fav.setText("UNFAVORITE");
                fav.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);
            }
        }

        return rootView;
    }


}
