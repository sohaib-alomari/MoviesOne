package tmdb.omari.com.tmdb.Utils;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import tmdb.omari.com.tmdb.R;

public class Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference("sortby"));

        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();

       Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar,root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String s=newValue.toString();
        if(preference instanceof ListPreference)
        {
            ListPreference listPreference=(ListPreference) preference;
            int prefIndex= listPreference.findIndexOfValue(s);

            if(prefIndex>=0)
            {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }

            else
            {
                preference.setSummary(s);

            }

        }



        return true;
    }

    private  void bindPreferenceSummaryToValue(Preference preference)
    {

        preference.setOnPreferenceChangeListener(this);

        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }



}
