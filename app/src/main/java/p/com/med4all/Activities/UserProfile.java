package p.com.med4all.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import APIS.ServerRequest;
import Libs.HandelString;
import Libs.StaticVars;
import Model.City_Area_Model;
import Model.User;
import p.com.med4all.R;

public class UserProfile extends AppCompatActivity {
    private static final String TAG = UserProfile.class.getSimpleName();

    private EditText userProfile_txt_firstName , userProfile_txt_lastName ,
            userProfile_txt_phone , userProfile_txt_email, userProfile_txt_address ;
    private Spinner userProfile_spinner_city , userProfile_spinner_area ;
    private Button userProfiel_btn_submitEdit ;
    ArrayList<City_Area_Model> citiesList = new ArrayList<City_Area_Model>();
    ArrayList<City_Area_Model> areaList = new ArrayList<City_Area_Model>();
    String cityIDSelected = "" ;
    String areaIDSelected = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userProfile_txt_firstName = findViewById(R.id.userProfile_txt_firstName);
        userProfile_txt_lastName  = findViewById(R.id.userProfile_txt_lastName);
        userProfile_txt_phone  = findViewById(R.id.userProfile_txt_phone);
        userProfile_txt_email = findViewById(R.id.userProfile_txt_email);
        userProfile_txt_address = findViewById(R.id.userProfile_txt_address);
        userProfile_spinner_city = findViewById(R.id.userProfile_spinner_city);
        userProfile_spinner_area = findViewById(R.id.userProfile_spinner_area);
        userProfiel_btn_submitEdit =  findViewById(R.id.userProfile_btn_submit) ;

        userProfile_txt_firstName.setText(User.get_first_name());
        userProfile_txt_lastName.setText(User.get_last_name());
        userProfile_txt_phone.setText(User.get_phone());
        userProfile_txt_email.setText(User.get_email());
        userProfile_txt_address.setText(User.get_address());

        userProfiel_btn_submitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dismiss Keyboard
                InputMethodManager imm = (InputMethodManager) StaticVars.getContext()
                        .getSystemService(StaticVars.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                doUpdate();
            }
        });
        getCityFromServerBy();




    }

    @Override
    protected void onStart() {
        super.onStart();
        if (userProfile_txt_phone.getText().length() < 11)
                 userProfile_txt_phone.setEnabled(true);
    }

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StaticVars.getContext() ,
                R.layout.spiner_text , items);
        userProfile_spinner_city.setAdapter(adapter);

        int postion  = items.indexOf(User.get_city_name());
        userProfile_spinner_city.setSelection(postion);

        userProfile_spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

  private  Boolean setAreaFromSheardPrefrences = true ;
    private void setAreaAdtaper(){

        ArrayList<String> items = new ArrayList<String>();
        for (City_Area_Model city : areaList){
            items.add(city.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StaticVars.getContext() ,
                R.layout.spiner_text , items);
        userProfile_spinner_area.setAdapter(adapter);

        if (setAreaFromSheardPrefrences){
            setAreaFromSheardPrefrences = false ;
            int postion  = items.indexOf(User.get_area_name());
            userProfile_spinner_area.setSelection(postion);
        }


        userProfile_spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                areaIDSelected = areaList.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void doUpdate(){

        if (userProfile_txt_firstName.getText().toString().replace(" ","").equals("")){
            userProfile_txt_firstName.setText("");
            Toast.makeText(StaticVars.getContext() , "أدخل الاسم الاول"   ,  Toast.LENGTH_LONG).show();
            return;
        }
        if (userProfile_txt_lastName.getText().toString().replace(" ","").equals("")){
            userProfile_txt_lastName.setText("");
            Toast.makeText(StaticVars.getContext() , "أدخل الاسم الثاني"  ,  Toast.LENGTH_LONG).show();
            return;
        }
        if (userProfile_txt_phone.getText().toString().replace(" ","").equals("")){
            userProfile_txt_phone.setText("");

            Toast.makeText(StaticVars.getContext() ,  "ادخل رقم التليفون"  ,  Toast.LENGTH_LONG).show();

            return;
        }
        if (userProfile_txt_phone.getText().toString().replace(" ","").length() < 11 ){
//            userProfile_txt_phone.setText("");
            Toast.makeText(StaticVars.getContext() ,  "يجب ان يكون مكون من 11 رقم"  ,  Toast.LENGTH_LONG).show();
            return;
        }
        if (cityIDSelected.toString().replace(" ","").equals("")){
            Toast.makeText(StaticVars.getContext() ,  "اختر المحافظة"  ,  Toast.LENGTH_LONG).show();
            return;
        }
        if (areaIDSelected.toString().replace(" ","").equals("")){
            Toast.makeText(StaticVars.getContext() ,  "اختر الحي"   ,  Toast.LENGTH_LONG).show();
            return;
        }
        if (userProfile_txt_address.getText().toString().replace(" ","").equals("")){
            userProfile_txt_address.setText("");
            Toast.makeText(StaticVars.getContext() ,  "اكتب عنوانك التفصيلي"  ,  Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject data = new JSONObject();
        try {
            data.put(getResources().getString(R.string.user_key_id) , User.get_ID());
            data.put(getResources().getString(R.string.user_key_f_name), HandelString.replaceSpicalCharacter(userProfile_txt_firstName.getText().toString()) );
            data.put(getResources().getString(R.string.user_key_l_name), HandelString.replaceSpicalCharacter(userProfile_txt_lastName.getText().toString()) );
            data.put(getResources().getString(R.string.user_key_phone), HandelString.replaceSpicalCharacter(userProfile_txt_phone.getText().toString()) );
            data.put(getResources().getString(R.string.user_key_email), HandelString.replaceSpicalCharacter(userProfile_txt_email.getText().toString()) );
            data.put(getResources().getString(R.string.user_key_city_id), HandelString.replaceSpicalCharacter(cityIDSelected));
            data.put(getResources().getString(R.string.user_key_area_id),HandelString.replaceSpicalCharacter( areaIDSelected) );
            data.put(getResources().getString(R.string.user_key_address), HandelString.replaceSpicalCharacter(userProfile_txt_address.getText().toString() ));
            Map<String, String> params = new HashMap<String, String>();
            params.put("include", "userModel");
            params.put("ask", "UPDATE_USER_PROFILE");
            params.put("OS", "ANDROID");
            params.put("data", data.toString());

            new ServerRequest.Connection_(params){
                @Override
                public void didStart() {
                    super.didStart();

                }
                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    try{
                        if (Boolean.valueOf(dataRequest) == true ){
                            Log.d(TAG , dataRequest);
                            Toast.makeText(StaticVars.getContext() ,
                                    "تمت عمليه التسجيل بنجاح" ,Toast.LENGTH_LONG).show();
                            User.set_first_name(userProfile_txt_firstName.getText().toString());
                            User.set_last_name(userProfile_txt_lastName.getText().toString());
                            User.set_phone(userProfile_txt_phone.getText().toString());

                            User.set_CityID(cityIDSelected);
                            User.set_city_name(userProfile_spinner_city.getSelectedItem().toString());
                            User.set_areaID(areaIDSelected);
                            User.set_area_name(userProfile_spinner_area.getSelectedItem().toString());
                            User.set_address(userProfile_txt_address.getText().toString());
                            Intent intent = new Intent(StaticVars.getContext(), MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(StaticVars.getContext() ,
                                    "حدث خطاء اثناء التعديل .." ,Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ex){
                        ex.printStackTrace(System.err);
                        Toast.makeText(StaticVars.getContext() ,
                                "حدث خطاء" , Toast.LENGTH_LONG).show();
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
        }}