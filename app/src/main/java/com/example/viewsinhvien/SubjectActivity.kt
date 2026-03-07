package com.example.viewsinhvien

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SubjectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_subject)

        val tv = findViewById<TextView>(R.id.tvSubject)
        tv.text = """
            Lập trình Android
            Cơ sở dữ liệu
            Lập trình Java
            Mạng máy tính
            Lập trình di động
            Mẫu thiết kế phần mềm
        """.trimIndent()
    }
}