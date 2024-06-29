package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.API.APIService;
import com.example.myapplication.model.User;
import com.google.gson.Gson;

import java.util.Currency;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestAPI extends AppCompatActivity {
    private TextView textName;
    private TextView textEmail;
    private TextView textLogin;
    private TextView textToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
//        User user = new User("aa1","Le thanh tam","hsdjh","dhfhdjs","avc@gmail.com");
//        Gson gson = new Gson();
        textName= findViewById(R.id.textViewName);
        textEmail= findViewById(R.id.textViewEmail);
        textLogin= findViewById(R.id.textViewLogin);
        textToken= findViewById(R.id.textViewToken);
        clickCallAPI();
    }

    private void clickCallAPI() {
        APIService.apiService.convertuser("zNiq9JniHDxXfJ6vviMpTvRoM6G_njDx91nojAJg6Jfa" +
                        "ZpQ2QJ1qzeB_PCLRcqXeVPrGKU95Oie8_dcYhkvZ5cLC-xsuj3jYOJmA1Yb3SEsKFZqtv3DaNY" +
                        "cMrmhZHmUMi80zadyHLKCJh4OHUK-FGAUhAHLuIij_FujABhu3lRZGDHRQo40F-rhUz_UBon5L0s9FJ" +
                        "qLsNH0s1F_orzyMrmEVPczsAM6LXVNRBEL7IMvZEPLNYASpjJ0hSJz6SyiPKmXT8QEa8Fe8mzDTCqnGIg","Mg7DcDFymf8eRs11sY3j5MxI7YPL3rfOf")
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Toast.makeText(TestAPI.this,"Call API sucess!!", Toast.LENGTH_SHORT).show();
                        User a = response.body();
                        if (a != null) {
                            textName.setText(a.getDisplayName());
                            textEmail.setText(a.getEmail());
                            textLogin.setText(a.getLoginToken());
                            textToken.setText(a.getPassword());
                        }

                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(TestAPI.this,"Call API failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}