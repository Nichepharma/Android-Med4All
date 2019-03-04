package p.com.med4all.Activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import APIS.ServerRequest;
import Fragment.Home;
import Libs.StaticVars;
import Model.NotificationSubscripion;
import Model.RequestModel;
import Model.User;
import p.com.med4all.R;

public class RequestDetails extends AppCompatActivity {
    private static final String TAG = RequestDetails.class.getSimpleName();

    private TextView lbl_medNameValue ,
            lbl_itemCountvValue ,
            lbl_vistiTimeValue ,
            lbl_cityAreaValue  ,
            lbl_addressValue  ,
            lbl_noteValue ,
            userNameTitle,
            userNameValue ,
            phoneValue ;
    private ImageView image_status ;
    private Button requestDetails_btn_cancel;
    private RequestModel requestModel ;

    private int indexOfRequest = 0 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        requestModel = (RequestModel) bundle.getSerializable("RequestModel");
        indexOfRequest = bundle.getInt("indexOfRequest");
        initGUI_item() ;
        setDataInViews() ;
    }

    private void initGUI_item(){
        lbl_medNameValue = findViewById(R.id.requestDetails_lbl_medNameValue);
        lbl_itemCountvValue = findViewById(R.id.requestDetails_lbl_itemCountvValue);
        lbl_vistiTimeValue = findViewById(R.id.requestDetails_lbl_vistiTimeValue);
        lbl_cityAreaValue = findViewById(R.id.requestDetails_lbl_cityAreaValue);
        lbl_addressValue  = findViewById(R.id.requestDetails_lbl_addressValue);
        lbl_noteValue = findViewById(R.id.requestDetails_lbl_noteValue);
        userNameTitle = findViewById(R.id.requestDetails_lbl_userNameTitle);
        userNameValue  = findViewById(R.id.requestDetails_lbl_userNameValue);
        phoneValue = findViewById(R.id.requestDetails_lbl_phoneValue);
        image_status = findViewById(R.id.requestDetails_image_status);
        requestDetails_btn_cancel = findViewById(R.id.requestDetails_btn_cancel);

        handelActionButton();
    }
    private  void handelActionButton(){

        requestDetails_btn_cancel.setVisibility(View.GONE);

        if (User.get_typeID().equals("1")){
            requestDetails_btn_cancel.setVisibility(View.VISIBLE);
            if (requestModel.getReq_status().equals("1")
                                    ||
                requestModel.getReq_status().equals("2")){
                requestDetails_btn_cancel.setVisibility(View.VISIBLE);
                requestDetails_btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelRequest();
                    }
                });

            }
            return;
        }

        if (User.get_typeID().equals("2")){

            if (requestModel.getReq_status().equals("1")){
                requestDetails_btn_cancel.setVisibility(View.VISIBLE);
                requestDetails_btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeRequestStauts("2");
                    }
                });

                requestDetails_btn_cancel.setBackgroundColor(getResources().getColor(R.color.green));
                requestDetails_btn_cancel.setText("قبول");
            }else if (requestModel.getReq_status().equals("2")){
                requestDetails_btn_cancel.setVisibility(View.VISIBLE);
                requestDetails_btn_cancel.setBackgroundColor(getResources().getColor(R.color.red));
                requestDetails_btn_cancel.setText("إلغاء");
                requestDetails_btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeRequestStauts("1");
                    }
                });
            }
            return;
        }
    }
    private void setDataInViews(){
        if (requestModel != null){
            lbl_medNameValue.setText(requestModel.getMedicine_name());
            lbl_itemCountvValue .setText(requestModel.getMedicine_itemCount());
            lbl_vistiTimeValue .setText(requestModel.getDonator_availableTime());
            lbl_cityAreaValue  .setText(requestModel.getDonator_city_name() + " - " + requestModel.getDonator_area_name());
            lbl_addressValue  .setText(requestModel.getDonator_address());
            lbl_noteValue .setText(requestModel.getDonator_notes());
//
            if (User.get_typeID().equals("2")){
                userNameTitle.setText("أسم المتبرع");
                userNameValue.setText(requestModel.getDonator_first_name()+ " " + requestModel.getDonator_last_name());
                phoneValue.setText(requestModel.getDonator_phone());


            }else{
                if (!requestModel.getVoulnteer_first_name().equals("null")){
                    userNameValue .setText(requestModel.getVoulnteer_first_name() + " " + requestModel.getVoulnteer_last_name());
                    phoneValue.setText(requestModel.getVoulnteer_phone());
                }
            }

            if (requestModel.getReq_status().equals("1") ){
                image_status.setImageResource(R.drawable.state1);
            }else if  (requestModel.getReq_status() .equals("2") ){
                image_status.setImageResource(R.drawable.state2);
            }else if  (requestModel.getReq_status().equals("3")){
                image_status.setImageResource(R.drawable.state3 );
            }


        }
    }


    private void cancelRequest(){

        try {
            JSONObject data = new JSONObject();
            data.put(getResources().getString(R.string.request_key_id), requestModel.getReq_id());

            Map<String, String> params = new HashMap<String, String>();
            params.put("include", "requestModel");
            params.put("ask", "DELETE_REQUEST");
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
                    if (Boolean.valueOf(dataRequest) == true ) {
                        Home.requestArrayList.remove(indexOfRequest);
                        String uToken = requestModel.getVoulnteer_token();
                        NotificationSubscripion.pushNotificationToToken(uToken,"تم إلغاء طلب تم قبوله");

                        Toast.makeText(StaticVars.getContext() ,
                                "تم إلغاء الطلب" ,Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(StaticVars.getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }
            };
        }catch (Exception ex){
            ex.printStackTrace(System.err);
        }


    }

    private void changeRequestStauts(String newStatus){
        if (!User.get_ID().equals("2")) {return;}
        try {
            JSONObject data = new JSONObject();
            Map<String, String> params = new HashMap<String, String>();

            data.put( getResources().getString(R.string.request_key_id), requestModel.getReq_id());
            data.put(getResources().getString(R.string.request_key_volunteer_id), User.get_ID());
            data.put("newStatus", newStatus);

            String notificationBody =  "حدث تغير علي طلبك" ;
            if (newStatus == "2"){
                notificationBody =  "تم استلام طلبك من قبل المتطوع" ;
             }else if (newStatus == "1"){
                  notificationBody = "تم إلغاء طلب قد تم قبوله" ;
            }
            params.put("ask", "CHANGE_REQUEST_STATUS");
            params.put("include", "requestModel");
            params.put("OS", "ANDROID");
            params.put("data", data.toString());
            final String finalNotificationBody = notificationBody;
            new ServerRequest.Connection_(params) {
                @Override
                public void didStart() {
                    super.didStart();
                }

                @Override
                public void didFinsh(String dataRequest) {
                    super.didFinsh(dataRequest);
                    if (Boolean.parseBoolean(dataRequest)){
                        String uToken = requestModel.getDonator_token();
                        NotificationSubscripion.pushNotificationToToken(uToken, finalNotificationBody
                        );

                        View parentLayout = findViewById(android.R.id.content);

                        Snackbar.make(parentLayout , "تم إرسال طلبك بنجاح", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();


                    }
                }
            };
        }catch (Exception ex){
            ex.printStackTrace(System.err);
        }
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
