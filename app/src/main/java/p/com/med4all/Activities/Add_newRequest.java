package p.com.med4all.Activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import APIS.ServerRequest;
import Fragment.Home;
import Libs.HandelString;
import Libs.StaticVars;
import Model.City_Area_Model;
import Model.NotificationSubscripion;
import Model.User;
import p.com.med4all.R;

import static Libs.StaticVars.getContext;
import static com.android.volley.VolleyLog.TAG;

public class Add_newRequest extends AppCompatActivity {
    private Button addRequest_btn_date , addRequest_btn_time , addRequest_btn_submit;
    private EditText addRequest_txt_medName ,
            addRequest_txt_itemCount ,
            addRequest_txt_address ,
            addRequest_txt_note ;
    Spinner addRequest_spinner_city , addRequest_spinner_area ;
    private RelativeLayout addRequest_progressBarView ;
    ArrayList<City_Area_Model> citiesList = new ArrayList<City_Area_Model>();
    ArrayList<City_Area_Model> areaList = new ArrayList<City_Area_Model>();
    String cityIDSelected = "" ;
    String areaIDSelected = "" ;


    final Calendar dateOfCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog.OnTimeSetListener timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_request);

        addRequest_txt_medName =findViewById(R.id.addRequest_txt_medName);
        addRequest_txt_itemCount =findViewById(R.id.addRequest_txt_itemCount);
        addRequest_txt_address =findViewById(R.id.addRequest_txt_address);
        addRequest_txt_note =findViewById(R.id.addRequest_txt_note);
        addRequest_btn_date =findViewById(R.id.addRequest_btn_date);
        addRequest_btn_time =findViewById(R.id.addRequest_btn_time);
        addRequest_spinner_city =findViewById(R.id.addRequest_spinner_city);
        addRequest_spinner_area =findViewById(R.id.addRequest_spinner_area);
        addRequest_btn_submit =findViewById(R.id.addRequest_btn_submit);
        addRequest_progressBarView = findViewById(R.id.addRequest_progressBarView);

        addRequest_txt_address.setText(User.get_address());


        addRequest_btn_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initMyDatePicker();
                new DatePickerDialog(Add_newRequest.this, date, dateOfCalendar
                        .get(Calendar.YEAR), Calendar.MONTH - 1,
                        dateOfCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });

        addRequest_btn_time.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new TimePickerDialog(Add_newRequest.this, timePicker, dateOfCalendar
                        .get(Calendar.HOUR), dateOfCalendar.get(Calendar.MINUTE), false).show();
            }
        });

        timePicker = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                dateOfCalendar.set(Calendar.HOUR, hourOfDay);
                dateOfCalendar.set(Calendar.MINUTE, minute);

                updateTimeLabel();
            }
        };
        getCityFromServerBy() ;

        addRequest_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dismiss Keyboard
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                submit_addingNewRequest();
            }
        });
    }


    public void submit_addingNewRequest(){
        boolean canInsert = true ;


        if (addRequest_txt_medName.getText().toString().replace(" ","").length() == 0){
            addRequest_txt_medName.setText("");
            addRequest_txt_medName.setError("إدخل اسم الدواء");
//            Toast.makeText(getContext() , "إدخل اسم الدواء" , Toast.LENGTH_LONG).show();
            canInsert = false ;
        }
        if (addRequest_txt_itemCount.getText().length() == 0){
            addRequest_txt_itemCount.setError("حدد الكمية") ;
            addRequest_txt_itemCount.setText("");
//            Toast.makeText(getContext() , "حدد الكمية" , Toast.LENGTH_LONG).show();
            canInsert = false ;

        }
        if (addRequest_txt_itemCount.getText().length() > 0 && Integer.parseInt(String.valueOf(addRequest_txt_itemCount.getText().toString())) < 1){
            addRequest_txt_itemCount.setError("1");
//            Toast.makeText(getContext() , "تم تعديل الكمية تلقائي الي قطعه واحده" , Toast.LENGTH_LONG).show();
            canInsert = false ;
        }
        if (addRequest_btn_date.getText().toString().replace(" ","").length() == 0){
            addRequest_btn_date.setError("حدد تاريخ الزيارة") ;
//            Toast.makeText(getContext() , "حدد تاريخ الزيارة" , Toast.LENGTH_LONG).show();
            canInsert = false ;
        }
        if (addRequest_btn_time.getText().toString().replace(" ","").length() == 0){
            addRequest_btn_time.setError("حدد ميعاد الزيارة") ;
//            Toast.makeText(getContext() , "حدد ميعاد الزيارة" , Toast.LENGTH_LONG).show();
            canInsert = false ;
        }
        if (cityIDSelected.toString().replace(" ","").length() == 0){
//            Toast.makeText(getContext() , "اختر المحاظفة" , Toast.LENGTH_LONG).show();
            canInsert = false ;
        }
        if (areaIDSelected.toString().replace(" ","").length() == 0){
//            Toast.makeText(getContext() , "اختر الحي او المنطقة" ,Toast.LENGTH_LONG).show();
            canInsert = false ;
        }
        if (HandelString.handleEscapeCharacter(addRequest_txt_address.getText().toString().replace(" ","")).length() == 0){
            addRequest_txt_address.setText("");
            addRequest_btn_time.setError("اكتب العنوان التفصيلي للمنطقه") ;
//            Toast.makeText(getContext() , "اكتب العنوان التفصيلي للمنطقه" , Toast.LENGTH_LONG).show();
            canInsert = false ;
        }
        if (HandelString.handleEscapeCharacter(addRequest_txt_note.getText().toString().replace(" ","")).length() == 0){
            addRequest_txt_note.setText("");
        }
        if(canInsert == false){return;}
        addRequest_btn_submit.setVisibility(View.GONE);
        String medName   =  addRequest_txt_medName.getText().toString() ;
        String itemCount =  addRequest_txt_itemCount.getText().toString() ;

        String requestDate_time =  (addRequest_btn_date.getText().toString()).replace("\\","").replace("/","-")  +  " " + addRequest_btn_time.getText().toString();
        String address =  addRequest_txt_address.getText().toString() ;
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String note =  addRequest_txt_note.getText().toString() ;

        JSONObject data = new JSONObject();
        try {
            data.put(getContext().getResources().getString(R.string.request_key_donator_id), User.get_ID());
            data.put(getContext().getResources().getString(R.string.request_key_date), currentDateTimeString);
            data.put(getContext().getResources().getString(R.string.request_key_donator_availableTime), requestDate_time);
            data.put(getContext().getResources().getString(R.string.request_key_city_id), cityIDSelected);
            data.put(getContext().getResources().getString(R.string.request_key_area_id), areaIDSelected);
            data.put(getContext().getResources().getString(R.string.request_key_address_detail), HandelString.replaceSpicalCharacter(address));
            data.put(getContext().getResources().getString(R.string.request_key_country_id), "1");
            data.put(getContext().getResources().getString(R.string.request_key_med_name), HandelString.replaceSpicalCharacter(medName));
            data.put(getContext().getResources().getString(R.string.request_key_item_count), itemCount);
            data.put(getContext().getResources().getString(R.string.request_key_request_note), HandelString.replaceSpicalCharacter(note));



            Map<String, String> params = new HashMap<String, String>();
            params.put("include", "requestModel");
            params.put("ask", "CREATE");
            params.put("data",data.toString());
            params.put("OS", "ANDROID");
            addRequest_progressBarView.setVisibility(View.VISIBLE);
            new ServerRequest.Connection_(params){
                @Override
                public void didStart() {
                    super.didStart();
                }
                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    addRequest_progressBarView.setVisibility(View.GONE);
                    try {
                        if (Integer.parseInt(dataRequest.replace("\"","")) > 0 ){
                            int reqID = Integer.parseInt(dataRequest.replace("\"","") );
                            NotificationSubscripion.pushNotificationToGroups(User.get_areaID() , ""+reqID);

                            Toast.makeText(getContext() , "تم ارسال طلبك بنجاح", Toast.LENGTH_LONG).show();
                            Home.getRequestFromServer = true;
                            Intent intent = new Intent(StaticVars.getContext(), MainActivity.class);
                            startActivity(intent);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext() , "حدث خطاء اثناء ارسال طلبك", Toast.LENGTH_LONG).show();
                    }
                }
            };

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    private void initMyDatePicker(){
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                dateOfCalendar.set(Calendar.YEAR, year);
                dateOfCalendar.set(Calendar.MONTH , monthOfYear);
                dateOfCalendar.set(Calendar.DAY_OF_MONTH , dayOfMonth);
                updateDateLabel();
            }

        };
    }
    private void updateDateLabel() {
        Calendar today = Calendar.getInstance();

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        String selectedDate   =  sdf.format(dateOfCalendar.getTime()) ;
        String currentDate  =  sdf.format(today.getTime()) ;

        if (isDateAfter(selectedDate , currentDate)){
            addRequest_btn_date.setText("");
            addRequest_btn_date.setError("يجب ان يكون تاريخ الزيارة اكبر من التاريخ الحالي");
           return;
        }


        addRequest_btn_date.setText(selectedDate);
    }

    public static boolean isDateAfter(String startDate,String endDate)
    {
        try
        {
            String myFormatString = "yyyy-M-dd"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate))
                return true;
            else
                return false;
        }
        catch (Exception e)
        {

            return false;
        }
    }


    private void updateTimeLabel() {
        Calendar today = Calendar.getInstance();
       /* if (dateOfCalendar.before(today)){
            addRequest_btn_time.setError("يجب ان يكون تاريخ الزيارة اكبر من التاريخ الحالي");
            addRequest_btn_time.setText("");
            return;
        }
        */
        String myFormat = "HH:mm:00"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        addRequest_btn_time.setText(sdf.format(dateOfCalendar.getTime()));
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
        addRequest_spinner_city.setAdapter(adapter);

        int postion  = items.indexOf(User.get_city_name());
        addRequest_spinner_city.setSelection(postion);

        addRequest_spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        addRequest_spinner_area.setAdapter(adapter);

        if (setAreaFromSheardPrefrences){
            setAreaFromSheardPrefrences = false ;
            int postion  = items.indexOf(User.get_area_name());
            addRequest_spinner_area.setSelection(postion);
        }


        addRequest_spinner_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
