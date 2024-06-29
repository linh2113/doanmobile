package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Khai báo tên và phiên bản của cơ sở dữ liệu
    private static final String DATABASE_NAME = "USER_INFO";
    private static final int DATABASE_VERSION = 1;

    // Khai báo tên bảng và các cột của bảng
    public static final String TABLE_USERS = "USER_INFO";
    public static final String COLUMN_DISPLAY_NAME = "display_name";
    public static final String COLUMN_GMAIL = "gmail";
    public static final String COLUMN_TOKEN = "token";

    // Constructor của lớp DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users với các cột display_name, gmail, token
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_DISPLAY_NAME + " TEXT PRIMARY KEY,"
                + COLUMN_GMAIL + " TEXT,"
                + COLUMN_TOKEN + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Thêm dữ liệu mẫu vào bảng users
        String INSERT_SAMPLE_USER = "INSERT INTO " + TABLE_USERS + "("
                + COLUMN_DISPLAY_NAME + "," + COLUMN_GMAIL + "," + COLUMN_TOKEN + ") VALUES('sampleUser', 'tinyfox@gmail.com', 'b37e3a99c33e8b21f4cfe6175f91ad0ecf06d87b')";
        db.execSQL(INSERT_SAMPLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng users nếu đã tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Tạo lại bảng
        onCreate(db);
    }

    // Phương thức để cập nhật display_name
    public void updateDisplayName(String oldDisplayName, String newDisplayName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DISPLAY_NAME, newDisplayName);
        db.update(TABLE_USERS, values, COLUMN_DISPLAY_NAME + "=?", new String[]{oldDisplayName});
        db.close();
    }

    // Phương thức để lấy display_name hiện tại (giả định đã đăng nhập)
    public String getCurrentDisplayName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_DISPLAY_NAME}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_DISPLAY_NAME);
            if (columnIndex != -1) {
                String displayName = cursor.getString(columnIndex);
                cursor.close();
                return displayName;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Phương thức để lấy giá trị của gmail và token từ bảng users
    public String getUserGmail() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_GMAIL}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_GMAIL);
            if (columnIndex != -1) {
                String gmail = cursor.getString(columnIndex);
                cursor.close();
                return gmail;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public String getUserToken() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_TOKEN}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_TOKEN);
            if (columnIndex != -1) {
                String token = cursor.getString(columnIndex);
                cursor.close();
                return token;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }
}
