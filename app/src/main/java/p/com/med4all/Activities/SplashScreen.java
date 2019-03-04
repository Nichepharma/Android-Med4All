package p.com.med4all.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import Libs.StaticVars;
import Model.User;
import p.com.med4all.R;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = SplashScreen.class.getSimpleName();
    private final long splashtimer = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        StaticVars.setContext(getApplicationContext());


        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //start your activity here
                changeActivity();
            }

        }, splashtimer);


    }




    private void changeActivity(){
        try{
            if (User.get_ID() == null || User.get_ID().equals("0") || User.get_ID().equals("")){
                Intent intent = new Intent(StaticVars.getContext() , LoginScreen.class);
                startActivity(intent);
                finish();
                return;
            }
        }catch (Exception ex){
        ex.printStackTrace(System.err);
        }

        Intent intent = new Intent(StaticVars.getContext() , MainActivity.class);
        startActivity(intent);
        finish();
    }
}
