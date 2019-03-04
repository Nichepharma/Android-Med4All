package p.com.med4all.Activities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Fragment.Home;
import Libs.DBHelper;
import Libs.StaticVars;
import Model.NotificationSubscripion;
import Model.RequestModel;
import Model.User;
import p.com.med4all.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        android.support.v4.app.Fragment homeFragmentHome =  new Home();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragmentHome)
                .commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View navHeader = navigationView.getHeaderView(0);
        final TextView userName = navHeader.findViewById(R.id.menu_userName);
        final TextView menuArea = navHeader.findViewById(R.id.menu_userArea);
        final TextView menuCity = navHeader.findViewById(R.id.menu_userCity);


        userName.setText(User.get_first_name() + " " + User.get_last_name() );
        menuArea.setText(User.get_area_name());
        menuCity.setText(User.get_city_name());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        //setup volunteer menu item
        if (User.get_typeID().equals("2")){
            getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.menu_volunteerWillGo).setVisible(true);
            navigationView.getMenu().findItem(R.id.menu_favourite).setVisible(true);

        }

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
        //    Toast.makeText(getApplicationContext() , "Hoooba " , Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        if (id == R.id.menu_userProfile) {
         Intent intent = new Intent(StaticVars.getContext() , UserProfile.class);
         startActivity(intent);

        } else if (id == R.id.menu_volunteerWillGo) {
            Intent intent = new Intent(StaticVars.getContext() , VolunteerWillGo.class);
            startActivity(intent);
        } else if (id == R.id.menu_favourite) {
            Intent intent = new Intent(StaticVars.getContext() , FavouriteRequest.class);
            startActivity(intent);
        } else if (id == R.id.menu_userArchive) {
            Intent intent = new Intent(StaticVars.getContext() , UserArchiveRequest.class);
            startActivity(intent);
        }  else if (id == R.id.menu_appAbout) {
            Intent intent = new Intent(StaticVars.getContext() , AboutApplication.class);
            startActivity(intent);
        }else  if (id == R.id.menu_logout){
            doLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /****************************************************************/
    // handel back button
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_arrow_back_white_24dp)
                        .setTitle("تنبية")
                        .setMessage("هل تريد الخروج من التطبيق ؟")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("ﻻ", null)
                        .show();

        return true ; // super.onKeyUp(keyCode, event);
    }

    private void doLogout(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("هل تريد تسجيل الخروج");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "نعم",
                new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        NotificationSubscripion.subscripTopics(User.get_areaID());
                        User.set_ID("");
                        User.set_first_name("");
                        User.set_last_name("");
                        User.set_phone("");
                        User.set_email("");
                        User.set_city_name("");
                        User.set_CityID("");
                        User.set_area_name("");
                        User.set_areaID("");
                        User.set_address("");
                        User.set_typeID("");
                        User.set_typeValue("");
                        // unsubscribe topic

                        Home.requestArrayList = new ArrayList<RequestModel>();
                        FavouriteRequest.requestArrayList = new ArrayList<RequestModel>();
                        DBHelper mDbHelper  = new DBHelper(StaticVars.getContext());
                        SQLiteDatabase db = mDbHelper.getWritableDatabase();
                        db.execSQL("delete from "+ mDbHelper.TABLE_NAME);
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();
                        Intent intent = new Intent(getApplication(),SplashScreen.class);
                        startActivity(intent);
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "ﻻ",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
