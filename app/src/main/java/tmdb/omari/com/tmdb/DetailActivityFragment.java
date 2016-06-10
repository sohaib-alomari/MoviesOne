package tmdb.omari.com.tmdb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivityFragment  extends Fragment    {


    public static String date;
    public static String rating;
    public static String overview;
    public static String title;
    public static String poster;

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



       return rootView;
    }


}
