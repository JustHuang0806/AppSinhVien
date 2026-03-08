package com.example.adminsv

import android.content.Intent // Import để chuyển trang
import android.os.Bundle
import android.view.View // Import View chung
import androidx.appcompat.app.AppCompatActivity

class tintucsv : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết với layout activity_tintucsv.xml
        setContentView(R.layout.activity_tintucsv)

        // 1. Ánh xạ nút mũi tên bằng ID đã đặt trong XML
        val btnBack = findViewById<View>(R.id.btnBackAdmin)

        // 2. Thiết lập sự kiện Click
        btnBack.setOnClickListener {
            // Tạo lệnh chuyển từ trang hiện tại (this) sang trang chủ (AdminActivity)
            val intent = Intent(this, AdminActivity::class.java)

            // Thực hiện chuyển trang
            startActivity(intent)

            // Kết thúc trang hiện tại để khi bấm nút Back của máy không bị quay lại đây
            finish()
        }
    }
}