package tmdb.omari.com.tmdb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class DetailActivity extends AppCompatActivity {

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if(savedInstanceState==null)
        {


            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailActivityFragment.DETAIL_MOVIE, getIntent().getParcelableExtra(DetailActivityFragment.DETAIL_MOVIE));

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


/*
    public void trailer1(View v)
    {
            Intent viewTrailer=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+DetailActivityFragment.youtube));
            startActivity(viewTrailer);
    }
    public void trailer2(View v)
    {
        Intent viewTrailer=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+DetailActivityFragment.youtube2));
        startActivity(viewTrailer);

    }

    public void favorite(View v)
    {
        Button b = (Button)findViewById(R.id.favorite);
        if(b.getText().equals("FAVORITE"))
        {
            //code to store movie data in database
            b.setText("UNFAVORITEe");
            b.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.MULTIPLY);

            ContentValues values = new ContentValues();
            values.put(MovieProvider.NAME,DetailActivityFragment.poster);
            values.put(MovieProvider.OVERVIEW,DetailActivityFragment.overview);
            values.put(MovieProvider.RATING,DetailActivityFragment.rating);
            values.put(MovieProvider.DATE,DetailActivityFragment.date);
            values.put(MovieProvider.REVIEW,DetailActivityFragment.review);
            values.put(MovieProvider.YOUTUBE1,DetailActivityFragment.youtube);
            values.put(MovieProvider.YOUTUBE2,DetailActivityFragment.youtube2);
            values.put(MovieProvider.TITLE,DetailActivityFragment.title);

            getContentResolver().insert(MovieProvider.CONTENT_URI, values);


        }
        else
        {
            b.setText("FAVORITE");
            b.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);

           getContentResolver().delete(Uri.parse("content://tmdb.omari.com.tmdb.Movies/movies"),
                "title=?",new String[]{DetailActivityFragment.title});
        }
    }
    */

    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }


}