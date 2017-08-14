package com.example.arsalankhan.synchronizesqlitemysql;

/**
 * Created by Arsalan khan on 8/14/2017.
 */

public class dbContract {

    public static final int SYNC_STATE_OK =0;
    public static final int SYNC_STATE_FAILED =1;

    public static final String TABLE_NAME="personInfo";
    public static final String DATABASE_NAME="personDb";

    public static final String NAME="name";
    public static final String SYNC_STATE="syncstate";

    public static final String url ="http://192.168.10.3/android/savedata.php";

    public static final String INTENT_FILTER ="com.example.arsalankhan.synchronizesqlitemysql.BROADCAST";
}
