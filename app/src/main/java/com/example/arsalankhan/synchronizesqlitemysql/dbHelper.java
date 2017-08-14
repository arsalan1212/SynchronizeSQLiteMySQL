package com.example.arsalankhan.synchronizesqlitemysql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Arsalan khan on 8/14/2017.
 */

public class dbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String ID="_id";
    private static final String CREATE_TABLE ="CREATE TABLE "+dbContract.TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                                    +dbContract.NAME+" text,"+dbContract.SYNC_STATE+" INTEGER);";

    private static final String DROP_TABLE= "DROP TABLE IF EXISTS "+dbContract.TABLE_NAME;

    private Context context;
    public dbHelper(Context context){
       super(context,dbContract.DATABASE_NAME,null,DATABASE_VERSION);
        this.context= context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try{
            sqLiteDatabase.execSQL(CREATE_TABLE);
            Toast.makeText(context, "Database Created", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Log.d("TAG","ERROR: "+e.getMessage().toString());
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
        Toast.makeText(context, "Database Upgrade", Toast.LENGTH_SHORT).show();
    }



    // inserting data into database
    public long insert(String name, int SyncState , SQLiteDatabase database){

        ContentValues values = new ContentValues();
        values.put(dbContract.NAME,name);
        values.put(dbContract.SYNC_STATE,SyncState);

       long id= database.insert(dbContract.TABLE_NAME,null,values);
        return id;
    }


    //getting all data
    public Cursor getLocalData(SQLiteDatabase database){

        String columns[] = {dbContract.NAME, dbContract.SYNC_STATE};

        Cursor cursor = database.query(dbContract.TABLE_NAME,columns, null,null,null,null,null);

        return cursor;
    }

    //upadating a specfic data sync state

    public void UpadateSyncState(String name, int syncState, SQLiteDatabase database){

        ContentValues values = new ContentValues();
        values.put(dbContract.SYNC_STATE,syncState);

        String selection = dbContract.NAME+" like ? ";
        String selectionArgs[] = {name};

        database.update(dbContract.TABLE_NAME,values,selection,selectionArgs);
    }


}
