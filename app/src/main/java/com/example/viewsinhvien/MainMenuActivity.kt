package com.example.viewsinhvien

import com.example.viewlogin.LoginActivity
import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Ánh xạ các View
        val btnMenu = findViewById<MaterialButton>(R.id.btnMainMenu)
        val menuArea = findViewById<LinearLayout>(R.id.expandableMenu)
        val schoolDesc = findViewById<View>(R.id.schoolDescription)
        val rootLayout = findViewById<LinearLayout>(R.id.headerLayout).parent as ViewGroup
        val btnLogout = findViewById<MaterialButton>(R.id.btnLogout)

        // 1. NÚT ĐĂNG KÝ MÔN HỌC (Mới thêm)
        findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener {
            startActivity(Intent(this, DangKyMonHoc::class.java))
        }

        btnMenu.setOnClickListener {
            TransitionManager.beginDelayedTransition(rootLayout)

            if (menuArea.visibility == View.VISIBLE) {
                menuArea.visibility = View.GONE
                schoolDesc.visibility = View.VISIBLE
                btnMenu.setIconResource(android.R.drawable.ic_menu_revert)
            } else {
                menuArea.visibility = View.VISIBLE
                schoolDesc.visibility = View.GONE
                btnMenu.setIconResource(android.R.drawable.ic_menu_close_clear_cancel)
            }
        }

        // 3. Chuyển trang cho các nút chức năng khác
        findViewById<MaterialButton>(R.id.btnSubject).setOnClickListener {
            startActivity(Intent(this, SubjectActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnSchedule).setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnEconomy).setOnClickListener {
            startActivity(Intent(this, EconomyActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnScore).setOnClickListener {
            startActivity(Intent(this, ScoreActivity::class.java))
        }

        findViewById<MaterialButton>(R.id.btnProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // 4. Xử lý Đăng xuất
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
            sharedPref.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}