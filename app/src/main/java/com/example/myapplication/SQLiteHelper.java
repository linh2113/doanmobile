package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.ContentValues;
import android.database.Cursor;
import java.io.ByteArrayOutputStream;

public class SQLiteHelper extends SQLiteOpenHelper { // Khai báo lớp SQLiteHelper kế thừa từ SQLiteOpenHelper

    // Khai báo tên cơ sở dữ liệu và phiên bản
    private static final String DATABASE_NAME = "ImageDB";
    private static final int DATABASE_VERSION = 1;

    // Khai báo tên bảng và các cột của bảng
    private static final String TABLE_NAME = "Images";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE = "image";

    // Constructor của lớp SQLiteHelper
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Images với các cột id (PRIMARY KEY AUTOINCREMENT) và image (BLOB)
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTable); // Thực thi câu lệnh tạo bảng
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng Images nếu đã tồn tại
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Tạo lại bảng
        onCreate(db);
    }

    // Phương thức để chèn hình ảnh vào cơ sở dữ liệu
    public long insertImage(Bitmap bitmap) {
        //Bitmap trong Android là
        // một lớp được sử dụng để làm việc với hình ảnh (ảnh kỹ thuật số).
        // Nó là một đối tượng đại diện cho một hình ảnh được lưu trữ trong bộ nhớ
        // và được sử dụng để thực hiện các thao tác như hiển thị, chỉnh sửa và lưu trữ hình ảnh.


        // Chuyển đổi Bitmap thành mảng byte
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();//Được sử dụng để lưu trữ dữ liệu dạng byte trong bộ nhớ.
        //Nén đối tượng Bitmap thành định dạng PNG.
        //Tham số 100 chỉ định chất lượng nén (100 là chất lượng cao nhất).
        //Kết quả nén được ghi vào byteArrayOutputStream.
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        //Chuyển nội dung của ByteArrayOutputStream thành mảng byte (byte[]).
        byte[] imageInBytes = byteArrayOutputStream.toByteArray();

        // Lấy cơ sở dữ liệu có thể ghi
        SQLiteDatabase db = this.getWritableDatabase();
        // Tạo đối tượng ContentValues để chứa các giá trị cần chèn
        ContentValues contentValues = new ContentValues();
        // Đặt giá trị cho cột image
        contentValues.put(COLUMN_IMAGE, imageInBytes);

        // Chèn dữ liệu vào bảng Images và trả về ID của hàng mới chèn
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result;
    }

    // Phương thức để lấy hình ảnh từ cơ sở dữ liệu
    public Bitmap getImage(int id) {
        // Lấy cơ sở dữ liệu có thể đọc
        SQLiteDatabase db = this.getReadableDatabase();
        // Truy vấn bảng Images để lấy cột image với điều kiện id khớp
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_IMAGE}, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) { // Kiểm tra xem cursor có dữ liệu không
            // Lấy mảng byte của hình ảnh từ cursor
            byte[] imageInBytes = cursor.getBlob(0);
            cursor.close(); // Đóng cursor
            // Chuyển đổi mảng byte thành Bitmap và trả về
            return BitmapFactory.decodeByteArray(imageInBytes, 0, imageInBytes.length);
        }
        return null; // Trả về null nếu không tìm thấy hình ảnh
    }
}

