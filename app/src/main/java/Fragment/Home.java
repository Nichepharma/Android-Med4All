package Fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import Libs.DBHelper;
import Libs.StaticVars;
import Model.NotificationSubscripion;
import Model.RequestModel;
import Model.User;
import p.com.med4all.Activities.Add_newRequest;
import p.com.med4all.Activities.MainActivity;
import p.com.med4all.R;
import p.com.med4all.Activities.RequestDetails;

public class Home extends Fragment {
    private static final String TAG = Home.class.getSimpleName();
    private View view;

    private ListView request_ListView;
    private RelativeLayout progressBarView ;

    public static ArrayList<RequestModel> requestArrayList = new ArrayList<RequestModel>();
    private ListviewAdapter listviewAdapter;
    private Button home_btn_goToAddNewRequest ;
    public static  boolean getRequestFromServer = true ;
    public Home() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(false);
        progressBarView = view.findViewById(R.id.home_rogressBarView);
        request_ListView = view.findViewById(R.id.home_requestListView);
        home_btn_goToAddNewRequest = view.findViewById(R.id.home_btn_goToAddNewRequest);
        home_btn_goToAddNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddRequest();
            }
        });
        listviewAdapter = new ListviewAdapter();
        request_ListView.setAdapter(listviewAdapter);
        listviewAdapter.notifyDataSetChanged();

        if (requestArrayList.size() == 0 || getRequestFromServer){
            requestArrayList.clear();
            listviewAdapter.notifyDataSetChanged();
            getRequestFromServer();
            getRequestFromServer = false ;
        }
        if (User.get_typeID().equals("2")){
            home_btn_goToAddNewRequest.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }




    private void getRequestFromServer(){

        try {

            JSONObject data = new JSONObject();
            data.put(getResources().getString(R.string.request_key_donator_id), User.get_ID());
            data.put(getResources().getString(R.string.request_key_request_status), 1);

            Map<String, String> params = new HashMap<String, String>();
            //            IS A NORMAL USER (DONATOR)
            if (User.get_typeID().equals("1")){
                params.put("ask", "GET_REQUEST_BY_USER");
            }
            // IS A VOLUNTEER
            else if(User.get_typeID().equals("2")) {
                params.put("ask", "GET_VOLUNTEER_REQUEST");
                data.put(getResources().getString(R.string.request_key_area_id),User.get_areaID());
            }

            params.put("include", "requestModel");
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
                    if (dataRequest == null || dataRequest.equals("null")){
                        progressBarView.setVisibility(View.GONE);
                        return;
                    }
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

                            if (User.get_ID().equals("1")) {
                                requestModel.setVoulnteer_id(request.getString(getResources().getString(R.string.request_key_volunteer_id)));
                                requestModel.setVoulnteer_first_name(request.getString(getResources().getString(R.string.request_key_volunteer_f_name)));
                                requestModel.setVoulnteer_last_name(request.getString(getResources().getString(R.string.request_key_volunteer_l_name)));
                                requestModel.setVoulnteer_phone(request.getString(getResources().getString(R.string.request_key_volunteer_phone)));
                                requestModel.setVoulnteer_token(request.getString(getResources().getString(R.string.request_key_volunteer_token)));
                            }else if (User.get_ID().equals("2")) {
                                requestModel.setDonator_id(request.getString(getResources().getString(R.string.request_key_donator_id)));
                                requestModel.setDonator_first_name(request.getString(getResources().getString(R.string.request_key_donator_f_name)));
                                requestModel.setDonator_last_name(request.getString(getResources().getString(R.string.request_key_donator_l_name)));
                                requestModel.setDonator_phone(request.getString(getResources().getString(R.string.request_key_donator_phone)));
                                requestModel.setDonator_token(request.getString(getResources().getString(R.string.request_key_donator_token)));
                            }

                            requestArrayList.add(requestModel);
                        }
                        listviewAdapter.notifyDataSetChanged();


                    }catch (Exception ex){
                        ex.printStackTrace(System.err);
                        Toast.makeText(StaticVars.getContext() , "حدث خطاء" , Toast.LENGTH_LONG).show();
                    }
                    progressBarView.setVisibility(View.GONE);

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
            return requestArrayList.size();
        }

        @Override
        public Object getItem(int i) {return requestArrayList.get(i);}

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

            final RequestModel requestModel = requestArrayList.get(i);
            txt_medName.setText(requestModel.getMedicine_name());

            if (User.get_typeID().equals("2")){
                txt_userTitle.setText("اسم المتبرع");
                txt_userName.setText(requestModel.getDonator_first_name() + " " + requestModel.getDonator_last_name());
                final Button home_btn_favourite , home_btn_volunteerWillGo ;
                home_btn_favourite = cell.findViewById(R.id.home_btn_favourite);
                home_btn_volunteerWillGo = cell.findViewById(R.id.home_btn_volunteerWillGo);
                home_btn_volunteerWillGo.setVisibility(View.VISIBLE);
                home_btn_favourite.setVisibility(View.VISIBLE);
                final boolean[] isAvailabel = {DBHelper.checkIsAvailable(StaticVars.getContext(), requestModel.getReq_id())};

                home_btn_favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(isAvailabel[0]){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                home_btn_favourite.setBackground(getResources().getDrawable(R.drawable.ic_star_border_black_48dp));
                            }
                        }else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                home_btn_favourite.setBackground(getResources().getDrawable(R.drawable.ic_star_black_36dp));
                            }
                        }
                        favouritHandel(requestModel);
                        isAvailabel[0] = !isAvailabel[0];
                    }
                });
                if(isAvailabel[0]){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        home_btn_favourite.setBackground(getResources().getDrawable(R.drawable.ic_star_black_36dp));
                    }
                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        home_btn_favourite.setBackground(getResources().getDrawable(R.drawable.ic_star_border_black_48dp));
                    }
                }
                home_btn_volunteerWillGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setMessage("هل تريد الذهاب للمتبرع");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "نعم",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        home_btn_volunteerWillGo.setVisibility(View.GONE);
                                        volunteerAcceptRequest(requestModel.getReq_id() , i);
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

            }else if(User.get_typeID().equals("1") ){
//                txt_medTitle.setText("");
                txt_userTitle.setText("اسم المتطوع");
                if (requestModel.getReq_status().equals("1")){
                    txt_userName.setText("في انتظار متطوع" );
                }else{
                    txt_userName.setText(requestModel.getVoulnteer_first_name() + " " + requestModel.getVoulnteer_last_name() );

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

        private  void favouritHandel(RequestModel requestModel){
            DBHelper mDbHelper  = new DBHelper(StaticVars.getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("request_id" , requestModel.getReq_id());
            values.put(getResources().getString(R.string.request_key_med_name) , requestModel.getMedicine_name());
            values.put(getResources().getString(R.string.request_key_date) , requestModel.getReq_date());
            values.put(getResources().getString(R.string.request_key_donator_id) , requestModel.getDonator_id());
            values.put(getResources().getString(R.string.request_key_donator_f_name) , requestModel.getDonator_first_name());
            values.put(getResources().getString(R.string.request_key_donator_l_name) , requestModel.getDonator_last_name());
            values.put(getResources().getString(R.string.request_key_donator_phone) , requestModel.getDonator_phone());
            values.put(getResources().getString(R.string.request_key_country_id) , requestModel.getDonator_country_id());
            values.put(getResources().getString(R.string.request_key_country_name) , requestModel.getDonator_country_name());
            values.put(getResources().getString(R.string.request_key_city_id) , requestModel.getDonator_city_id());
            values.put(getResources().getString(R.string.request_key_city_name) , requestModel.getDonator_city_name());
            values.put(getResources().getString(R.string.request_key_area_id) , requestModel.getDonator_area_id());
            values.put(getResources().getString(R.string.request_key_area_name) , requestModel.getDonator_area_name());
            values.put(getResources().getString(R.string.request_key_address_detail) , requestModel.getDonator_address());
            values.put(getResources().getString(R.string.request_key_donator_availableTime) , requestModel.getDonator_availableTime());
            values.put(getResources().getString(R.string.request_key_item_count) , requestModel.getMedicine_itemCount());
            values.put(getResources().getString(R.string.request_key_request_note) , requestModel.getDonator_notes());
            values.put(getResources().getString(R.string.request_key_request_status) , requestModel.getReq_status());
            try{
                if (DBHelper.insert_favourit(StaticVars.getContext() , values)){
                    Snackbar.make(view, "تم اﻷضافه الي المفضله", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else{
                    DBHelper.delete_favourit(StaticVars.getContext() ,  requestModel.getReq_id());
                    Snackbar.make(view, "حذفت من المفضله", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }

        }

        private  void volunteerAcceptRequest(String  requestID , final int requestIndex ){
            if (!User.get_ID().equals("2")) {return;}

            try {
                JSONObject data = new JSONObject();
                Map<String, String> params = new HashMap<String, String>();

                data.put( getResources().getString(R.string.request_key_id), requestID);
                data.put(getResources().getString(R.string.request_key_volunteer_id), User.get_ID());
                data.put("newStatus", "2");
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
                            String uToken = requestArrayList.get(requestIndex).getDonator_token();
                            NotificationSubscripion.pushNotificationToToken(uToken,"تم استلام طلبك من قبل المتطوع");
                            Snackbar.make(view, "تم إرسال طلبك بنجاح", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            requestArrayList.remove(requestIndex);
                            listviewAdapter.notifyDataSetChanged();

                        }
                    }
                };
            }catch (Exception ex){
                ex.printStackTrace(System.err);
            }
        }
    }
    public void goToAddRequest(){
      Intent intent = new Intent(StaticVars.getContext(), Add_newRequest.class);
      startActivity(intent);
    }


}
