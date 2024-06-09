package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.ContentValues;
import android.database.Cursor;
import java.io.ByteArrayOutputStream;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ImageDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Images";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE = "image";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imageInBytes = byteArrayOutputStream.toByteArray();

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGE, imageInBytes);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }

    public Bitmap getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_IMAGE}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            byte[] imageInBytes = cursor.getBlob(0);
            cursor.close();
            return BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);
        }
        return null;
    }
}
