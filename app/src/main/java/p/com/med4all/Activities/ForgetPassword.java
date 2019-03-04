package p.com.med4all.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import APIS.ServerRequest;
import Libs.HandelString;
import Libs.StaticVars;
import p.com.med4all.R;

public class ForgetPassword extends AppCompatActivity {
    private static final String TAG = ForgetPassword.class.getSimpleName();

   private TextView txt_email ;
   private Button btn_restoreMyMail ;
   private RelativeLayout forgert_progressBarView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txt_email = findViewById(R.id.forgert_txt_email);

        btn_restoreMyMail = findViewById(R.id.forgert_btn_restore);
        forgert_progressBarView = findViewById(R.id.forgert_progressBarView);

        btn_restoreMyMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_email.getText().length() == 0) {
                    txt_email.setError("ادخل البريد الاليكتروني");
                    return;
                }
                if (HandelString.isValidEmaillId(txt_email.getText().toString()) == false ) {
                    txt_email.setError("ادخل البريد الاليكتروني");
                    txt_email.setText("");
                    Toast.makeText(StaticVars.getContext(), "ادخل البريد الاليكتروني بشكل صحيح", Toast.LENGTH_LONG).show();
                    return;
                }

                resorePasswordFromServer();
            }
        });

    }



    private  void resorePasswordFromServer(){


        Map<String, String> params = new HashMap<String, String>();
        params.put("include", "userModel");
        params.put("ask", "FORGET_PASSWORD");
        params.put("OS", "ANDROID");
        JSONObject data = new JSONObject();
        try {
            data.put(getResources().getString(R.string.user_key_email), HandelString.replaceSpicalCharacter(txt_email.getText().toString()));
            params.put("data", data.toString());
            forgert_progressBarView.setVisibility(View.VISIBLE);
            new ServerRequest.Connection_(params) {
                @Override
                public void didStart() {
                    super.didStart();

                }

                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    Log.d(TAG, dataRequest.toString());
                    forgert_progressBarView.setVisibility(View.GONE);

                    try {
                        JSONObject res = new JSONObject(dataRequest);
                        if (res.get("succ").equals("1")){
                            Toast.makeText(StaticVars.getContext() ,
                                    "تم ارسال الرقم السري الي البريد الاليكتروني الخاص بك" ,
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(StaticVars.getContext() , LoginScreen.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

        }catch (Exception ex){

        }
    }




    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
