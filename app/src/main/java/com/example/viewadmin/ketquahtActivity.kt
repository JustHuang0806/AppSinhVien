package com.example.viewadmin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewsinhvien.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class ketquaht : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var studentUid: String
    private lateinit var studentName: String

    // Đối với RecyclerView hiển thị điểm tại chỗ
    private lateinit var rvMonHoc: RecyclerView
    private var listScores = mutableListOf<Map<String, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ketquaht)

        // 1. Nhận dữ liệu từ Intent
        studentUid = intent.getStringExtra("STUDENT_UID") ?: ""
        studentName = intent.getStringExtra("STUDENT_NAME") ?: "Sinh viên"

        // 2. Ánh xạ View
        val btnBack = findViewById<TextView>(R.id.btnBackHomeKQ)
        val btnSaveScore = findViewById<Button>(R.id.btnSaveScore)
        val etStudentUid = findViewById<EditText>(R.id.etStudentUid)
        val etSubject = findViewById<EditText>(R.id.etSubjectName)
        val etSemester = findViewById<EditText>(R.id.etSemester)
        val etGrade = findViewById<EditText>(R.id.etGrade)
        rvMonHoc = findViewById(R.id.rvMonHoc)
        rvMonHoc.layoutManager = LinearLayoutManager(this)

        // Hiển thị thông tin sinh viên
        etStudentUid.setText(studentUid)
        etStudentUid.isEnabled = false // Không cho sửa UID

        //val tvTitle = findViewById<TextView>(R.id.tvTitleAdminKQ) // Nếu bạn có TextView tiêu đề
        // tvTitle.text = "Điểm của $studentName"

        // 3. Load danh sách điểm hiện tại
        loadExistingScores()

        // 4. Lưu điểm
        btnSaveScore.setOnClickListener {
            val subject = etSubject.text.toString().trim()
            val semester = etSemester.text.toString().trim()
            val gradeStr = etGrade.text.toString().trim()

            if (subject.isEmpty() || gradeStr.isEmpty() || semester.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val scoreData = hashMapOf(
                "subjectName" to subject,
                "semester" to semester,
                "grade" to gradeStr.toDouble(),
                "timestamp" to Timestamp.now()
            )

            // Lưu vào: Users -> {uid} -> Scores -> {subject}
            db.collection("Users").document(studentUid)
                .collection("Scores").document(subject).set(scoreData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Đã lưu điểm môn $subject", Toast.LENGTH_SHORT).show()
                    etSubject.text.clear()
                    etGrade.text.clear()
                    loadExistingScores() // Tải lại danh sách sau khi lưu
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        btnBack.setOnClickListener { finish() }
    }

    private fun loadExistingScores() {
        db.collection("Users").document(studentUid)
            .collection("Scores")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                listScores.clear()
                for (doc in documents) {
                    listScores.add(doc.data)
                }
                val adapter = ScoreAdminAdapter(listScores)
                rvMonHoc.adapter = adapter
            }
    }
}