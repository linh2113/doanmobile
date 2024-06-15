package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper { // Khai báo lớp DBHelper kế thừa từ SQLiteOpenHelper

    // Khai báo tên và phiên bản của cơ sở dữ liệu
    private static final String DATABASE_NAME = "userdb";
    private static final int DATABASE_VERSION = 1;

    // Khai báo tên bảng và các cột của bảng
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Constructor của lớp DBHelper
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users với các cột username (PRIMARY KEY) và password
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USERNAME + " TEXT PRIMARY KEY,"
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Thêm dữ liệu mẫu vào bảng users
        String INSERT_SAMPLE_USER = "INSERT INTO " + TABLE_USERS + "("
                + COLUMN_USERNAME + ", " + COLUMN_PASSWORD + ") VALUES('sampleUser', 'password123')";
        db.execSQL(INSERT_SAMPLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng users nếu đã tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Tạo lại bảng
        onCreate(db);
    }

    // Phương thức để cập nhật username
    public void updateUsername(String oldUsername, String newUsername) {
        SQLiteDatabase db = this.getWritableDatabase(); // Lấy cơ sở dữ liệu có thể ghi
        ContentValues values = new ContentValues(); // Tạo đối tượng ContentValues để chứa các giá trị cần cập nhật
        values.put(COLUMN_USERNAME, newUsername); // Đặt giá trị mới cho cột username
        // Cập nhật bảng users với điều kiện cột username khớp với oldUsername
        db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{oldUsername});
        db.close(); // Đóng kết nối cơ sở dữ liệu
    }

    // Phương thức để lấy username hiện tại (giả định đã đăng nhập)
    public String getCurrentUsername() {
        SQLiteDatabase db = this.getReadableDatabase(); // Lấy cơ sở dữ liệu có thể đọc
        // Truy vấn để lấy cột username từ bảng users
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USERNAME}, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) { // Kiểm tra xem cursor có dữ liệu không,moveToFirst: có ít nhất 1 hàng
            int columnIndex = cursor.getColumnIndex(COLUMN_USERNAME); // Lấy chỉ số của cột username
            if (columnIndex != -1) { // Nếu cột tồn tại
                String username = cursor.getString(columnIndex); // Lấy giá trị của cột username
                cursor.close(); // Đóng cursor
                return username; // Trả về username
            }
        }
        if (cursor != null) {
            cursor.close(); // Đóng cursor nếu không có dữ liệu
        }
        return null; // Trả về null nếu không tìm thấy username
    }

    // Phương thức để cập nhật password
    public void updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase(); // Lấy cơ sở dữ liệu có thể ghi
        ContentValues values = new ContentValues(); // Tạo đối tượng ContentValues để chứa các giá trị cần cập nhật
        values.put(COLUMN_PASSWORD, newPassword); // Đặt giá trị mới cho cột password
        // Cập nhật bảng users với điều kiện cột username khớp với username
        db.update(TABLE_USERS, values, COLUMN_USERNAME + "=?", new String[]{username});
        db.close(); // Đóng kết nối cơ sở dữ liệu
    }

    // Phương thức để kiểm tra mật khẩu hiện tại có đúng không
    public boolean checkCurrentPassword(String username, String currentPassword) {
        SQLiteDatabase db = this.getReadableDatabase(); // Lấy cơ sở dữ liệu có thể đọc
        // Truy vấn để lấy cột password từ bảng users với điều kiện username khớp
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_PASSWORD}, COLUMN_USERNAME + "=?",
                new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) { // Kiểm tra xem cursor có dữ liệu không
            int passwordColumnIndex = cursor.getColumnIndex(COLUMN_PASSWORD); // Lấy chỉ số của cột password
            if (passwordColumnIndex != -1) { // Nếu cột tồn tại
                String password = cursor.getString(passwordColumnIndex); // Lấy giá trị của cột password
                cursor.close(); // Đóng cursor
                return password.equals(currentPassword); // So sánh mật khẩu và trả về kết quả
            } else {
                cursor.close(); // Đóng cursor nếu cột không tồn tại
                throw new IllegalArgumentException("Column not found: " + COLUMN_PASSWORD); // Ném ra ngoại lệ nếu cột không tồn tại
            }
        }
        if (cursor != null) {
            cursor.close(); // Đóng cursor nếu không có dữ liệu
        }
        return false; // Trả về false nếu không tìm thấy mật khẩu khớp
    }
}