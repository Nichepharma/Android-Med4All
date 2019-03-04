package p.com.med4all.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class UserArchiveRequest extends AppCompatActivity {
    private static final String TAG = UserArchiveRequest.class.getSimpleName();
    private ListView request_ListView;
     ArrayList<RequestModel> archiveArrayList = new ArrayList<RequestModel>();
    private ListviewAdapter listviewAdapter;
    private int requestArchivetStatus = 3  ;
    private RelativeLayout progressBarView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_archive_request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        request_ListView = findViewById(R.id.archive_requestListView);
        progressBarView =  findViewById(R.id.archiverogressBarView);
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
            data.put(getResources().getString(R.string.request_key_request_status), requestArchivetStatus );

            Map<String, String> params = new HashMap<String, String>();
            params.put("include", "requestModel");
            if (User.get_typeID().equals("1")) {
                data.put(getResources().getString(R.string.request_key_donator_id), User.get_ID());
                params.put("ask", "GET_REQUEST_BY_USER");
            } else if (User.get_typeID().equals("2")){
                params.put("ask", "GET_ARCHIVE_VOLUNTEER_REQUEST");
                data.put(getResources().getString(R.string.request_key_volunteer_id), User.get_ID());
            }
            params.put("OS", "ANDROID");
            params.put("data", data.toString());


            new ServerRequest.Connection_(params) {
                @Override
                public void didStart() {
                    super.didStart();
                    progressBarView.setVisibility(View.VISIBLE);
                }

                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    Log.d(TAG, dataRequest.toString());
                    try{
                        progressBarView.setVisibility(View.GONE);

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
    }




    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



}
