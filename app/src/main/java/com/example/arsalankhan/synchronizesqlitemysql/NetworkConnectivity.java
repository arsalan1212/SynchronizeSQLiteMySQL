package com.example.arsalankhan.synchronizesqlitemysql;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arsalan khan on 8/14/2017.
 */

public class NetworkConnectivity extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        if(isNetworkAvailable(context)){

            final dbHelper helper = new dbHelper(context);

            final SQLiteDatabase database = helper.getWritableDatabase();

            Cursor cursor = helper.getLocalData(database);

            while(cursor.moveToNext()){

                int syncState = cursor.getInt(cursor.getColumnIndex(dbContract.SYNC_STATE));

                if(syncState == dbContract.SYNC_STATE_FAILED){

                    final String name = cursor.getString(cursor.getColumnIndex(dbContract.NAME));
                    StringRequest request = new StringRequest(Request.Method.POST, dbContract.url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);

                                        String status = jsonObject.getString("response");

                                        if(status.equals("OK")){

                                            // update sync state in sqlite database
                                            helper.UpadateSyncState(name,dbContract.SYNC_STATE_OK,database);

                                            //send broadcast to update UI
                                            context.sendBroadcast(new Intent(dbContract.INTENT_FILTER));

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<>();

                            params.put("name",name);
                            return params;
                        }
                    };

                    MySingleton.getInstance(context).addToRequestQueue(request);
                }
            }

        }


    }

    private boolean isNetworkAvailable(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());
    }
}
