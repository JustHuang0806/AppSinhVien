package com.example.viewsinhvien

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScoreActivity : AppCompatActivity() {

    private lateinit var rvSemesters: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        rvSemesters = findViewById(R.id.rv_semesters)
        rvSemesters.layoutManager = LinearLayoutManager(this)

        loadDataFromFirebase()
    }

    private fun loadDataFromFirebase() {
        val mockData = listOf(
            Semester(
                "Năm học: 2022-2023 - Học kỳ: HK01", listOf(
                    Subject(1010443, "Triết học Mác - Lênin", 3, 6.1, 5.5, 6.5),
                    Subject(1221163, "Toán rời rạc", 3, 3.2, 2.0, 4.0)
                ), 82.0
            )
        )
        rvSemesters.adapter = HocKyAdapter(mockData) { monHocDuocChon ->
            showScoreDetailPopup(monHocDuocChon)
        }
    }

    private fun showScoreDetailPopup(subject: Subject) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)


        dialog.setContentView(R.layout.popup_bang_diem)

        val tvName = dialog.findViewById<TextView>(R.id.tv_detail_subject_name)
        val tvFinal = dialog.findViewById<TextView>(R.id.tv_final_score)
        val tvMid = dialog.findViewById<TextView>(R.id.tv_midterm_score)

        tvName.text = subject.TenSinhVien
        tvFinal.text = subject.DiemCuoiKy.toString()
        tvMid.text = subject.DiemGiuaKy.toString()

        dialog.show()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
    }
}

// CHUYỂN 2 CLASS NÀY RA NGOÀI CLASS SCOREACTIVITY ĐỂ HẾT LỖI UNRESOLVED REFERENCE
data class Subject(
    var MaSinhVien: Int = 0,
    val TenSinhVien: String,
    val TinChi: Int,
    val Diemhe10: Double,
    val DiemGiuaKy: Double,
    val DiemCuoiKy: Double
) {
    val Diemhe4: Double get() = if (Diemhe10 >= 8.5) 4.0 else if (Diemhe10 >= 7.0) 3.0 else if (Diemhe10 >= 5.5) 2.5 else if (Diemhe10 >= 5.0) 2.0 else if (Diemhe10 >= 4.0) 1.0 else 0.0
    val DiemChu: String get() = if (Diemhe10 >= 8.5) "A" else if (Diemhe10 >= 7.0) "B" else if (Diemhe10 >= 5.5) "C+" else if (Diemhe10 >= 5.0) "C" else if (Diemhe10 >= 4.0) "D" else "F"
    val isPassed: Boolean get() = Diemhe10 >= 4.0
}

data class Semester(
    val title: String,
    val subjects: List<Subject>,
    val trainingScore: Double
)
