package com.example.w3d1_sqlite_simple;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String EXAMPLE_NAME = "Edwin";
    private static final String EXAMPLE_AGE = "31";

    private static final String TAG = "MainActivityTAG_";

    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = ((TextView) findViewById(R.id.result));
    }

    public void doMagic(View view) {
        UsersDatabaseHelper usersDatabaseHelper = new UsersDatabaseHelper(getApplicationContext());
        SQLiteDatabase db = usersDatabaseHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(UsersDatabaseHelper.KEY_USER_NAME, EXAMPLE_NAME);
            values.put(UsersDatabaseHelper.KEY_AGE, EXAMPLE_AGE);

            db.insertOrThrow(UsersDatabaseHelper.TABLE_USERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while trying to add post to database");
        } finally {
            db.endTransaction();
        }
    }

    public void readData(View view) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try{
            UsersDatabaseHelper usersDatabaseHelper = new UsersDatabaseHelper(getApplicationContext());
            db = usersDatabaseHelper.getReadableDatabase();

            StringBuilder result = new StringBuilder(100);

            cursor = db.rawQuery("SELECT * FROM " + UsersDatabaseHelper.TABLE_USERS, null);
            int nameIndex = cursor.getColumnIndex(UsersDatabaseHelper.KEY_USER_NAME);
            int ageIndex = cursor.getColumnIndex(UsersDatabaseHelper.KEY_AGE);
            int idIndex = cursor.getColumnIndex("id");

            if(cursor.moveToFirst()){
                do{
                    result.append(cursor.getInt(idIndex)).append(" ")
                            .append(cursor.getString(nameIndex)).append(" ")
                            .append(cursor.getInt(ageIndex)).append("\n");
                }while (cursor.moveToNext());
            }

            resultView.setText(result.toString());
        }catch (SQLiteException e){
            e.printStackTrace();
            Toast.makeText(this, "Error accessing db", Toast.LENGTH_SHORT).show();
        }finally {
            if(cursor != null){
                cursor.close();
            }
            if (db != null){
                db.close();
            }
        }

    }
}
