package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private TextView currentName;
    private EditText newName;
    private EditText currentPassword;
    private EditText newPassword;
    private Button btnChangeName;
    private Button btnChangePassword;
    private ImageView avatar;
    private Button btnUploadAvatar;

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
         dbHelper = new DBHelper(this);
         currentName=findViewById(R.id.currentName);
         newName=findViewById(R.id.newName);
         btnChangeName=findViewById(R.id.btnChangeName);

         currentPassword = findViewById(R.id.currentPassword);
         newPassword = findViewById(R.id.newPassword);
         btnChangePassword = findViewById(R.id.btnChangePassword);

         avatar = findViewById(R.id.avatar);
         btnUploadAvatar = findViewById(R.id.btnUploadAvatar);

        // Lấy và hiển thị username hiện tại
        String currentUsername = dbHelper.getCurrentUsername();
        if (currentUsername != null) {
            currentName.setText(currentUsername);
            // Lấy và hiển thị avatar hiện tại
            String avatarUri = dbHelper.getAvatar(currentUsername);
            if (avatarUri != null) {
                avatar.setImageURI(Uri.parse(avatarUri));
            }
        }

        // Xử lý sự kiện khi nhấn nút Change Username
        btnChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUsername = newName.getText().toString().trim();
                if (!newUsername.isEmpty()) {
                    String currentUsername = dbHelper.getCurrentUsername();
                    dbHelper.updateUsername(currentUsername, newUsername);
                    currentName.setText(newUsername);
                    currentUsername = newUsername; // Cập nhật currentUsername
                    Toast.makeText(MainActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "New username cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút Change Password
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPasswordText = currentPassword.getText().toString().trim();
                String newPasswordText = newPassword.getText().toString().trim();
                if (!currentPasswordText.isEmpty() && !newPasswordText.isEmpty()) {
                    if (dbHelper.checkCurrentPassword(currentUsername, currentPasswordText)) {
                        dbHelper.updatePassword(currentUsername, newPasswordText);
                        Toast.makeText(MainActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Passwords cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nhấn nút Upload Avatar
        btnUploadAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                } else {
                    openImagePicker();
                }
            }
        });

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            avatar.setImageURI(imageUri);
            String currentUsername = dbHelper.getCurrentUsername();
            if (currentUsername != null) {
                dbHelper.updateAvatar(currentUsername, imageUri.toString());
                Toast.makeText(this, "Avatar updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}