package com.example.viewlogin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.viewsinhvien.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    // Khai báo FirebaseAuth
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Khởi tạo Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Ánh xạ View (Hãy đảm bảo ID trong XML khớp với ở đây)
        val etEmail = findViewById<EditText>(R.id.etForgotEmail) // Kiểm tra ID này trong XML của bạn
        val btnReset = findViewById<Button>(R.id.btnResetPassword)
        val tvBack = findViewById<TextView>(R.id.tvBackToLoginForgot)

        // Nút quay lại
        tvBack.setOnClickListener {
            finish()
        }

        // Xử lý gửi Email khôi phục
        btnReset.setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                etEmail.error = "Vui lòng nhập Email đã đăng ký!"
                return@setOnClickListener
            }

            // Gọi hàm gửi email của Firebase
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Yêu cầu thành công! Vui lòng kiểm tra hộp thư đến của bạn.",
                            Toast.LENGTH_LONG
                        ).show()
                        // Sau khi gửi xong có thể tự động quay về màn hình Login
                        finish()
                    } else {
                        val error = task.exception?.message ?: "Lỗi không xác định"
                        Toast.makeText(this, "Lỗi: $error", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}