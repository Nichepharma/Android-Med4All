package APIS;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import Libs.StaticVars;
import p.com.med4all.R;

/**
 * Created by yahia on 6/17/2016.
 */
public class ServerRequest {
    interface interfaceConnection {
        void didStart();

        void didFinsh(String dataRequest);
    }


    public abstract static class Connection_ implements interfaceConnection {
        public Connection_(Map<String, String> params) {
            new ServerConnect(params).execute();
        }

        public Connection_(String otherUrl, Map<String, String> params) {
            new ServerConnect(otherUrl, params).execute();
        }

        @Override
        public void didStart() {
        }

        @Override
        public void didFinsh(String dataRequest) {

        }

        private class ServerConnect extends AsyncTask<String, String, String> {
            String str_link = StaticVars.getContext().getResources().getString(R.string.serverLink);
            private Map<String, String> params2 = new HashMap<>();

            protected ServerConnect(Map<String, String> paramValue) {
                didStart();
                params2 = paramValue;
                Log.d("ServerConnect", params2.toString());
            }

            protected ServerConnect(String apiURL, Map<String, String> paramValue) {
                str_link = apiURL;
                didStart();
                params2 = paramValue;
                Log.d("ServerConnect", params2.toString());
            }

            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override
            protected String doInBackground(String... params) {
                // TODO Auto-generated method stub
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                /*
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {*/
                if (params2.get("otherUrl") != null ){
                    str_link =  params2.get("otherUrl") ;
                }
                RequestQueue queue;
                queue = Volley.newRequestQueue(StaticVars.getContext());
                // TODO Auto-generated method stub
                StringRequest strreq = new StringRequest(Request.Method.POST, str_link, new Response.Listener<String>() {

                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String arg0) {
                        // TODO Auto-generated method stub
                        Log.d(">>> Type Data <<< ", arg0);
                        didFinsh(arg0);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // TODO Auto-generated method stub
                        VolleyLog.d("failed", "Error: " + arg0.getMessage());
                        //    Toast.makeText(getApplicationContext(), "error connection to server", Toast.LENGTH_LONG).show();
                        didFinsh(arg0.toString());
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        return params2;
                    }
                };

                queue.add(strreq);
                  /*  }
                });*/


                return null;
            }


        }

    }


}
