package com.example.arsalankhan.synchronizesqlitemysql;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private EditText editTextName;
    ArrayList<contract> arrayListContract= new ArrayList<>();
    MyAdapter adapter;
    private BroadcastReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editTextName = (EditText) findViewById(R.id.et_Name);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        adapter = new MyAdapter(arrayListContract);
        mRecyclerView.setAdapter(adapter);


        readFromLocalDatabase();

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                readFromLocalDatabase();
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mReceiver, new IntentFilter(dbContract.INTENT_FILTER));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
    }

    //btn onclick
    public void saveData(View view){

        String name = editTextName.getText().toString();
        if(!TextUtils.isEmpty(name)){

            if(isNetworkAvailable()){

                saveDataOnServer(name);

            }
            else{
               saveDataLocally(name, dbContract.SYNC_STATE_FAILED);
            }

       readFromLocalDatabase();

        }
        else{
            Toast.makeText(this, "Fill the Field", Toast.LENGTH_SHORT).show();
        }


    }


    //save data on server
    private void saveDataOnServer(final String name) {


        StringRequest request = new StringRequest(Request.Method.POST, dbContract.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response!=null){
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String status = jsonObject.getString("response");

                        if(status.equals("OK")){

                            saveDataLocally(name,dbContract.SYNC_STATE_OK);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                        saveDataLocally(name, dbContract.SYNC_STATE_FAILED);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                saveDataLocally(name,dbContract.SYNC_STATE_FAILED);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("name",name);
                return params;
            }
        };
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }

//save data into sqlite database
    private  void saveDataLocally(String name, int syncState){

        dbHelper helper = new dbHelper(this);

        SQLiteDatabase database = helper.getWritableDatabase();
        long id= helper.insert(name,syncState,database);
    }

    // reading from SQLITE database
    private void readFromLocalDatabase(){

        arrayListContract.clear();

        dbHelper helper = new dbHelper(this);
        SQLiteDatabase database= helper.getReadableDatabase();

        Cursor cursor = helper.getLocalData(database);

        while(cursor.moveToNext()){

            int syncstate = cursor.getInt(cursor.getColumnIndex(dbContract.SYNC_STATE));
            String name = cursor.getString(cursor.getColumnIndex(dbContract.NAME));

            contract c = new contract(name,syncstate);

            arrayListContract.add(c);
        }

        cursor.close();
        database.close();

        //refresh the recyclerview
        adapter.notifyDataSetChanged();
    }


    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo !=null && networkInfo.isConnected());
    }
}
