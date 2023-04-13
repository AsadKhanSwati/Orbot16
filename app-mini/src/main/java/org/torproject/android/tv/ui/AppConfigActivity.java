package org.torproject.android.tv.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import org.torproject.android.tv.R;
import org.torproject.android.service.OrbotConstants;
import org.torproject.android.service.util.Prefs;
import org.torproject.android.service.vpn.TorifiedApp;

import static org.torproject.android.service.OrbotConstants.PREFS_KEY_TORIFIED;
import static org.torproject.android.tv.MiniMainActivity.getApp;

public class AppConfigActivity extends AppCompatActivity {

    TorifiedApp mApp;

    private boolean mAppTor = false;
    private boolean mAppData = false;
    private boolean mAppWifi = false;

    private SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_config);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String pkgId = getIntent().getStringExtra(Intent.EXTRA_PACKAGE_NAME);

        mPrefs =  Prefs.getSharedPrefs(getApplicationContext());

        ApplicationInfo aInfo = null;
        try {
            aInfo = getPackageManager().getApplicationInfo(pkgId, 0);
            mApp = getApp(this, aInfo);

            getSupportActionBar().setIcon(mApp.getIcon());

            setTitle(mApp.getName());
        }
        catch (Exception e){}

        mAppTor = mPrefs.getBoolean(pkgId + OrbotConstants.APP_TOR_KEY,true);
        mAppData = mPrefs.getBoolean(pkgId + OrbotConstants.APP_DATA_KEY,false);
        mAppWifi = mPrefs.getBoolean(pkgId + OrbotConstants.APP_WIFI_KEY,false);

        Switch switchAppTor = findViewById(R.id.switch_app_tor);
        switchAppTor.setChecked(mAppTor);
        switchAppTor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPrefs.edit().putBoolean(pkgId + OrbotConstants.APP_TOR_KEY,isChecked).commit();

                Intent response = new Intent();
                setResult(RESULT_OK,response);
            }
        });

        Switch switchAppData = findViewById(R.id.switch_app_data);
        switchAppData.setChecked(mAppData);
        switchAppData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPrefs.edit().putBoolean(pkgId + OrbotConstants.APP_DATA_KEY,isChecked).commit();

                Intent response = new Intent();
                setResult(RESULT_OK,response);
            }
        });
        switchAppData.setEnabled(false);

        Switch switchAppWifi = findViewById(R.id.switch_app_wifi);
        switchAppWifi.setChecked(mAppWifi);
        switchAppWifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPrefs.edit().putBoolean(pkgId + OrbotConstants.APP_WIFI_KEY,isChecked).commit();

                Intent response = new Intent();
                setResult(RESULT_OK,response);
            }
        });
        switchAppWifi.setEnabled(false);



    }

    private void addApp ()
    {
        mApp.setTorified(true);

        String tordAppString = mPrefs.getString(PREFS_KEY_TORIFIED, "");

        tordAppString = tordAppString += mApp.getPackageName()+"|";

        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(PREFS_KEY_TORIFIED, tordAppString);
        edit.commit();

        Intent response = new Intent();
        setResult(RESULT_OK,response);

    }

    private void removeApp ()
    {
        mApp.setTorified(false);


        String tordAppString = mPrefs.getString(PREFS_KEY_TORIFIED, "");

        tordAppString = tordAppString.replace(mApp.getPackageName()+"|","");

        SharedPreferences.Editor edit = mPrefs.edit();
        edit.putString(PREFS_KEY_TORIFIED, tordAppString);
        edit.commit();

        Intent response = new Intent();
        setResult(RESULT_OK,response);

    }

    /*
     * Create the UI Options Menu (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_config, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        else if (item.getItemId() == R.id.menu_remove_app) {
            removeApp();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




}
