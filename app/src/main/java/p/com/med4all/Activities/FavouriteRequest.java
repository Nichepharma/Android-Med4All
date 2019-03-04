package p.com.med4all.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import APIS.ServerRequest;
import Libs.DBHelper;
import Libs.StaticVars;
import Model.RequestModel;
import Model.User;
import p.com.med4all.R;

public class FavouriteRequest extends AppCompatActivity {

    private static final String TAG = FavouriteRequest.class.getSimpleName();
    private ListView request_ListView;
    static ArrayList<RequestModel> requestArrayList = new ArrayList<RequestModel>();
    private ListviewAdapter listviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_request);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        requestArrayList = DBHelper.select_favourit(StaticVars.getContext()) ;

        request_ListView = findViewById(R.id.fav_requestListView);
        listviewAdapter = new ListviewAdapter();
        request_ListView.setAdapter(listviewAdapter);
        listviewAdapter.notifyDataSetChanged();


    }







    private class ListviewAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        @Override
        public int getCount() {
            return requestArrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return requestArrayList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int i, View cell, ViewGroup viewGroup) {
            if (inflater == null)
                inflater = (LayoutInflater) getLayoutInflater();
            if (cell == null)
                cell = inflater.inflate(R.layout.custom_list_view_cell, null);
//            TextView txt_medTitle = cell.findViewById(R.id.cell_medTitle);
            TextView txt_userTitle = cell.findViewById(R.id.cell_userTitle);
            TextView txt_medName = cell.findViewById(R.id.cell_medName);
            TextView txt_userName = cell.findViewById(R.id.cell_userName);
            ImageView statusImage = cell.findViewById(R.id.cell_imageStatus);

            final RequestModel requestModel = requestArrayList.get(i);
            txt_medName.setText(requestModel.getMedicine_name());

            if (User.get_typeID().equals("2")) {
                txt_userTitle.setText("اسم المتبرع");
                txt_userName.setText(requestModel.getDonator_first_name() + " " + requestModel.getDonator_last_name());
                final Button home_btn_favourite , home_btn_volunteerWillGo;
                home_btn_favourite = cell.findViewById(R.id.home_btn_favourite);
                home_btn_volunteerWillGo = cell.findViewById(R.id.home_btn_volunteerWillGo);
                home_btn_volunteerWillGo.setVisibility(View.VISIBLE);
                home_btn_favourite.setBackground(getResources().getDrawable(R.drawable.ic_star_black_36dp));
                home_btn_favourite.setVisibility(View.VISIBLE);
                home_btn_favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        favouritHandel(requestModel , i);
                    }
                });
                home_btn_volunteerWillGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(StaticVars.getContext());
                        builder1.setMessage("هل تريد الذهاب للمتبرع");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "نعم",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        home_btn_volunteerWillGo.setVisibility(View.GONE);
                                        volunteerAcceptRequest(requestModel.getReq_id(), i);
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

            } else if (User.get_typeID().equals("1")) {
//                txt_medTitle.setText("");
                txt_userTitle.setText("اسم المتطوع");
                if (requestModel.getReq_status().equals("1")) {
                    txt_userName.setText("في انتظار متطوع");
                } else {
                    txt_userName.setText(requestModel.getVoulnteer_first_name() + " " + requestModel.getVoulnteer_last_name());

                }
            }


            if (requestModel.getReq_status().equals("1")) {
                statusImage.setImageResource(R.drawable.state1);
            } else if (requestModel.getReq_status().equals("2")) {
                statusImage.setImageResource(R.drawable.state2);
            } else if (requestModel.getReq_status().equals("3")) {
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

        private void favouritHandel(RequestModel requestModel , int postion ) {
            DBHelper.delete_favourit(getApplicationContext(),requestModel.getReq_id());
            requestArrayList.clear();
            requestArrayList = DBHelper.select_favourit(StaticVars.getContext()) ;
//            requestArrayList.remove(postion);
            listviewAdapter.notifyDataSetChanged();


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
                            Toast.makeText(StaticVars.getContext() ,
                                    "تم إرسال طلبك بنجاح" ,
                                    Toast.LENGTH_LONG).show();
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




    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }



}
