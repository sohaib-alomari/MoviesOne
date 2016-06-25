package tmdb.omari.com.tmdb;


//Sohaib Alomari Movie Project Part 1

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    public static boolean TABLET = false;
    private final static String TAG = "Omari: <" + MainActivity.class.getSimpleName() + ">";

    public boolean mTwoPane;

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



        if (findViewById(R.id.containerDetailll) != null) {

            mTwoPane = true;


         
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
}
