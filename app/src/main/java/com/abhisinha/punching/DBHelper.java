package com.abhisinha.punching;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "employee";

    // Country table name
    private static final String TABLE_EMP= "EMP";

    // Country Table Columns names
    private static final String KEY_ID = "id";
    private static final String NAME = "NAME";
    private static final String EMP_ID = "EMP_ID";
    private static final String DATE = "DATE";
    private static final String TIME = "TIME";
    private static final String ADDRESS = "ADDRESS";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATETABLE = "CREATE TABLE " + TABLE_EMP + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME + " TEXT,"
                + EMP_ID +" TEXT, DATE TEXT, TIME TEXT, ADDRESS TEXT)";

        db.execSQL(CREATETABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMP);
        // Create tables again
        onCreate(db);
    }

    // Adding new country
   public long empInsert(String eMP_ID,String nAME,String dATE,String tIME,String aDDRESS) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EMP_ID, eMP_ID);
        values.put(NAME, nAME);
        values.put(DATE, dATE);
        values.put(TIME, tIME);
        values.put(ADDRESS, aDDRESS);


        // Inserting Row
      long count=  db.insert(TABLE_EMP, null, values);

       Log.e("TAG", "addCountry: "+count );
        db.close(); // Closing database connection
       return count;




    }



  public Cursor  displayAllData(){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
       Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_EMP,null);

        return cursor;

    }

}
