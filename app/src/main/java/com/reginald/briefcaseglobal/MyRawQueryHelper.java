package com.reginald.briefcaseglobal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Reginald on 16/04/2021.
 */

public class MyRawQueryHelper extends SQLiteOpenHelper {

    public static String dir =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/TheBriefcaseDonotdelete/";
    public static final String DATABASE_NAME ="LinxBriefcaseDB.db";
    public static final int DATABASE_VERSION = 5;
    public static final String TIMESYNC_TABLE_NAME = "TimeSync";
    public MyRawQueryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //you need to create all the table in the oncreate  method so all the table you need to create when the database helper class is acceesssed

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

        Log.e("db-vers", "Updating table from " + oldVersion + " to " + newVersion);
        onCreate(db);
    }

    public void updateDeals(String update)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(update);
    }


/*
    public ArrayList<SettingsModel> getSettings(){
        ArrayList<SettingsModel> labels = new ArrayList();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * From tblSettings", null);

        SettingsModel routes= null;
        if (cursor.moveToFirst()) {
            do {
                routes = new SettingsModel();
                routes.setstrServerIp(cursor.getString(cursor.getColumnIndex("IP")));
                routes.setLocationId(cursor.getString(cursor.getColumnIndex("Location")));

                labels.add(routes);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }
*/
    public void saveCustomerInfo(String CustomerName,String cellnumber,String email,String streetAddress,String Suburb,String uniqueCardId,String cardNumber,String birthDate,String Gender )
    {

        ContentValues values = new ContentValues();
        long dates = new Date().getTime();
        values.put("CustomerName", CustomerName);
        values.put("cellnumber",cellnumber);
        values.put("email",email);
        values.put("streetAddress",streetAddress);
        values.put("Suburb",Suburb);
        values.put("uniqueCardId",uniqueCardId);
        values.put("cardNumber",cardNumber);
        values.put("birthDate",birthDate);
        values.put("Gender",Gender);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert("tblCustomerInfo", null, values);
    }







}
