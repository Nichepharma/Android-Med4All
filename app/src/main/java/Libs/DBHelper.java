package Libs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.util.ArrayList;

import Model.RequestModel;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by yahia on 06/08/17.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Med4All.db";
    public static final String TABLE_NAME = "favouritRequest";


    private static final String SQL_CREATE_FAVOURIT =
            "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +
                    "(f_id  INTEGER PRIMARY KEY AUTOINCREMENT , " +
                    "request_id TEXT UNIQUE ," +
                    "med_name TEXT ," +
                    "request_date TEXT ," +
                    "donator_id TEXT , " +
                    "donator_f_name TEXT , " +
                    "donator_l_name TEXT , " +
                    "donator_phone TEXT , " +
                    "country_id TEXT , " +
                    "country_name TEXT , " +
                    "city_id TEXT , " +
                    "city_name TEXT, " +
                    "area_id TEXT , " +
                    "area_name TEXT," +
                    "address_detail TEXT, " +
                    "donator_availableTime TEXT," +
                    "item_count TEXT, " +
                    "request_note TEXT, " +
                    "request_status TEXT) ;" ;

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }

    public static boolean  insert_favourit(Context context  , ContentValues values){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try{
            long newRowId = db.insert(dbHelper.TABLE_NAME, null, values);
            if (newRowId != -1 ){
                return true ;
            }
            return false ;
        }catch (Exception ex){
            ex.printStackTrace();
            return false ;
        }
    }
    public static ArrayList select_favourit(Context context ){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<RequestModel> requestArrayList = new ArrayList<RequestModel>();
        requestArrayList.clear();
        Cursor cursor = db.rawQuery("SELECT " +
                                    "`request_id` ," +
                                    " `med_name` , " +
                                    "`item_count` ," +
                                    "`request_date` , " +
                                    "`donator_id` ," +
                                    "`donator_f_name` ," +
                                    "`donator_l_name` ," +
                                    "`donator_phone` ," +
                                    "`country_id` ," +
                                    "`country_name` ," +
                                    "`city_id` ," +
                                    "`city_name` ," +
                                    "`area_id` ," +
                                    "`area_name` ," +
                                    "`address_detail` ," +
                                    "`donator_availableTime` ," +
                                    "`request_note` ," +
                                    "`request_status` " +
                                    " FROM "+ TABLE_NAME + "", null);
        if(cursor.moveToFirst()){
            do{
                RequestModel requestModel = new RequestModel();

                requestModel.setReq_id(cursor.getString(0));
                requestModel.setMedicine_name(cursor.getString(1));
                requestModel.setMedicine_itemCount(cursor.getString(2));
                requestModel.setReq_date(cursor.getString(3));
                requestModel.setDonator_id(cursor.getString(4));
                requestModel.setDonator_first_name(cursor.getString(5));
                requestModel.setDonator_last_name(cursor.getString(6));
                requestModel.setDonator_phone(cursor.getString(7));
                requestModel.setDonator_country_id(cursor.getString(8));
                requestModel.setDonator_country_name(cursor.getString(9));
                requestModel.setDonator_city_id(cursor.getString(10));
                requestModel.setDonator_city_name(cursor.getString(11));
                requestModel.setDonator_area_id(cursor.getString(12));
                requestModel.setDonator_area_name(cursor.getString(13));
                requestModel.setDonator_address(cursor.getString(14));
                requestModel.setDonator_availableTime(cursor.getString(15));
                requestModel.setDonator_notes(cursor.getString(16));
                requestModel.setReq_status(cursor.getString(17));
                requestArrayList.add(requestModel);

            }while(cursor.moveToNext());
        }
        cursor.close();

        return  requestArrayList ;
    }
    public static void delete_favourit(Context context ,Object request_id){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            db.execSQL("DELETE FROM " + DBHelper.TABLE_NAME + " WHERE request_id = " + request_id);
            } catch (SQLiteException e) {
            e.printStackTrace();
         }
    }

    public static boolean checkIsAvailable (Context context  , String requestID){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;

        String query = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + " request_id  = " + requestID;

        try {
            Cursor cursor = db.rawQuery(query, null);
            exists = (cursor.getCount() > 0);
            cursor.close();

        } catch (SQLiteException e) {

            e.printStackTrace();
            db.close();

        }

        return  exists ;
    }
}
