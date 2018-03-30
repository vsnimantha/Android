package com.example.toshibat.practicalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

/**
 * Created by toshiba t on 24/9/2015.
 */
public class Methods extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="COMPANY2.db";
    public static final String TABLE_NAME="company";
    public static final String COL_1="ID";
    public static final String COL_2="Company_Name";
    public static final String COL_3="Company_Address";
    public static final String COL_4="Company_Phone1";
    public static final String COL_5="Company_Phone2";
    public static final String COL_6="Company_Fax";
    public static final String COL_7="Company_Email";
    public static final String COL_8="Company_Web";
    public static final String COL_9="Company_logo";








    public Methods(Context context) {
        super(context,DATABASE_NAME,null,1);
       // SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY,Company_Name VARCHAR,Company_Address VARCHAR,Company_Phone1 VARCHAR,Company_Phone2 VARCHAR," +
                "Company_Fax VARCHAR,Company_Email VARCHAR,Company_Web VARCHAR,Company_Logo BLOB)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }
    public boolean insertData(Integer id,String name,String addr,String phn1,String phn2,String fax ,String email,String web,byte[] image) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,addr);
        contentValues.put(COL_4,phn1);
        contentValues.put(COL_5,phn2);
        contentValues.put(COL_6,fax);
        contentValues.put(COL_7,email);
        contentValues.put(COL_8,web);
        contentValues.put(COL_9,image);

        long result = db.insert(TABLE_NAME, null, contentValues);


        if(result==-1){
            return false;
        }else
            return true;
    }

    public Cursor viewData(){
        SQLiteDatabase db=this.getWritableDatabase();
       // Cursor cursor=db.rawQuery("select ID,Company_Name,Company_Address,Company_Phone1,Company_Phone2,Company_Fax,Company_Email,Company_Web from "+TABLE_NAME,null);
        Cursor cursor=db.rawQuery("select * from "+TABLE_NAME,null);
        return cursor;

    }
    public boolean updateData(String id,String name,String addr,String phn1,String phn2,String fax ,String email,String web){
        if(true){
            return true;
        }else
            return false;
    }
    public int deleteData(String id){

        return 0;
    }
}
