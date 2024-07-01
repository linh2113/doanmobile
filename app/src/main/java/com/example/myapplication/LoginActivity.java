package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.API.ApiClient;
import com.example.myapplication.API.ApiService;
import com.example.myapplication.model.Login;
import com.example.myapplication.response.LoginResponse;
import com.example.myapplication.response.NewLoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editMail;
    private EditText editPassword;
    private Button btnLogin, btnGoToRegister;
    Login newlogin;
    private ApiService apiService;
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        editMail = (EditText) findViewById(R.id.edit_gmail);
        editPassword = (EditText) findViewById(R.id.edit_password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnGoToRegister = (Button) findViewById(R.id.btnGoToRegister);

        btnLogin.setOnClickListener(v -> {
            String gmailAddress = editMail.getText().toString().trim();
            String token = dbHelper.getUserToken();
            if (token != null) {
                newlogin();
            } else {
                login();
            }
        });
        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
            }
        });
    }
    private void newlogin() {
        String gmailAddress = editMail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        ApiService apiService1 = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<NewLoginResponse> call = apiService1.newlogin("newlogin", gmailAddress, password);
        call.enqueue(new Callback<NewLoginResponse>() {
            @Override
            public void onResponse(Call<NewLoginResponse> call, Response<NewLoginResponse> response) {
                if (response.isSuccessful()) {
                    NewLoginResponse newLoginResponse = response.body();
                    if (newLoginResponse != null) {
                        if (newLoginResponse.isStatus()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                            dbHelper.addUser("currentDisplayName", newLoginResponse.getGmailAddress(), newLoginResponse.getToken());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("TOKEN", newLoginResponse.getToken());
                            startActivity(intent);
                        } else {
                            if (newLoginResponse.getInvalidDetail() != null) {
                                Toast.makeText(LoginActivity.this, newLoginResponse.getInvalidDetail().getInvalidMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewLoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void login(){
        String gmailAddress = editMail.getText().toString().trim();
        ApiService apiService2 = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<LoginResponse> call = apiService2.login("login", gmailAddress, "token");
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.isStatus()){
                        dbHelper.saveToken(loginResponse.getGmailAddress(), loginResponse.getToken()); Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại: " + loginResponse.getInvalidDetail().getInvalidMsg(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}