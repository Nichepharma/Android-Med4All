package Model;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import APIS.ServerRequest;

/**
 * Created by yahia on 16/08/17.
 */

public class NotificationSubscripion {

    private static String TAG = "NotificationSubscripion";

    //TODO: SUBSCRIPE TOPIC
    public static void subscripTopics (String topic){
        try {
            unSubscripeTopic(getCurrentNotifictionSubscribe());
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        }catch (Exception e){
            Log.e(TAG, "Erorr is " + e.toString());
        }
    }
    //TODO: UNSUBSCRIPE TOPIC
   static void unSubscripeTopic(String topic) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
        }catch (Exception e){
            Log.e(TAG, "Erorr is " + e.toString());
        }
    }
    // TODO:GET CURENT USER SUBSCRIPE TOPIC
    private static String getCurrentNotifictionSubscribe (){
        String currentSubScribe = null;
        try {
            currentSubScribe = User.get_areaID() ;
        }catch (Exception e){
            Log.e(TAG , "error currentSubScribe "  + e) ;
        }
        return currentSubScribe ;
    }

    // TODO:PUSH NOTIFICATION USER SUBSCRIPE TOPIC
    public static void pushNotificationToGroups(String topic , String requestID){
        Map<String, String> params = new HashMap<String, String>();
        params.put("notificationType", "sendToTopic");
        params.put("token_id", topic);
        params.put("title","الدواء للجميع");
        params.put("body", "طلب جديد في منطقتك");
        params.put("request_id" , requestID);
        executeRequest(params);
    }

    // TODO:PUSH NOTIFICATION  USER TOKEN
    public static void pushNotificationToToken(String userToken, String strBody){
        Map<String, String> params = new HashMap<String, String>();
        params.put("notificationType", "token");
        params.put("token_id", userToken);
        params.put("title","الدواء للجميع");
        params.put("body", strBody );
        executeRequest(params);
    }

    private static void executeRequest (Map<String, String> params){
        String notificationUrl = "http://nichepharma.com/med4all/notification/index.php";
        new ServerRequest.Connection_(notificationUrl , params){
            @Override
            public void didStart() {
                super.didStart();
            }
            @Override
            public void didFinsh(String dataRequest) {
                super.didFinsh(dataRequest);
                Log.d(TAG , dataRequest);
            }
        };
    }
}
