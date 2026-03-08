package com.example.adminsv

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val btnThongTin = findViewById<View>(R.id.btn_thongtin_sv)
        val btnMonHoc = findViewById<View>(R.id.btn_monhoc_sv)
        val btnKetQua = findViewById<View>(R.id.btn_ketqua_ht)
        val btnTaiChinh = findViewById<View>(R.id.btn_taichinh_sv)
        val btnTinTuc = findViewById<View>(R.id.btn_tintuc_sv)

        btnThongTin.setOnClickListener {
            startActivity(Intent(this, sinhvien::class.java))
        }

        btnMonHoc.setOnClickListener {
            startActivity(Intent(this, monhoc::class.java))
        }

        btnKetQua.setOnClickListener {
            startActivity(Intent(this, ketquaht::class.java))
        }

        btnTaiChinh.setOnClickListener {
            startActivity(Intent(this, taichinh::class.java))
        }

        btnTinTuc.setOnClickListener {
            startActivity(Intent(this, tintucsv::class.java))
        }
    }
}