package com.example.arsalankhan.synchronizesqlitemysql;

/**
 * Created by Arsalan khan on 8/14/2017.
 */

public class contract {

    private String name;
    private int SyncState;


    public contract(String name, int syncState) {
        this.name = name;
        SyncState = syncState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSyncState() {
        return SyncState;
    }

    public void setSyncState(int syncState) {
        SyncState = syncState;
    }
}
