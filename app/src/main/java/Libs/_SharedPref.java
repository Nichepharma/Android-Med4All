package Libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

import Model.NotificationSubscripion;
import Model.User;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by yahia on 19/07/17.
 */

public class _SharedPref {


    public  static String get(Context context, String prefKey) {
        SharedPreferences sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreference.getString(prefKey, "");
        return value;
    }
    public static void set(Context context, String key , Object Object) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key,Object.toString());
        editor.commit();
    }
    public static void setByArray(Context context, JSONObject userInfo) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = preferences.edit();
            for (Iterator<String> it = userInfo.keys(); it.hasNext(); ) {
                String key = it.next();
                editor.putString(key, userInfo.get(key).toString());
            }
            editor.commit();
            NotificationSubscripion.subscripTopics("general");

            if (User.get_typeID().equals("2")){
                //TODO: SUBSCRIBE NOTIFICATION TOPIC
                NotificationSubscripion.subscripTopics(User.get_areaID());
            }

        }catch (Exception ex){
            Log.e(TAG , ex.toString());
        }

    }

}
