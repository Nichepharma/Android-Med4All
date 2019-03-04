package p.com.med4all.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import APIS.ServerRequest;
import Libs.HandelString;
import Libs.InternetConnection;
import Libs.StaticVars;
import Libs._SharedPref;
import p.com.med4all.R;


public class LoginScreen extends AppCompatActivity {
    private static final String TAG = LoginScreen.class.getSimpleName();
    private TextView txt_email, txt_password;
    private RelativeLayout progressBar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        txt_email = findViewById(R.id.login_txt_email);
        txt_password = findViewById(R.id.login_txt_password);
        progressBar = findViewById(R.id.login_progressBarView);
    }

    public void login_forgetPassword(View view) {
        Intent intent = new Intent(StaticVars.getContext(), ForgetPassword.class);
        startActivity(intent);
    }
    public void login_goToRegistration(View view) {
        Intent intent = new Intent(StaticVars.getContext(), RegistrationAccount.class);
        startActivity(intent);
    }
    public void checkLogin(View view) {
        if (InternetConnection.check()) {
            if (txt_email.getText().toString().replace(" ","").length() == 0) {
                Toast.makeText(StaticVars.getContext(), "ادخل البريد الاليكتروني", Toast.LENGTH_LONG).show();
                return;
            }

            if (HandelString.isValidEmaillId(txt_email.getText().toString().replace(" ","").toString()) == false ) {
                Toast.makeText(StaticVars.getContext(), "ادخل البريد الاليكتروني بشكل صحيح", Toast.LENGTH_LONG).show();
                return;
            }

            if (txt_password.getText().toString().replace(" ","").length() == 0) {
                Toast.makeText(StaticVars.getContext(), "ادخل الرقم السري", Toast.LENGTH_LONG).show();
                return;
            }
            checkServerLogin();
        } else {
            Toast.makeText(StaticVars.getContext(), "ﻻ يوجد اتصال بالانترنت", Toast.LENGTH_LONG).show();
        }

    }




    private void checkServerLogin() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("include", "userModel");
        params.put("ask", "AUTH");
        params.put("OS", "ANDROID");
        JSONObject data = new JSONObject();
        try {
            String  token = FirebaseInstanceId.getInstance().getToken();

            data.put("u_email",HandelString.replaceSpicalCharacter(txt_email.getText().toString()));
            data.put("u_password", HandelString.replaceSpicalCharacter(txt_password.getText().toString()));
            data.put("u_token", token);
            params.put("data", data.toString());
            progressBar.setVisibility(View.VISIBLE);
            new ServerRequest.Connection_(params) {
                @Override
                public void didStart() {
                    super.didStart();

                }

                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    progressBar.setVisibility(View.GONE);

                    Log.d(TAG, dataRequest.toString());

                    if (dataRequest.equals("0")) {
                        Toast.makeText(getApplicationContext(), "خطاء في تسجيل الدخول",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(dataRequest.toString());

                        _SharedPref.setByArray(getApplicationContext(), jsonObject);

                        Toast.makeText(getApplicationContext(), "مرحبا " + _SharedPref.get(getApplicationContext(), "u_f_name"),
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(StaticVars.getContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "خطاء",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            };

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "حدث خطاء", Toast.LENGTH_LONG).show();
        }

    }


}
