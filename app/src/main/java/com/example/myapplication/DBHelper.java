package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userdb";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_AVATAR = "avatar"; // Thêm cột avatar

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_AVATAR + " TEXT" + ")"; // Thêm cột avatar
        db.execSQL(CREATE_USERS_TABLE);

        // Thêm dữ liệu mẫu với COLUMN_AVATAR
        String INSERT_SAMPLE_USER = "INSERT INTO " + TABLE_USERS + "("
                + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ", " + COLUMN_AVATAR + ") VALUES('sampleUser', 'password123', null)";
        db.execSQL(INSERT_SAMPLE_USER);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Phương thức để cập nhật username
    public void updateUsername(String oldUsername, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, newUsername);
        db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{oldUsername});
        db.close();
    }

    // Phương thức để lấy username hiện tại (giả định đã đăng nhập)
    public String getCurrentUsername() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            if (columnIndex != -1) {
                String username = cursor.getString(columnIndex);
                cursor.close();
                return username;
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    // Phương thức để cập nhật password
    public void updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
    }
    // Phương thức để kiểm tra mật khẩu hiện tại
    public boolean checkCurrentPassword(String username, String currentPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PASSWORD}, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int passwordColumnIndex = cursor.getColumnIndex(COLUMN_PASSWORD);
            if (passwordColumnIndex != -1) {
                String password = cursor.getString(passwordColumnIndex);
                cursor.close();
                return password.equals(currentPassword);
            } else {
                cursor.close();
                throw new IllegalArgumentException("Column not found: " + COLUMN_PASSWORD);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }

    // Phương thức để cập nhật avatar
    public void updateAvatar(String username, String avatarUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AVATAR, avatarUri);
        db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close();
    }

    // Phương thức để lấy avatar hiện tại
    public String getAvatar(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_AVATAR}, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int avatarColumnIndex = cursor.getColumnIndex(COLUMN_AVATAR);
            if (avatarColumnIndex != -1) {
                String avatar = cursor.getString(avatarColumnIndex);
                cursor.close();
                return avatar;
            } else {
                cursor.close();
                throw new IllegalArgumentException("Column not found: " + COLUMN_AVATAR);
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }



}