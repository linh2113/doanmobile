package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.API.ApiService;
import com.example.myapplication.model.User;
import com.example.myapplication.response.RegisterRespone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private SQLiteHelper sqliteHelper;
    private EditText editEmail, editPassword;
    private Button btnRegister, btnOut;
    private TextView txtGoToLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DBHelper(this);

        txtGoToLogin = (TextView) findViewById(R.id.txtGoToLogin);
        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnOK);
        btnOut = (Button) findViewById(R.id.btnOut);

        btnRegister.setOnClickListener(v -> register());
        btnOut.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        txtGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
    private void register() {
        String gmailAddress = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        User user = new User(gmailAddress, password);

        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<RegisterRespone> call = apiService.register("register", gmailAddress, password);
        call.enqueue(new Callback<RegisterRespone>() {
            @Override
            public void onResponse(Call<RegisterRespone> call, Response<RegisterRespone> response) {
                if (response.isSuccessful()) {
                    RegisterRespone registerRespone = response.body();
                    if (registerRespone != null) {
                        if (registerRespone.isStatus()) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (registerRespone.getInvalidDetail() != null) {
                                Toast.makeText(RegisterActivity.this, registerRespone.getInvalidDetail().getInvalidMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RegisterRespone> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}