package com.example.adminsv

import android.content.Intent
import android.os.Bundle
import android.widget.Button // Đừng quên import Button
import androidx.appcompat.app.AppCompatActivity

class ketquaht : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết với layout activity_ketquaht.xml
        setContentView(R.layout.activity_ketquaht)

        // 1. Ánh xạ nút bấm từ XML (ID phải khớp với btnBackHomeKQ)
        val btnBack = findViewById<Button>(R.id.btnBackHomeKQ)

        // 2. Thiết lập sự kiện click
        btnBack.setOnClickListener {
            // Chuyển từ trang hiện tại quay lại AdminActivity (Trang chủ)
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)

            // Đóng trang kết quả học tập để giải phóng bộ nhớ
            finish()
        }
    }
}