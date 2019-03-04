package p.com.med4all;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import APIS.ServerRequest;
import Libs.StaticVars;
import Model.RequestModel;
import Model.User;
import p.com.med4all.Activities.RequestDetails;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

/**
 * Created by yahia on 13/08/17.
 */

public class HandelMessagingService extends FirebaseMessagingService {
    private static String TAG = HandelMessagingService.class.getCanonicalName();
    private static final int MY_NOTIFICATION_ID=1;
    private NotificationManager notificationManager;
    private Notification myNotification;

    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // Check if message contains a data payload.
        String req_id = "" ;
        if (remoteMessage.getData().size() > 0) {
            req_id = remoteMessage.getData().get("request_id");
            if (isAppInForeground(getApplicationContext())) {
                optionalAlert(req_id , remoteMessage.getNotification().getBody());

            } else {
                getRequestByID(req_id);
                Intent myIntent = new Intent(getApplicationContext(), RequestDetails.class);
                @SuppressLint("WrongConstant")
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        myIntent,
                        Intent.FLAG_ACTIVITY_NEW_TASK);

                myNotification = new NotificationCompat.Builder(getApplicationContext())
                        .setContentTitle("Exercise of Notification!")
                        .setContentText("Do Something...")
                        .setTicker("Notification!")
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .build();

                notificationManager =
                        (NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(MY_NOTIFICATION_ID, myNotification);

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().toString());

            optionalAlert(req_id , remoteMessage.getNotification().getBody());

        }
    }


    private void optionalAlert (final String requesID , String strMessage) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(
                this, R.style.mainActivityTheme));

        if (requesID != "" ){
            builder.setMessage(strMessage)
                    .setTitle("تنبيه")
                    .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                    .setPositiveButton("فتح", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getRequestByID(requesID);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("اخفاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }
                    });
        }else {
            // if no id
            builder.setMessage(strMessage)
                    .setTitle("تنبيه")
                    .setIcon(getResources().getDrawable(R.mipmap.ic_launcher))
                    .setNegativeButton("اخفاء", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                            dialog.dismiss();
                        }
                    });
        }

        try {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    alertDialog.show();
                    Looper.loop();

                }
            }.start();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }


    }

    private void getRequestByID(String requestID) {
    if (requestID == ""){
        Toast.makeText(this, "حدث خطاء" , Toast.LENGTH_LONG).show();
        return;
    }
        try {
            JSONObject data = new JSONObject();
            data.put(getResources().getString(R.string.request_key_id), requestID);

            Map<String, String> params = new HashMap<String, String>();
            params.put("include", "requestModel");
            params.put("ask", "GET_REQUEST_BY_ID");
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
                    if (dataRequest == null || dataRequest.equals("null")) {
                        return;
                    }
                    Log.d(TAG, dataRequest.toString());
                    try {
                        RequestModel requestModel = new RequestModel();


                        JSONObject request = new JSONObject(dataRequest);

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
                        } else if (User.get_ID().equals("2")) {
                            requestModel.setDonator_id(request.getString(getResources().getString(R.string.request_key_donator_id)));
                            requestModel.setDonator_first_name(request.getString(getResources().getString(R.string.request_key_donator_f_name)));
                            requestModel.setDonator_last_name(request.getString(getResources().getString(R.string.request_key_donator_l_name)));
                            requestModel.setDonator_phone(request.getString(getResources().getString(R.string.request_key_donator_phone)));
                            requestModel.setDonator_token(request.getString(getResources().getString(R.string.request_key_donator_token)));
                        }

                        openRequestDetailsActivity(requestModel);

                    } catch (Exception ex) {
                        ex.printStackTrace(System.err);
                        Toast.makeText(StaticVars.getContext(), "حدث خطاء", Toast.LENGTH_LONG).show();
                    }

                }
            };

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openRequestDetailsActivity(RequestModel requestModel ) {

        Intent intent = new Intent(getApplicationContext() , RequestDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("newStatus",requestModel.getReq_status());
        intent.putExtra("RequestModel", (Serializable) requestModel);
        startActivity(intent);
    }

    private boolean isAppInForeground(Context context)
    {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
            String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();

            return foregroundTaskPackageName.toLowerCase().equals(context.getPackageName().toLowerCase());
        }
        else
        {
            ActivityManager.RunningAppProcessInfo appProcessInfo = new ActivityManager.RunningAppProcessInfo();
            ActivityManager.getMyMemoryState(appProcessInfo);
            if (appProcessInfo.importance == IMPORTANCE_FOREGROUND || appProcessInfo.importance == IMPORTANCE_VISIBLE)
            {
                return true;
            }

            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            // App is foreground, but screen is locked, so show notification
            return km.inKeyguardRestrictedInputMode();
        }
    }

}
