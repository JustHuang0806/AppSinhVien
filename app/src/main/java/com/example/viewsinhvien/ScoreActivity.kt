package com.example.viewsinhvien

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ScoreActivity : AppCompatActivity() {

    private lateinit var rvSemesters: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_score)

        rvSemesters = findViewById(R.id.rv_semesters)
        rvSemesters.layoutManager = LinearLayoutManager(this)

        loadDataFromFirebase()
    }

    private fun loadDataFromFirebase() {
        val currentUserId = auth.currentUser?.uid ?: return

        db.collection("Users").document(currentUserId)
            .collection("Scores")
            .get()
            .addOnSuccessListener { documents ->
                val semesterMap = mutableMapOf<String, MutableList<Subject>>()

                for (doc in documents) {
                    val subjectName = doc.getString("subjectName") ?: "Không tên"
                    
                    // Ưu tiên lấy trường 'semester', nếu null thì lấy 'currentSemesterLimit'
                    val semesterValue = doc.get("semester") ?: doc.get("currentSemesterLimit")
                    val semesterTitle = semesterValue?.toString() ?: "Học kỳ khác"
                    
                    val grade = when (val g = doc.get("grade")) {
                        is Double -> g
                        is Long -> g.toDouble()
                        is Number -> g.toDouble()
                        else -> 0.0
                    }
                    
                    val tinChi = doc.getLong("tinChi")?.toInt() ?: 3

                    val subject = Subject(
                        MaSinhVien = 0,
                        TenSinhVien = subjectName,
                        TinChi = tinChi,
                        Diemhe10 = grade,
                        DiemGiuaKy = 0.0,
                        DiemCuoiKy = grade
                    )

                    if (!semesterMap.containsKey(semesterTitle)) {
                        semesterMap[semesterTitle] = mutableListOf()
                    }
                    semesterMap[semesterTitle]?.add(subject)
                }

                // Sắp xếp theo tên học kỳ để hiển thị thứ tự 1, 2, 3...
                val semesterList = semesterMap.map { (title, subjects) ->
                    Semester(title, subjects, 80.0)
                }.sortedBy { it.title }

                if (semesterList.isEmpty()) {
                    Toast.makeText(this, "Bạn chưa có điểm số nào.", Toast.LENGTH_SHORT).show()
                } else {
                    rvSemesters.adapter = HocKyAdapter(semesterList) { monHocDuocChon ->
                        showScoreDetailPopup(monHocDuocChon)
                    }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Lỗi tải điểm: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showScoreDetailPopup(subject: Subject) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.popup_bang_diem)

        val tvName = dialog.findViewById<TextView>(R.id.tv_detail_subject_name)
        val tvFinal = dialog.findViewById<TextView>(R.id.tv_final_score)
        val tvMid = dialog.findViewById<TextView>(R.id.tv_midterm_score)
        val tvDiemHe4 = dialog.findViewById<TextView>(R.id.tv_detail_grade_4)

        tvName.text = subject.TenSinhVien
        tvFinal.text = subject.DiemCuoiKy.toString()
        tvMid.text = subject.DiemGiuaKy.toString()
        tvDiemHe4?.text = "Điểm hệ 4: ${subject.Diemhe4}"

        dialog.show()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.CENTER)
        }
    }
}
