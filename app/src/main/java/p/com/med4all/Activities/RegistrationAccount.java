package p.com.med4all.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import APIS.ServerRequest;
import Libs.HandelString;
import Libs.StaticVars;
import Model.City_Area_Model;
import p.com.med4all.R;

public class RegistrationAccount extends AppCompatActivity {
    private static final String TAG = RegistrationAccount.class.getSimpleName();
    EditText reg_txt_firstName , reg_txt_lastName , reg_txt_phone , reg_txt_email , reg_txt_password , reg_txt_rePassword ,reg_txt_address ;
    Spinner reg_spinner_city  , reg_spinner_area ;
    CheckBox reg_checkBox ;
    private RelativeLayout reg_progressBarView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reg_txt_firstName = findViewById(R.id.reg_txt_firstName);
        reg_txt_lastName  = findViewById(R.id.reg_txt_lastName);
        reg_txt_phone  = findViewById(R.id.reg_txt_phone);
        reg_txt_email  = findViewById(R.id.reg_txt_email);
        reg_txt_password  = findViewById(R.id.reg_txt_password);
        reg_txt_rePassword  = findViewById(R.id.reg_txt_rePassword);
        reg_txt_address  = findViewById(R.id.reg_txt_address);
        reg_spinner_city = findViewById(R.id.reg_spinner_city);
        reg_spinner_area = findViewById(R.id.reg_spinner_area);
        reg_checkBox = findViewById(R.id.reg_checkBox);
        reg_progressBarView  = findViewById(R.id.reg_progressBarView);

        getCityFromServerBy();

    }



    public void submitNewAccount(View view){
        if (reg_txt_firstName.getText().toString().replace(" ","").length() == 0){
            Toast.makeText(StaticVars.getContext() , "أدخل " + "الاسم الاول" , Toast.LENGTH_LONG).show();
            return;
        }
        if (reg_txt_lastName.getText().toString().replace(" ","").length() == 0){
            Toast.makeText(StaticVars.getContext() , "أدخل " + "اﻷسم الثاني" , Toast.LENGTH_LONG).show();
            return;
        }
        if (reg_txt_phone.getText().toString().replace(" ","").length() == 0){
            Toast.makeText(StaticVars.getContext() , "أدخل " + "رقم التليفون" , Toast.LENGTH_LONG).show();
            return;
        }
        if (reg_txt_email.getText().toString().replace(" ","").length() == 0){
            Toast.makeText(StaticVars.getContext() , "أدخل " + "البريد الإليكتروني" , Toast.LENGTH_LONG).show();
            return;
        }
        if (HandelString.isValidEmaillId(reg_txt_email.getText().toString()) == false){
            Toast.makeText(StaticVars.getContext() , "أدخل " + "البريد الإليكتروني بشكل صحيح" , Toast.LENGTH_LONG).show();
            return;
        }
         if (reg_txt_password.getText().toString().replace(" ","").length() == 0) {
             Toast.makeText(StaticVars.getContext() , "أدخل " + "الرقم السري" , Toast.LENGTH_LONG).show();
             return;
        }
        if (reg_txt_rePassword.getText().toString().replace(" ","").length() == 0) {
            Toast.makeText(StaticVars.getContext() , "اعد كتابه الرقم السري" + "" , Toast.LENGTH_LONG).show();
            return;
        }
        if (!reg_txt_rePassword.getText().toString().replace(" ","").toString().equals(reg_txt_password.getText().toString()) ) {
            Toast.makeText(StaticVars.getContext() , "كلمات السر غير متطابقة " + "" , Toast.LENGTH_LONG).show();
            return;
        }
        if (reg_txt_address.getText().toString().replace(" ","").length() == 0) {
            Toast.makeText(StaticVars.getContext() , "أدخل " + "العنوان التفصيلي" , Toast.LENGTH_LONG).show();
            return;
        }
        if (cityIDSelected.length() == 0) {
            Toast.makeText(StaticVars.getContext() , "أختر المحافظة " + "" , Toast.LENGTH_LONG).show();
            return;
        }
        if (areaIDSelected.length() == 0) {
            Toast.makeText(StaticVars.getContext() , "اخر الحي " + "" , Toast.LENGTH_LONG).show();
            return;
        }
        final int userType  = reg_checkBox.isEnabled() == true ? 1 :2 ;


        Map<String, String> params = new HashMap<String, String>();
        params.put("include", "userModel");
        params.put("ask", "EMAIL_PHONE_CHECKED");
        params.put("OS", "ANDROID");
        final JSONObject data = new JSONObject();
        try {
            final String token = FirebaseInstanceId.getInstance().getToken();
            data.put(getResources().getString(R.string.user_key_email), HandelString.replaceSpicalCharacter(reg_txt_email.getText().toString()));
            data.put(getResources().getString(R.string.user_key_phone),  HandelString.replaceSpicalCharacter(reg_txt_phone.getText().toString()));
            params.put("data", data.toString());
            reg_progressBarView.setVisibility(View.VISIBLE);
            new ServerRequest.Connection_(params){
                @Override
                public void didStart() {
                    super.didStart();
                }

                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    reg_progressBarView.setVisibility(View.GONE);

                    int errorCode =  Integer.parseInt(dataRequest);
                    switch (errorCode){
                        case 0 :
                            try {
                                data.put(getResources().getString(R.string.user_key_f_name), HandelString.replaceSpicalCharacter(reg_txt_firstName.getText().toString()));
                                data.put(getResources().getString(R.string.user_key_l_name), HandelString.replaceSpicalCharacter(reg_txt_lastName.getText().toString()));
                                data.put(getResources().getString(R.string.user_key_password), HandelString.replaceSpicalCharacter(reg_txt_password.getText().toString()));
                                data.put(getResources().getString(R.string.user_key_city_id), HandelString.replaceSpicalCharacter(cityIDSelected));
                                data.put(getResources().getString(R.string.user_key_area_id), HandelString.replaceSpicalCharacter(areaIDSelected));
                                data.put(getResources().getString(R.string.user_key_country_id), 1);
                                data.put(getResources().getString(R.string.user_key_address), HandelString.replaceSpicalCharacter(reg_txt_address.getText().toString()));
                                data.put(getResources().getString(R.string.user_key_type_id), HandelString.replaceSpicalCharacter(""+userType));
                                data.put(getResources().getString(R.string.user_key_token), token);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Intent intent = new Intent(RegistrationAccount.this , PhoneNumerAuth.class);
                            intent.putExtra("userData", data.toString());
                            Toast.makeText(StaticVars.getContext() , "تم ارسال رمز التفعيل الي هاتفك" , Toast.LENGTH_LONG).show();
                            startActivity(intent);
                            break;
                        case 1 :
                            Toast.makeText(StaticVars.getContext() , "البريد الاليكتروني مستخدم من قبل" , Toast.LENGTH_LONG).show();
                            reg_txt_email.setError("البريد الاليكتروني مستخدم من قبل");

                            break;
                        case 2:
                            Toast.makeText(StaticVars.getContext() , "رقم التليفون مستخدم من قبل" , Toast.LENGTH_LONG).show();
                            reg_txt_phone.setError("رقم التليفون مستخدم من قبل");

                            break;
                        case 3 :
                            Toast.makeText(StaticVars.getContext() , "البريد الاليكتروني و رقم التليفون مسجل من قبل " , Toast.LENGTH_LONG).show();
                            reg_txt_email.setError("رقم التليفون مستخدم من قبل");
                            reg_txt_phone.setError("البريد الاليكتروني مستخدم من قبل");

                            break;
                    }
                    Log.d(TAG , dataRequest);
                }

            };
        }catch (Exception ex){
            ex.printStackTrace(System.err);
        }
    }




    ArrayList<City_Area_Model> citiesList = new ArrayList<City_Area_Model>();
    ArrayList<City_Area_Model> areaList = new ArrayList<City_Area_Model>();
    String cityIDSelected = "" ;
    String areaIDSelected = "" ;

    private void getCityFromServerBy(){

        JSONObject data = new JSONObject();
        try {
            data.put("country_id", "1");

            Map<String, String> params = new HashMap<String, String>();
            params.put("include", "areaModel");
            params.put("ask", "GETCITY");
            params.put("data",data.toString());
            params.put("OS", "ANDROID");
            new ServerRequest.Connection_(params){
                @Override
                public void didStart() {
                    super.didStart();
                }
                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    try {
                        Log.d(TAG , dataRequest );
                        JSONArray citiesArray = new JSONArray(dataRequest);
                        for ( int i = 0 ; i < citiesArray.length() ; i ++){
                            JSONObject eachCity = new JSONObject(citiesArray.getJSONObject(i).toString());
                            City_Area_Model city_model = new City_Area_Model();
                            city_model.setId(eachCity.getString("id"));
                            city_model.setName(eachCity.getString("name"));
                            citiesList.add(city_model);
                        }
                        setCityAdtaper();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                }
            };

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void setCityAdtaper(){
        ArrayList<String> items = new ArrayList<String>();
        for (City_Area_Model city : citiesList){
            items.add(city.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item , items);
        reg_spinner_city.setAdapter(adapter);
        reg_spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaList.clear();
                areaIDSelected = "";
                cityIDSelected = citiesList.get(position).getId();
                getAreaFromServerByCityID(cityIDSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getAreaFromServerByCityID(String cityID){

        JSONObject data = new JSONObject();
        try {
            data.put("city_id", cityID);

            Map<String, String> params = new HashMap<String, String>();
            params.put("include", "areaModel");
            params.put("ask", "GETAREA");
            params.put("data",data.toString());
            params.put("OS", "ANDROID");

            new ServerRequest.Connection_(params){
                @Override
                public void didStart() {
                    super.didStart();
                }
                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    try {
                        Log.d(TAG , dataRequest );
                        JSONArray citiesArray = new JSONArray(dataRequest);
                        for ( int i = 0 ; i < citiesArray.length() ; i ++){
                            JSONObject eachArea = new JSONObject(citiesArray.getJSONObject(i).toString());
                            City_Area_Model city_model = new City_Area_Model();
                            city_model.setId(eachArea.getString("id"));
                            city_model.setName(eachArea.getString("name"));
                            areaList.add(city_model);
                        }
                        setAreaAdtaper();

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                }
            };

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAreaAdtaper(){

        ArrayList<String> items = new ArrayList<String>();
        for (City_Area_Model city : areaList){
            items.add(city.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item , items);
        reg_spinner_area.setAdapter(adapter);
        reg_spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaIDSelected = areaList.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }





    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
