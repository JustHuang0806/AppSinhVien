package com.example.adminsv

import android.content.Intent // Để chuyển màn hình
import android.os.Bundle
import android.view.View // Để ánh xạ nút trong suốt
import androidx.appcompat.app.AppCompatActivity

class sinhvien : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Liên kết với layout activity_sinhvien_list.xml
        setContentView(R.layout.activity_sinhvien_list)

        // 1. Ánh xạ nút mũi tên quay về (ID: btnBackAdmin)
        val btnBack = findViewById<View>(R.id.btnBackAdmin)

        // 2. Thiết lập sự kiện Click cho nút quay về
        btnBack.setOnClickListener {
            // Chuyển hướng về trang chủ AdminActivity
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)

            // Đóng trang danh sách sinh viên để tối ưu bộ nhớ
            finish()
        }

        // --- Bạn có thể thêm code xử lý RecyclerView hoặc FloatingActionButton ở dưới này ---
    }
}