package Libs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

/**
 * Created by yahia on 20/07/17.
 */

public class StaticVars {

    public static Context getContext() {
        return _context;
    }

    public static void setContext(Context context) {
        _context = context;
    }



    @SuppressLint("HardwareIds")
    public static  String get_DeviceToken() {
        if (_DeviceToken == null)
            _DeviceToken = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        return _DeviceToken;
    }






    private static Context _context ;
    private static String _DeviceToken ;
}
