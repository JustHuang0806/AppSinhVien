package com.example.adminsv

import android.content.Intent // Cần thiết để chuyển màn hình
import android.os.Bundle
import android.view.View // Dùng View để ánh xạ nút trong suốt
import androidx.appcompat.app.AppCompatActivity

class taichinh : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết với layout activity_taichinhsv.xml
        setContentView(R.layout.activity_taichinhsv)

        // 1. Ánh xạ nút mũi tên từ XML (ID là btnBackAdmin như đã sửa ở file XML trước)
        val btnBack = findViewById<View>(R.id.btnBackAdmin)

        // 2. Cài đặt sự kiện khi nhấn vào mũi tên
        btnBack.setOnClickListener {
            // Tạo hành động chuyển từ trang này (this) sang trang chủ (AdminActivity)
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)

            // Đóng trang tài chính để tiết kiệm bộ nhớ
            finish()
        }
    }
}