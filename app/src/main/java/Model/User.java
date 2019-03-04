package Model;


import Libs.StaticVars;
import Libs._SharedPref;
import p.com.med4all.R;

public class User {

    public static  String get_ID() {
        if (_ID.equals(""))
            _ID = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_id));
        return _ID;

    }

    public static String get_first_name() {
        if (_first_name.equals(""))
            _first_name = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_f_name));
        return _first_name;
    }

    public static String get_last_name() {
        if (_last_name.equals(""))
            _last_name = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_l_name));
        return _last_name;
    }

    public static String get_phone() {
        if (_phone.equals(""))
            _phone = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_phone));
        return _phone;
    }

    public static String get_email() {
        if (_email.equals(""))
            _email = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_email));
        return _email;
    }

    public static String get_countryID() {
        if (_countryID.equals(""))
            _countryID = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_city_id ));
        return _countryID;
    }

    public static String get_country_name() {
        if (_country_name.equals(""))
            _country_name  = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_country_name ));
        return _country_name;
    }

    public static String get_CityID() {
        if (_CityID.equals(""))
            _CityID = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_city_id ));
        return _CityID;
    }

    public static String get_city_name() {
        if (_city_name.equals(""))
            _city_name = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_city_name));
        return _city_name;
    }

    public static String get_areaID() {
        if (_areaID.equals(""))
            _areaID = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_area_id ));
        return _areaID;
    }

    public static String get_area_name() {
        if (_area_name.equals(""))
            _area_name = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_area_name));
        return _area_name;
    }

    public static String get_address() {
        if (_address.equals(""))
            _address = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_address));
        return _address;
    }

    public static String get_typeID() {

        if (_typeID.equals(""))
            _typeID = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_type_id));
        return _typeID;

    }

    public static String get_typeValue() {

        if (_typeValue.equals(""))
            _typeValue = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_type_value));
        return _typeValue;
    }

    public static String get_token() {
        if (_token.equals(""))
            _token = _SharedPref.get(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_token));
        return _token;
    }


    public static void set_ID(String _ID) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_id) , _ID);
        User._ID = _ID;
    }

    public static void set_first_name(String _first_name) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_f_name) , _first_name);
        User._first_name = _first_name;
    }

    public static void set_last_name(String _last_name) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_l_name) , _last_name);
        User._last_name = _last_name;
    }

    public static void set_phone(String _phone) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_phone) , _phone);
        User._phone = _phone;
    }

    public static void set_email(String _email) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_email) , _email);
        User._email = _email;
    }

    public static void set_countryID(String _countryID) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_country_id) , _countryID);
        User._countryID = _countryID;
    }

    public static void set_country_name(String _country_name) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_country_name) , _country_name);
        User._country_name = _country_name;
    }

    public static void set_CityID(String _CityID) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_city_id), _CityID);
        User._CityID = _CityID;
    }

    public static void set_city_name(String _city_name) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_city_name) , _city_name);
        User._city_name = _city_name;
    }

    public static void set_areaID(String _areaID) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_area_id) , _areaID);
        NotificationSubscripion.subscripTopics(_areaID);
        User._areaID = _areaID;
    }

    public static void set_area_name(String _area_name) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_area_name) , _area_name);
        User._area_name = _area_name;
    }

    public static void set_address(String _address) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_address) , _address);
        User._address = _address;
    }

    public static void set_typeID(String _typeID) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_type_id) , _typeID);
        User._typeID = _typeID;
    }

    public static void set_token(String _token) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_token) , _token);
        User._token = _token;
    }    public static void set_typeValue(String _typeValue) {
        _SharedPref.set(StaticVars.getContext() , StaticVars.getContext().getString(R.string.user_key_type_value) , _typeValue);
        User._typeValue = _typeValue;
    }

    private static String _ID = "" ;
    private static String _first_name = "";
    private static String _last_name = "" ;
    private static String _phone  = "";
    private static String _email = "" ;
    private static String _countryID = "";
    private static String _country_name = "" ;
    private static String _CityID = "";
    private static String _city_name = "" ;
    private static String _areaID = "";
    private static String _area_name = "" ;
    private static String _address = "" ;
    private static String _typeID = "";
    private static String _typeValue = "";
    private static String _token = "";


}
