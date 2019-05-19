package com.example.a18frior_app_project;

import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.Attributes;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.provider.ContactsContract;

import android.util.Log;


public class DestinationsReaderDbHelper extends SQLiteOpenHelper {
    private static Context myContext;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DestinationsReader.db";
    // Contacts table name
    private static final String TABLE_DESTINATIONS ="Destinations";

    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_Name = "Name";
    public static final String COLUMN_NAME_Location = "Location";
    public static final String COLUMN_NAME_Citizen = "Citizen";
    public static final String COLUMN_NAME_Category = "Category";
    public static final String COLUMN_NAME_Info = "Info";



    public DestinationsReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext=context;
    }
    public boolean delete()

    {
        myContext.deleteDatabase(DATABASE_NAME);
        return true;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE "+ TABLE_DESTINATIONS + "(" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME_Name+ " TEXT," + COLUMN_NAME_Location + " TEXT,"
                + COLUMN_NAME_Citizen + " TEXT,"+ COLUMN_NAME_Category + " TEXT," + COLUMN_NAME_Info+  " TEXT" +")";

        Log.e("CREATE", CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_DESTINATIONS);

        onCreate(db);
    }

    public void addDestinatoin(Destinations destinations) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Name, destinations.getName());
        values.put(COLUMN_NAME_Location,destinations.getLocation());
        values.put(COLUMN_NAME_Citizen,destinations.getCitizen());
        values.put(COLUMN_NAME_Category,destinations.getCategory());
        values.put(COLUMN_NAME_Info,destinations.getInfo());
// Inserting Row
        db.insert(TABLE_DESTINATIONS, null, values);

        String countQuery = "SELECT * FROM " + TABLE_DESTINATIONS;
        Cursor cursor = db.rawQuery(countQuery, null);

// return count
        Log.d("FridaRockar",""+cursor.getCount() + " added: " +destinations.getName());

        /*db.close(); // Closing database connection*/
    }

    public  Destinations getMountain(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DESTINATIONS, new String[]{COLUMN_NAME_Name,
                        COLUMN_NAME_Location, COLUMN_NAME_Citizen, COLUMN_NAME_Category, COLUMN_NAME_Info}, COLUMN_NAME_Name + "=?",
                new String[]{name}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

        if(cursor.getCount() == 0) {
            return null;
        }

        Destinations destinations = new Destinations(cursor.getString(1),
                cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getString(5));

        return destinations;
    }

    public int getDBSSize(){

        String selectQuery = "SELECT * FROM " + TABLE_DESTINATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor.getCount();
    }

    public ArrayList<Destinations> getAllMountains() {

        ArrayList <Destinations> bergTemp = new ArrayList<Destinations>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_DESTINATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("Frida rocker", ""+cursor.getCount());

// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            int counter = 0;
            do {
                //for(int i = 0;  i < cursor.getCount(); i++) {
                //   Mountain mountain;
                /*
                Mountain mountain = new Mountain(COLUMN_NAME_Name, COLUMN_NAME_Location, COLUMN_NAME_Height, COLUMN_NAME_Img_url, COLUMN_NAME_InfoUrl);
                mountain.setName(cursor.getString(0));
                mountain.setLocation(cursor.getString(1));
                mountain.setHeight(cursor.getString(2));
                mountain.setImg_url(cursor.getString(3));
                mountain.setInfoUrl(cursor.getString(4));*/

                String name = cursor.getString(1);
                String location = cursor.getString(2);
                String citizen = cursor.getString(3);
                String category = cursor.getString(4);
                String info = cursor.getString(5);
                //mountain = new Mountain(
                bergTemp.add(new Destinations(
                        name,
                        location,
                        citizen,
                        category,
                        info
                ));
                Log.d("Frida Rockar4", "_" + counter + "_" + cursor.getString(0) + " " + cursor.getString(1));

// Adding contact to list
                //bergTemp.add(mountain);

                Log.d("Frida RockarAddMountain", "_" + counter + "_ size:" + bergTemp.size());
                for (int j = 0; j < bergTemp.size(); j++) {
                    Log.d("Frida RockarAddMountain", "\t_" + counter +""+j+ "" + bergTemp.get(j).getName());
                }

                counter++;

            } while (cursor.moveToNext());
        }

        Log.d("Frida Rockar4", ""+bergTemp.size());
        return bergTemp;
    }

    public int getDestinationsCount() {
        String countQuery = "SELECT * FROM " + TABLE_DESTINATIONS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();


        return cursor.getCount();
    }

    public int updateDestinations(Destinations destinations) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_Name, destinations.getName());
        values.put(COLUMN_NAME_Location, destinations.getLocation());
        values.put(COLUMN_NAME_Citizen, destinations.getCitizen());
        values.put(COLUMN_NAME_Category, destinations.getCategory());
        values.put(COLUMN_NAME_Info, destinations.getInfo());
        return db.update(TABLE_DESTINATIONS, values, COLUMN_NAME_Name+ " = ?",
                new String[]{String.valueOf(destinations.getName())});
    }


    public void deleteDestinations(Destinations destinations) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DESTINATIONS, COLUMN_NAME_Name + " = ?",
                new String[] { String.valueOf(destinations.getName()) });
        /*db.close();*/
    }
}