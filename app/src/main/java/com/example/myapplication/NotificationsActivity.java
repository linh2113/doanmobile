package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.listAdapter.NotificationAdapter;
import com.example.myapplication.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView notificationList;
    private NotificationAdapter adapter;
    private List<Notification> notifications;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        notificationList = findViewById(R.id.notificationList);
        notificationList.setLayoutManager(new LinearLayoutManager(this));

        notifications = new ArrayList<>();
        notifications.add(new Notification("12/06/2024 15:00 Cập nhật tính năng", "• Thêm các phương thức hỗ trợ khác như Zalo, Telegram.\n• Nạp FoxCoin nay đã có thể thanh toán bằng cách quét mã QR.\n• Thanh toán thủ công đã có thể sao chép thông tin thanh toán một cách dễ dàng.\n• ….."));
        notifications.add(new Notification("12/06/2024 15:00 Sửa lỗi Nạp FoxCoin", "• Chi tiết sửa lỗi..."));
        notifications.add(new Notification("12/06/2024 15:00 Cập nhật bản V0.8", "• Chi tiết cập nhật..."));
        // Thêm các thông báo khác...

        adapter = new NotificationAdapter(notifications);
        notificationList.setAdapter(adapter);
    }
}