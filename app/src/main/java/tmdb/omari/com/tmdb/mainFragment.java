package tmdb.omari.com.tmdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

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
    // you have to sign up and get your own API key 3
    static String API_KEY="1f1e4e852b00dc660803e835ee5fc600";
    static ArrayList<String> posters;
    static boolean sortbyp=false;
    static boolean sortByFavorites;

    static PreferenceChangeListener listener;
    static SharedPreferences prefs;
    static ArrayList<String> overviews;
    static ArrayList<String> titles;
    static ArrayList<String> dates;
    static ArrayList<String> ratings;
    static ArrayList<String> youtubes;
    static ArrayList<String> youtubes2;
    static ArrayList<String> ids;
    static ArrayList<Boolean> favorited;
    static ArrayList<ArrayList<String>> comments;


    static ArrayList<String> postersF;
    static ArrayList<String> titlesF;
    static ArrayList<String> datesF;
    static ArrayList<String> ratingsF;
    static ArrayList<String> youtubesF;
    static ArrayList<String> youtubes2F;
    static ArrayList<ArrayList<String>> commentsF;
    static ArrayList<String> overviewsF;

    Movie movieItem = new Movie();
    public static String dbb;

public static int positionn;




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
                positionn=position;
                if (!sortByFavorites) {


                        favorited = bindFavoritesToMovies();



                    movieItem.setTitle(titles.get(position));
                    movieItem.setPoster_path(posters.get(position));
                    movieItem.setOverview(overviews.get(position));
                    movieItem.setRelease_date(dates.get(position));

                    movieItem.setRating(ratings.get(position));

                    movieItem.setYoutube(youtubes.get(position));

                    movieItem.setYoutube2(youtubes2.get(position));

                    movieItem.setTitle(titles.get(position));

                    movieItem.setComments(comments.get(position));


                    movieItem.setFav(favorited.get(position));
                    ((Callback) getActivity()).onItemSelected(titles.get(position),"title");

                }



                else{


                    movieItem.setPoster_path(postersF.get(position));
                    movieItem.setOverview(overviewsF.get(position));
                    movieItem.setRelease_date(datesF.get(position));

                    movieItem.setRating(ratingsF.get(position));

                    movieItem.setYoutube(youtubesF.get(position));

                    movieItem.setYoutube2(youtubes2F.get(position));

                    movieItem.setTitle(titlesF.get(position));

                    movieItem.setComments(commentsF.get(position));


                    movieItem.setFav(favorited.get(position));
                    ((Callback) getActivity()).onItemSelected(titles.get(position),"title");



                }

            }
        });


        return rootView;
    }




    @Override
    public void onStart() {

        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        listener = new PreferenceChangeListener();
        prefs.registerOnSharedPreferenceChangeListener(listener);

        if (prefs.getString("sortby", "").equals("popularity")) {
            getActivity().setTitle("Most Popular Movies");
            sortbyp = true;
            sortByFavorites = false;

        } else if (prefs.getString("sortby", "").equals("rating")) {
            getActivity().setTitle("Highest Rated Movies");
            sortbyp = false;
            sortByFavorites = false;

        } else if (prefs.getString("sortby", "").equals("favorites")) {
            getActivity().setTitle("Favorited Movies");
            sortbyp = false;
            sortByFavorites = true;
        }

        try {
            loadFavoritesData();
        }
        catch (Exception e)
        {

        }




        if (sortByFavorites) {
            if (postersF.size() == 0) {
               // txt.setText("You have no favorites movies.");

                //gridView.setVisibility(GridView.GONE);
            } else {
                gridView.setVisibility(GridView.VISIBLE);

            }
            if (postersF != null&&getActivity()!=null) {
                ImageAdapter adapter = new ImageAdapter(getActivity(), postersF, width);
                gridView.setAdapter(adapter);
                Toast.makeText(getContext(),dbb, Toast.LENGTH_SHORT).show();
        }
    } else {
        gridView.setVisibility(GridView.VISIBLE);



        if (isNetworkAvailable()) {
                //we made it work in the background thread because it can freeze the main thread till all the images are loaded so it needs to be done in the background
                gridView.setVisibility(GridView.VISIBLE);
                new ImageLoadTask().execute();
            }

            //just in case there is no internet Connection
            else {
                TextView tv = new TextView(getActivity());

                tv.setText("There is No Internet Connection!");


                gridView.setVisibility(GridView.GONE);
            }
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
                        ids = new ArrayList<String>(Arrays.asList(getStringsFromJSON(JSONResults,"id")));





                        // The Youtube Links sometimes come back to be inconsistent as in NULL so we put it in a loop that keeps trying untill we get a solid Result
                        while(true)
                        {
                            youtubes = new ArrayList<String>(Arrays.asList(getYoutubesFromIds(ids,0)));
                            youtubes2 = new ArrayList<String>(Arrays.asList(getYoutubesFromIds(ids,1)));
                            int nullCount = 0;
                            for(int i = 0; i<youtubes.size();i++)
                            {
                                if(youtubes.get(i)==null)
                                {
                                    nullCount++;
                                    youtubes.set(i,"no video found");
                                }
                            }
                            for(int i = 0; i<youtubes2.size();i++)
                            {
                                if(youtubes2.get(i)==null)
                                {
                                    nullCount++;
                                    youtubes2.set(i,"no video found");
                                }
                            }
                            if(nullCount>2)continue;
                            break;
                        }


                        comments = getReviewsFromIds(ids);
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

        public ArrayList<ArrayList<String>> getReviewsFromIds(ArrayList<String> ids)
        {

            while(true)
            {
                ArrayList<ArrayList<String>> results = new ArrayList<>();
                for(int i =0; i<ids.size(); i++)
                {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String JSONResult;

                    try {
                        String urlString = null;
                        urlString = "http://api.themoviedb.org/3/movie/" + ids.get(i) + "/reviews?api_key=" + API_KEY;
                        URL url = new URL(urlString);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                        //Read the input stream into a String
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
                        JSONResult = buffer.toString();
                        try {
                            results.add(getCommentsFromJSON(JSONResult));
                        } catch (JSONException E) {
                            return null;
                        }
                    } catch (Exception e) {
                        continue;

                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (final IOException e) {
                            }
                        }
                    }
                }
                return results;

            }
        }
        public ArrayList<String> getCommentsFromJSON(String JSONStringParam)throws JSONException{
            JSONObject JSONString = new JSONObject(JSONStringParam);
            JSONArray reviewsArray = JSONString.getJSONArray("results");
            ArrayList<String> results = new ArrayList<>();
            if(reviewsArray.length()==0)
            {
                results.add("No reviews found for this movie.");
                return results;
            }
            for(int i = 0; i<reviewsArray.length(); i++)
            {
                JSONObject result = reviewsArray.getJSONObject(i);
                results.add(result.getString("content"));
            }
            return results;

        }
        public String getYoutubeFromJSON(String JSONStringParam, int position) throws JSONException
        {
            JSONObject JSONString = new JSONObject(JSONStringParam);
            JSONArray youtubesArray = JSONString.getJSONArray("results");
            JSONObject youtube;
            String result = "no videos found";
            if(position ==0)
            {
                youtube = youtubesArray.getJSONObject(0);
                result = youtube.getString("key");
            }
            else if(position==1)
            {
                if(youtubesArray.length()>1)
                {
                    youtube = youtubesArray.getJSONObject(1);
                }
                else{
                    youtube = youtubesArray.getJSONObject(0);
                }
                result = youtube.getString("key");
            }
            return result;
        }




        public String[] getYoutubesFromIds(ArrayList<String> ids, int position)
        {
            String[] results = new String[ids.size()];
            for(int i =0; i<ids.size(); i++)
            {
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String JSONResult;

                try {
                    String urlString = null;
                    urlString = "http://api.themoviedb.org/3/movie/" + ids.get(i) + "/videos?api_key=" + API_KEY;


                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    //Read the input stream into a String
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
                    JSONResult = buffer.toString();
                    try {
                        results[i] = getYoutubeFromJSON(JSONResult, position);
                    } catch (JSONException E) {
                        results[i] = "no video found";
                    }
                } catch (Exception e) {

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                        }
                    }
                }
            }
            return results;

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


               // movieItem.setOriginal_title("Sohaib");

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



    public void loadFavoritesData()
    {
        Context applicationContext = DetailActivity.getContextOfApplication();


        String URL="content://tmdb.omari.com.tmdb.Movies/movies" ;
        Uri movies = Uri.parse(URL);
        Cursor c = getActivity().getContentResolver().query(movies,null,null,null,"title");
        postersF = new ArrayList<String>();
        titlesF = new ArrayList<String>();
        datesF = new ArrayList<String>();
        overviewsF = new ArrayList<String>();
        favorited = new ArrayList<Boolean>();
        commentsF = new ArrayList<ArrayList<String>>();
        youtubesF = new ArrayList<String>();
        youtubes2F = new ArrayList<String>();
        ratingsF = new ArrayList<String>();
        if(c==null) return;
        while(c.moveToNext())
        {

            dbb=c.getString(c.getColumnIndex(MovieProvider.NAME));

            postersF.add(c.getString(c.getColumnIndex(MovieProvider.NAME)));
            commentsF.add(convertStringToArrayList(c.getString(c.getColumnIndex(MovieProvider.REVIEW))));
            titlesF.add(c.getString(c.getColumnIndex(MovieProvider.TITLE)));
            overviewsF.add(c.getString(c.getColumnIndex(MovieProvider.OVERVIEW)));
            youtubesF.add(c.getString(c.getColumnIndex(MovieProvider.YOUTUBE1)));
            youtubes2F.add(c.getString(c.getColumnIndex(MovieProvider.YOUTUBE2)));
            datesF.add(c.getString(c.getColumnIndex(MovieProvider.DATE)));
            ratingsF.add(c.getString(c.getColumnIndex(MovieProvider.RATING)));
            favorited.add(true);


        }
    }

    public ArrayList<String> convertStringToArrayList(String s)
    {
        ArrayList<String> result = new ArrayList<>(Arrays.asList(s.split("divider")));
        return result;
    }


    public ArrayList<Boolean> bindFavoritesToMovies() {
        ArrayList<Boolean> result = new ArrayList<>();
        for (int i = 0; i < titles.size(); i++) {
            result.add(false);
        }
        if (titlesF != null){
            for (String favoritedTitles : titlesF) {
                for (int x = 0; x < titles.size(); x++) {
                    if (favoritedTitles.equals(titles.get(x))) {
                        result.set(x, true);
                    }
                }
            }
    }
        return result;
    }


    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         * @param movieData
         */
        public void onItemSelected(String movieData,String key);
    }





}
