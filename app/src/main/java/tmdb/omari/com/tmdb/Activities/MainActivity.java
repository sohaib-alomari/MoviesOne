package tmdb.omari.com.tmdb.Activities;


//Sohaib Alomari MovieContract Project Part 2

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import tmdb.omari.com.tmdb.Fragments.DetailActivityFragment;
import tmdb.omari.com.tmdb.Fragments.MainFragment;
import tmdb.omari.com.tmdb.R;
import tmdb.omari.com.tmdb.Utils.Settings;

public class MainActivity extends AppCompatActivity implements MainFragment.Callback  {

    public static boolean TABLET = false;
    private final static String TAG = "Omari: <" + MainActivity.class.getSimpleName() + ">";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    public boolean mTwoPane=true;

    public boolean isTablet(Context context)
    {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)==Configuration.SCREENLAYOUT_SIZE_LARGE);
        return(xlarge||large);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        isTablet(this);

        if (findViewById(R.id.containerDetailll) != null) {

            mTwoPane = true;

            Log.v("TwoPane", "---***IN TWO-PANE MODE ***---");

            if (savedInstanceState == null) {


               getSupportFragmentManager().beginTransaction()
                       .replace(R.id.containerDetailll, new DetailActivityFragment(),
                               TAG)
                        .commit();



            }
        } else {
            mTwoPane = false;
            Log.v(TAG, "---*** NOT TWO-PANE MODE ***---");
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String movie,String key) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            //arguments.putString(DetailActivityFragment.DETAIL_MOVIE, movie);
            if(key.equals("title"))
            {
                arguments.putString("title", movie);
            }


            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerDetailll, fragment, DetailActivityFragment.TAG)
                    .commit();
            Log.v("CallBack", "---***CALL BACK OK ***---");
        } else {
            Bundle arguments = new Bundle();
            if(key.equals("title"))
            {
                arguments.putString("title", movie);
            }
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(DetailActivityFragment.DETAIL_MOVIE, movie);
            startActivity(intent);
        }
    }
}
