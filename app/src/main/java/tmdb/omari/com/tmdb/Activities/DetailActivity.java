package tmdb.omari.com.tmdb.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import tmdb.omari.com.tmdb.Fragments.DetailActivityFragment;
import tmdb.omari.com.tmdb.R;
import tmdb.omari.com.tmdb.Utils.Settings;


public class DetailActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {


            Bundle arguments = new Bundle();
            arguments.putParcelable(tmdb.omari.com.tmdb.Fragments.DetailActivityFragment.DETAIL_MOVIE, getIntent().getParcelableExtra(tmdb.omari.com.tmdb.Fragments.DetailActivityFragment.DETAIL_MOVIE));

            contextOfApplication = getApplicationContext();
            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);
            Log.v("DetailActivity", "---***IN Detail Activity***---");

            getSupportFragmentManager().beginTransaction().add(R.id.containerDetailll, fragment).commit();


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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


}