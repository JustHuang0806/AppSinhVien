package com.example.adminsv

import android.content.Intent
import android.os.Bundle
import android.widget.Button // Cần import thêm Button
import androidx.appcompat.app.AppCompatActivity

class monhoc : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết với layout activity_monhoc.xml
        setContentView(R.layout.activity_monhoc)

        // 1. Tìm nút "Quay về trang chủ" bằng ID đã đặt trong XML
        val btnBackHome = findViewById<Button>(R.id.btnBackHome)

        // 2. Thiết lập sự kiện khi người dùng bấm vào nút
        btnBackHome.setOnClickListener {
            // Tạo Intent chuyển từ trang 'monhoc' về trang 'AdminActivity'
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)

            // Đóng trang hiện tại để giải phóng bộ nhớ
            finish()
        }
    }
}