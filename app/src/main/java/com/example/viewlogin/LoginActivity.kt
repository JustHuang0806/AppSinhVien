package com.example.viewlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.viewadmin.AdminActivity
import com.example.viewsinhvien.MainActivity
import com.example.viewsinhvien.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Phải setContentView TRƯỚC khi ánh xạ View
        setContentView(R.layout.activity_login)

        // 2. Khởi tạo Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // 3. KIỂM TRA ĐĂNG NHẬP TỰ ĐỘNG (Dùng SharedPreferences)
        val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val savedRole = sharedPref.getString("USER_ROLE", null)
        val savedUid = sharedPref.getString("USER_ID", null)

        // Nếu máy đã lưu UID (đã đăng nhập lần trước) -> Nhảy thẳng vào trong
        if (savedUid != null && savedRole != null && auth.currentUser != null) {
            when (savedRole) {
                "admin" -> {
                    startActivity(Intent(this, AdminActivity::class.java))
                    finish()
                    return // Kết thúc onCreate để không chạy code bên dưới
                }
                "sinhvien" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    return
                }
            }
        }

        // 4. Ánh xạ View (Chỉ chạy khi chưa đăng nhập)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvForgot = findViewById<TextView>(R.id.tvForgotPassword)

        tvForgot.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Thực hiện đăng nhập
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    uid?.let { currentUid ->
                        db.collection("Users").document(currentUid).get()
                            .addOnSuccessListener { document ->
                                if (document.exists()) {
                                    val role = document.getString("role")
                                    val fullName = document.getString("fullName") ?: ""
                                    val studentCode = document.getString("studentCode") ?: ""

                                    // LƯU THÔNG TIN VÀO MÁY (Shared-Pref)
                                    val editor = sharedPref.edit()
                                    editor.putString("USER_ID", currentUid)
                                    editor.putString("USER_ROLE", role)
                                    editor.putString("USER_NAME", fullName)
                                    editor.putString("USER_CODE", studentCode)
                                    editor.apply()

                                    // CHUYỂN MÀN HÌNH
                                    if (role == "admin") {
                                        startActivity(Intent(this, AdminActivity::class.java))
                                    } else if (role == "sinhvien") {
                                        startActivity(Intent(this, MainActivity::class.java))
                                    }
                                    finish()
                                } else {
                                    Toast.makeText(this, "Tài khoản không có dữ liệu!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}