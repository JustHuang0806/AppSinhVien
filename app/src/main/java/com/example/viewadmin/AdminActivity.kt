package com.example.viewadmin

import com.example.viewlogin.LoginActivity
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.viewsinhvien.R
import com.google.firebase.auth.FirebaseAuth

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val btnThongTin = findViewById<View>(R.id.btn_thongtin_sv)
        val btnMonHoc = findViewById<View>(R.id.btn_monhoc_sv)
        val btnTaiChinh = findViewById<View>(R.id.btn_taichinh_sv)
        val btnLogout = findViewById<LinearLayout>(R.id.btn_logout)

        btnThongTin.setOnClickListener {
            startActivity(Intent(this, sinhvien::class.java))
        }

        btnMonHoc.setOnClickListener {
            startActivity(Intent(this, monhoc::class.java))
        }

        btnTaiChinh.setOnClickListener {
            startActivity(Intent(this, taichinhsv::class.java))
        }
        
        btnLogout.setOnClickListener {
            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
