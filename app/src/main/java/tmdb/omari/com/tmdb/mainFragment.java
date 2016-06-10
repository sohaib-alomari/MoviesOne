package tmdb.omari.com.tmdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class mainFragment extends Fragment {

    static GridView gridView;
    static int width;
    static String API_KEY="1f1e4e852b00dc660803e835ee5fc600";
    static ArrayList<String> posters;
    static boolean sortbyp=false;

    static PreferenceChangeListener listener;
    static SharedPreferences prefs;
    static ArrayList<String> overviews;
    static ArrayList<String> titles;
    static ArrayList<String> dates;
    static ArrayList<String> ratings;






    public mainFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //We have to get the size of the screen to determain if it's a tablet of a phone
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        if (MainActivity.TABLET) {
            width = size.x /6;

        } else
            width = size.x /3;

        if (getActivity() != null) {
            ArrayList<String> arrayList = new ArrayList<String>();
            ImageAdapter adapter=new ImageAdapter(getActivity(),arrayList,width);
            gridView = (GridView) rootView.findViewById(R.id.gridview);
            gridView.setColumnWidth(width);
            gridView.setAdapter(adapter);


        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent i=new Intent(getActivity(),DetailActivity.class).
                        putExtra("overview",overviews.get(position)).
                        putExtra("poster", posters.get(position)).
                        putExtra("title",titles.get(position)).
                        putExtra("dates",dates.get(position)).
                        putExtra("rating",ratings.get(position));
                startActivity(i);

            }
        });


        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();
        prefs= PreferenceManager.getDefaultSharedPreferences( getActivity());
        listener= new PreferenceChangeListener();
        prefs.registerOnSharedPreferenceChangeListener(listener);

        if(prefs.getString("sortby","").equals("popularity"))
        {
            getActivity().setTitle("Most Popular Movies");
            sortbyp=true;

        }
        else if(prefs.getString("sortby","").equals("rating"))
        {
            getActivity().setTitle("Highest Rated Movies");
            sortbyp=false;

        }



        TextView txt=new TextView(getActivity());
        RelativeLayout relativeLayout=(RelativeLayout)getActivity().findViewById(R.id.relativelayout);





        if (isNetworkAvailable()) {
            //we made it work in the background thread because it can freeze the main thread till all the images are loaded so it needs to be done in the background
            gridView.setVisibility(GridView.VISIBLE);
           new ImageLoadTask().execute();
        }

        //just in case there is no internet Connection
        else {
            TextView tv = new TextView(getActivity());
             relativeLayout = (RelativeLayout) getActivity().findViewById(R.id.relativelayout);
            tv.setText("There is No Internet Connection!");

            if (relativeLayout.getChildCount() == 1) {
                relativeLayout.addView(tv);
            }
            gridView.setVisibility(GridView.GONE);
        }
    }


    public boolean isNetworkAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();

    }


    public class ImageLoadTask extends AsyncTask<Void, Void, ArrayList<String>> {


        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            while(true){
                try{
                    posters = new ArrayList(Arrays.asList(getPathsFromAPI(sortbyp)));
                    return posters;
                }
                catch(Exception e)
                {
                    continue;
                }
            }

        }
        @Override
        protected void onPostExecute(ArrayList<String>result)
        {
            if(result!=null && getActivity()!=null)
            {
                ImageAdapter adapter = new ImageAdapter(getActivity(),result, width);
                gridView.setAdapter(adapter);

            }
        }
        public String[] getPathsFromAPI(boolean sortByPop)
        {

            while(true)
            {

                HttpURLConnection urlConnection=null;
                BufferedReader reader=null;
                String JSONResults;

                try {
                    String urlString = null;
                    if (sortByPop) {
                        urlString = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+API_KEY;
                    } else {
                        urlString = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&vote_count.gte=500&api_key="+API_KEY;
                    }

                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();

                    if (inputStream == null) {
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {

                        buffer.append(line + "\n");

                    }

                    if (buffer.length() == 0) {
                        return null;
                    }

                    JSONResults = buffer.toString();

                    try {

                        overviews = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResults,"overview")));
                        titles = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResults,"original_title")));
                        ratings = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResults,"vote_average")));
                        dates = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResults,"release_date")));


                       return getPathsFromJson(JSONResults);
                    } catch (JSONException e) {
                        return null;
                    }
                }
                catch (Exception e)
                {
                    continue;


                }
                finally {
                    if(urlConnection!=null)
                    {
                        urlConnection.disconnect();
                    }
                    if(reader!=null)
                    {
                        try{
                            reader.close();

                        }
                        catch (final IOException e)
                        {

                        }
                    }
                }


            }

        }


        public String[] getPathsFromJson(String JSONStringParam) throws JSONException{

            JSONObject JSONString = new JSONObject(JSONStringParam);

            JSONArray moviesArray = JSONString.getJSONArray("results");
            String[] result = new String[moviesArray.length()];

            for(int i=0;i<moviesArray.length();i++  )
            {
                JSONObject movie=moviesArray.getJSONObject(i);
                String moviePath = movie.getString("poster_path");
                result[i]=moviePath;

            }
            return result;
        }


        public String[] getStringsFromJSON(String JSONStringParam,String param) throws JSONException{

            JSONObject JSONString = new JSONObject(JSONStringParam);

            JSONArray moviesArray = JSONString.getJSONArray("results");
            String[] result = new String[moviesArray.length()];

            for(int i = 0; i<moviesArray.length();i++)
            {
                JSONObject movie = moviesArray.getJSONObject(i);
                if(param.equals("vote_average"))
                {
                    Double number = movie.getDouble("vote_average");
                    String rating =Double.toString(number)+"/10";
                    result[i]=rating;
                }
                else {
                    String data = movie.getString(param);
                    result[i] = data;
                }
            }
            return result;
        }



    }

    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{


        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            gridView.setAdapter(null);
            onStart();

        }
    }


}
