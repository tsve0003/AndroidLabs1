package com.example.androidlabs1;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

 public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "MessagesDB";
    static final String DB_TABLE = "Messages_Table";

    //columns
    static final String COL_MESSAGE = "Message";
    static final String COL_ISSEND = "IsSend";
    static final String COL_MESSAGEID = "MessageID";

    //queries
    private static final String CREATE_TABLE = "CREATE TABLE "+DB_TABLE+" ("+COL_MESSAGEID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_MESSAGE+" TEXT, "+COL_ISSEND+" BIT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }

    //insert data
    public boolean insertData(String message, boolean isSend) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MESSAGE, message);
        if (isSend)
            contentValues.put(COL_ISSEND, 0);
        else
            contentValues.put(COL_ISSEND, 1);

        long result = db.insert(DB_TABLE, null, contentValues);

        return result != -1; //if result = -1 data doesn't insert
    }

    //view data
    public Cursor viewDataDb(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        printCursor(cursor);
        return cursor;
    }

    public void printCursor(Cursor cursor){
        SQLiteDatabase db = this.getReadableDatabase();
        Log.v("Database Version:", Integer.toString(db.getVersion()));
        Log.v("Number of columns: ", Integer.toString(cursor.getColumnCount()));
        for (int i = 0; i < cursor.getColumnCount(); i++){
            Log.v("Column "+(i+1)+": ", cursor.getColumnName(i));
        }
        Log.v("Number of rows:", Integer.toString(cursor.getCount()));
        Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));



    }
}


