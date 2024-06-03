package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private DBHelper dbHelper;
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


    }

}