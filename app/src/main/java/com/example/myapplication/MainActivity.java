package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.API.ApiService;
import com.example.myapplication.API.RetrofitClient;
import com.example.myapplication.model.ChangePasswordResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private TextView currentName;
    private EditText newName;
    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private Button btnChangeName;
    private Button btnChangePassword;
    private String currentUsername;
    private ImageView avatar;
    private Button btnUploadAvatar;
    private ImageView btnRouter;

    //mã định danh độc lập cho việc yêu cầu người dùng
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private SQLiteHelper sqliteHelper;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String IMAGE_ID_KEY = "imageId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRouter=findViewById(R.id.btnRouter);
        btnRouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để chuyển từ MainActivity sang MainActivity2
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);

                // Lấy currentName
                String currentNameText = currentName.getText().toString();
                intent.putExtra("currentName", currentNameText);

                // Lấy avatar
                avatar.setDrawingCacheEnabled(true);
                Bitmap bitmap = avatar.getDrawingCache();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                intent.putExtra("avatar", byteArray);

                // Bắt đầu activity mới (MainActivity2)
                startActivity(intent);
            }
        });



        dbHelper = new DBHelper(this);
        currentName=findViewById(R.id.currentName);
        newName=findViewById(R.id.newName);
        btnChangeName=findViewById(R.id.btnChangeName);

        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmNewPassword=findViewById(R.id.confirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        avatar = findViewById(R.id.avatar);
        btnUploadAvatar = findViewById(R.id.btnUploadAvatar);

        // Lấy và hiển thị username hiện tại
        currentUsername = dbHelper.getCurrentUsername();
        if (currentUsername != null) {
            currentName.setText(currentUsername);

        }


        // Xử lý sự kiện khi nhấn nút Change Username
        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = newName.getText().toString().trim();
                if (!newUsername.isEmpty()) {
                    if(newUsername.equals(currentUsername)){
                        Toast.makeText(MainActivity.this, "Tên mới không được trùng với tên hiện tại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    dbHelper.updateUsername(currentUsername, newUsername);
                    currentName.setText(newUsername);
                    currentUsername = newUsername; // Cập nhật currentUsername
                    Toast.makeText(MainActivity.this, "Đổi tên người dùng thành công", Toast.LENGTH_SHORT).show();
                    newName.setText("");
                } else {
                    Toast.makeText(MainActivity.this, "Tên mới không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút Change Password
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentPasswordText = currentPassword.getText().toString().trim();
                String newPasswordText = newPassword.getText().toString().trim();
                String confirmNewPasswordText = confirmNewPassword.getText().toString().trim();

                if (!currentPasswordText.isEmpty() && !newPasswordText.isEmpty() && !confirmNewPasswordText.isEmpty()) {
                    if(currentPasswordText.equals(newPasswordText)){
                        Toast.makeText(MainActivity.this, "Mật khẩu mới không được trùng với mật khẩu cũ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!newPasswordText.equals(confirmNewPasswordText)) {
                        Toast.makeText(MainActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    changePassword(currentPasswordText, newPasswordText);
                } else {
                    Toast.makeText(MainActivity.this, "Các trường mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                }
            }
        });

        sqliteHelper = new SQLiteHelper(this);

        btnUploadAvatar.setOnClickListener(v -> {
            // Kiểm tra xem ứng dụng có quyền đọc dữ liệu từ bộ nhớ ngoại không
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Nếu có quyền, mở giao diện để người dùng chọn hình ảnh từ thư viện ảnh của thiết bị
                pickImageFromGallery();
            } else {
                // Nếu không có quyền, yêu cầu cấp quyền từ người dùng
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        });

        loadSavedImage();
    }

    private void changePassword(String oldPassword, String newPassword) {
        Retrofit retrofit = RetrofitClient.getClient("https://script.google.com/macros/s/AKfycbxJpmPDl_ySg22T06AHwj62EuYM5Yz_S26j7efRJxliDl2S5klNX-vx1w6PHhhxG3VnfA/");
        ApiService apiService = retrofit.create(ApiService.class);

        Call<ChangePasswordResponse> call = apiService.changePassword(
                "updatepw",
                "tinyfox@gmail.com",
                "b37e3a99c33e8b21f4cfe6175f91ad0ecf06d87b",
                oldPassword,
                newPassword
        );

        call.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                if (response.isSuccessful()) {
                    ChangePasswordResponse changePasswordResponse = response.body();
                    if (changePasswordResponse != null && changePasswordResponse.getStatus()) {
                        Toast.makeText(MainActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Response not successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Phương thức để tải ảnh đã lưu
    private void loadSavedImage() {
        // Lấy SharedPreferences để truy cập dữ liệu đã lưu trữ
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // Lấy imageId từ SharedPreferences, nếu không tìm thấy sẽ trả về -1
        long imageId = sharedPreferences.getLong(IMAGE_ID_KEY, -1);
        // Kiểm tra xem imageId có hợp lệ không
        if (imageId != -1) {
            // Nếu có, lấy bitmap từ cơ sở dữ liệu với imageId đã lưu
            Bitmap bitmap = sqliteHelper.getImage((int) imageId);
            // Kiểm tra xem bitmap có tồn tại không
            if (bitmap != null) {
                // Nếu có, hiển thị bitmap trên ImageView
                avatar.setImageBitmap(bitmap);
            } else {
                // Nếu không, hiển thị thông báo "Không tìm thấy ảnh"
                Toast.makeText(MainActivity.this, "No Image Found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Phương thức để chọn ảnh từ thư viện ảnh của thiết bị
    private void pickImageFromGallery() {
        // Tạo Intent để mở thư viện ảnh
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Bắt đầu Activity để chọn ảnh và chờ kết quả
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Xử lý kết quả từ việc yêu cầu cấp quyền
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Kiểm tra xem mã yêu cầu có phải là STORAGE_PERMISSION_CODE không
        if (requestCode == STORAGE_PERMISSION_CODE) {
            // Kiểm tra xem quyền đã được cấp hay không
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu đã được cấp quyền, tiến hành chọn ảnh từ thư viện ảnh
                pickImageFromGallery();
            } else {
                // Nếu quyền bị từ chối, hiển thị thông báo "Quyền bị từ chối"
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Xử lý kết quả sau khi chọn ảnh từ thư viện ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Kiểm tra xem requestCode có phải là PICK_IMAGE_REQUEST và kết quả có thành công không
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Lấy URI của ảnh đã chọn
            Uri imageUri = data.getData();
            try {
                // Chuyển đổi URI thành Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                // Hiển thị Bitmap trên ImageView
                avatar.setImageBitmap(bitmap);

                // Lưu ảnh vào cơ sở dữ liệu và lấy imageId của ảnh đã lưu
                long imageId = sqliteHelper.insertImage(bitmap);
                // Kiểm tra xem imageId có hợp lệ không
                if (imageId != -1) {
                    // Nếu hợp lệ, hiển thị thông báo "Tải ảnh lên thành công"
                    Toast.makeText(this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    // Lưu imageId vào SharedPreferences
                    saveImageIdToSharedPreferences(imageId);
                } else {
                    // Nếu không hợp lệ, hiển thị thông báo "Không thể tải ảnh lên"
                    Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Phương thức để lưu imageId vào SharedPreferences
    private void saveImageIdToSharedPreferences(long imageId) {
        // Lấy SharedPreferences để truy cập dữ liệu đã lưu trữ
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        // Lấy Editor để chỉnh sửa dữ liệu SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Đặt giá trị imageId vào SharedPreferences
        editor.putLong(IMAGE_ID_KEY, imageId);
        // Áp dụng các thay đổi vào SharedPreferences
        editor.apply();
        // Truy xuất dữ liệu từ SharedPreferences
//        String username = sharedPreferences.getString("username", "defaultUser");
    }


}