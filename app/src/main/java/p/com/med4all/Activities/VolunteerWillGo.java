package p.com.med4all.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import APIS.ServerRequest;
import Libs.StaticVars;
import Model.RequestModel;
import Model.User;
import p.com.med4all.R;

public class VolunteerWillGo extends AppCompatActivity {
    private static final String TAG = UserArchiveRequest.class.getSimpleName();
    private ListView request_ListView;
    private ArrayList<RequestModel> archiveArrayList = new ArrayList<RequestModel>();
    private ListviewAdapter listviewAdapter;
    private int requestArchivetStatus = 2  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_will_go);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        request_ListView = findViewById(R.id.volunteerWilGo_requestListView);

        listviewAdapter = new ListviewAdapter();
        request_ListView.setAdapter(listviewAdapter);
        listviewAdapter.notifyDataSetChanged();
        if (archiveArrayList.size() == 0){
            getRequestFromServer();
        }


    }

    private void getRequestFromServer(){

        try {
            JSONObject data = new JSONObject();

            Map<String, String> params = new HashMap<String, String>();
            if (User.get_typeID().equals("1")) {
                data.put(getResources().getString(R.string.request_key_donator_id), User.get_ID());
                params.put("ask", "GET_REQUEST_BY_USER");
            } else if (User.get_typeID().equals("2")){
                params.put("ask", "GET_ARCHIVE_VOLUNTEER_REQUEST");
                data.put(getResources().getString(R.string.request_key_volunteer_id), User.get_ID());
            }
            data.put(getResources().getString(R.string.request_key_request_status), requestArchivetStatus );

            params.put("include", "requestModel");
            params.put("OS", "ANDROID");
            params.put("data", data.toString());


            new ServerRequest.Connection_(params) {
                @Override
                public void didStart() {
                    super.didStart();
                }

                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    Log.d(TAG, dataRequest.toString());
                    try{
                        JSONArray jsonArray =  new JSONArray(dataRequest);
                        for (int i = 0 ;i < jsonArray.length() ; i++){
                            JSONObject request =  new JSONObject(jsonArray.getJSONObject(i).toString());
                            RequestModel requestModel = new RequestModel();

                            requestModel.setReq_id(request.getString(getResources().getString(R.string.request_key_id)));
                            requestModel.setReq_date(request.getString(getResources().getString(R.string.request_key_date)));
                            requestModel.setReq_status(request.getString(getResources().getString(R.string.request_key_request_status)));

                            requestModel.setMedicine_name(request.getString(getResources().getString(R.string.request_key_med_name)));
                            requestModel.setMedicine_itemCount(request.getString(getResources().getString(R.string.request_key_item_count)));

                            requestModel.setDonator_id(request.getString(getResources().getString(R.string.request_key_donator_id)));
                            requestModel.setDonator_country_id(request.getString(getResources().getString(R.string.request_key_country_id)));
                            requestModel.setDonator_country_name(request.getString(getResources().getString(R.string.request_key_country_name)));
                            requestModel.setDonator_city_id(request.getString(getResources().getString(R.string.request_key_city_id)));
                            requestModel.setDonator_city_name(request.getString(getResources().getString(R.string.request_key_city_name)));
                            requestModel.setDonator_area_id(request.getString(getResources().getString(R.string.request_key_area_id)));
                            requestModel.setDonator_area_name(request.getString(getResources().getString(R.string.request_key_area_name)));
                            requestModel.setDonator_address(request.getString(getResources().getString(R.string.request_key_address_detail)));
                            requestModel.setDonator_availableTime(request.getString(getResources().getString(R.string.request_key_donator_availableTime)));
                            requestModel.setDonator_notes(request.getString(getResources().getString(R.string.request_key_request_note)));
                            //DONATOR
                            if (User.get_typeID().equals("1")) {
                                requestModel.setVoulnteer_id(request.getString(getResources().getString(R.string.request_key_volunteer_id)));
                                requestModel.setVoulnteer_first_name(request.getString(getResources().getString(R.string.request_key_volunteer_f_name)));
                                requestModel.setVoulnteer_last_name(request.getString(getResources().getString(R.string.request_key_volunteer_l_name)));
                                requestModel.setVoulnteer_phone(request.getString(getResources().getString(R.string.request_key_volunteer_phone)));
                                requestModel.setVoulnteer_token(request.getString(getResources().getString(R.string.request_key_volunteer_token)));
                            }
                            //VOULNTEER
                            else if (User.get_typeID().equals("2")) {
                                requestModel.setDonator_id(request.getString(getResources().getString(R.string.request_key_donator_id)));
                                requestModel.setDonator_first_name(request.getString(getResources().getString(R.string.request_key_donator_f_name)));
                                requestModel.setDonator_last_name(request.getString(getResources().getString(R.string.request_key_donator_l_name)));
                                requestModel.setDonator_phone(request.getString(getResources().getString(R.string.request_key_donator_phone)));
                                requestModel.setDonator_token(request.getString(getResources().getString(R.string.request_key_donator_token)));
                            }
                            archiveArrayList.add(requestModel);
                        }
                        listviewAdapter.notifyDataSetChanged();


                    }catch (Exception ex){
                        ex.printStackTrace(System.err);
                    }

                }
            };

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    private class ListviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        @Override
        public int getCount() {
            return archiveArrayList.size();
        }

        @Override
        public Object getItem(int i) {return archiveArrayList.get(i);}

        @Override
        public long getItemId(int i) {return i;}

        @Override
        public View getView(final int i, View cell, ViewGroup viewGroup) {
            if (inflater == null)
                inflater = (LayoutInflater)getLayoutInflater();
            if (cell == null)
                cell = inflater.inflate(R.layout.custom_list_view_cell, null);
//            TextView txt_medTitle = cell.findViewById(R.id.cell_medTitle);
            TextView txt_userTitle = cell.findViewById(R.id.cell_userTitle);
            TextView txt_medName = cell.findViewById(R.id.cell_medName);
            TextView txt_userName = cell.findViewById(R.id.cell_userName);
            ImageView statusImage = cell.findViewById(R.id.cell_imageStatus);

            final RequestModel requestModel = archiveArrayList.get(i);

            txt_medName.setText(requestModel.getMedicine_name());

            String userNameOtherSide  = "" ;

            if(User.get_typeID().equals("1") ){
                txt_userTitle.setText("اسم المتطوع");
                userNameOtherSide  = requestModel.getVoulnteer_first_name() + " " + requestModel.getVoulnteer_last_name();
                if (requestModel.getReq_status().equals("1")){
                    txt_userName.setText("في انتظار متطوع" );
                }else{
                    txt_userName.setText(userNameOtherSide);
                }
            }else if(User.get_typeID().equals("2") ){
                final Button home_btn_volunteerWillIsDone ;
                home_btn_volunteerWillIsDone = cell.findViewById(R.id.home_btn_volunteerWillGo);
                home_btn_volunteerWillIsDone.setVisibility(View.VISIBLE);
                home_btn_volunteerWillIsDone.setText("تم الأستلام");
                home_btn_volunteerWillIsDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(VolunteerWillGo.this);

                        builder1.setMessage("هل تم استلام الطلب بالفعل ؟");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "نعم",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        home_btn_volunteerWillIsDone.setVisibility(View.GONE);
                                        volunteerDoneRequest(requestModel.getReq_id() , i);
                                        dialog.cancel();
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
                });


                txt_userTitle.setText("اسم المتبرع");
                userNameOtherSide  = requestModel.getDonator_first_name() + " " + requestModel.getDonator_last_name();
                if (requestModel.getReq_status().equals("1")){
                    txt_userName.setText("في انتظار متطوع" );
                }else{
                    txt_userName.setText(userNameOtherSide);
                }
            }

            if (requestModel.getReq_status().equals("1")){
                statusImage.setImageResource(R.drawable.state1);
            }else if (requestModel.getReq_status().equals("2")){
                statusImage.setImageResource(R.drawable.state2);
            }else if (requestModel.getReq_status().equals("3")){
                statusImage.setImageResource(R.drawable.state3);
            }

            cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StaticVars.getContext() , RequestDetails.class);
                    intent.putExtra("RequestModel", (Serializable) requestModel);
                    intent.putExtra("indexOfRequest", i);
                    startActivity(intent);

                }
            });
            return cell;
        }

        private  void volunteerDoneRequest(String  requestID , final int requestIndex ){
            if (!User.get_ID().equals("2")) {return;}

            try {
                JSONObject data = new JSONObject();
                Map<String, String> params = new HashMap<String, String>();

                data.put( getResources().getString(R.string.request_key_id), requestID);
                data.put(getResources().getString(R.string.request_key_volunteer_id), User.get_ID());
                data.put("newStatus", "3");

                params.put("ask", "CHANGE_REQUEST_STATUS");
                params.put("include", "requestModel");
                params.put("OS", "ANDROID");
                params.put("data", data.toString());
                new ServerRequest.Connection_(params) {
                    @Override
                    public void didStart() {
                        super.didStart();
                    }

                    @Override
                    public void didFinsh(String dataRequest) {
                        super.didFinsh(dataRequest);
                        Log.d(TAG, dataRequest);
                        if (Boolean.parseBoolean(dataRequest)){
                            Toast.makeText(StaticVars.getContext() ,
                                    "تم إرسال طلبك بنجاح" ,
                                    Toast.LENGTH_LONG).show();
                            archiveArrayList.remove(requestIndex);
                            listviewAdapter.notifyDataSetChanged();

                        }
                    }
                };
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
