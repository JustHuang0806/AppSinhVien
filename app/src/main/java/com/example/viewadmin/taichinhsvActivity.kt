package com.example.viewadmin

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewsinhvien.R
import com.google.firebase.firestore.FirebaseFirestore

class taichinhsv : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var rvStudent: RecyclerView
    private var listSV = mutableListOf<Map<String, Any>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_taichinhsv)

        findViewById<android.view.View>(R.id.btnBackAdmin).setOnClickListener { finish() }

        rvStudent = findViewById(R.id.rvStudentFinance)
        rvStudent.layoutManager = LinearLayoutManager(this)

        loadDanhSachSinhVien()
    }

    private fun loadDanhSachSinhVien() {
        db.collection("Users").whereEqualTo("role", "sinhvien").get()
            .addOnSuccessListener { docs ->
                listSV.clear()
                for (doc in docs) {
                    val data = doc.data.toMutableMap()
                    data["uid"] = doc.id
                    listSV.add(data)
                }
                
                // Khởi tạo Adapter với mode Tài chính
                val adapter = StudentAdapter(
                    studentList = listSV,
                    onItemClick = { sv -> showNhapHocPhiDialog(sv) },
                    onItemLongClick = {},
                    onScoreClick = {},
                    onFinanceClick = { sv -> showNhapHocPhiDialog(sv) },
                    showFinanceOnly = true // Ẩn nút Điểm, hiện nút Tài chính
                )
                rvStudent.adapter = adapter
            }
    }

    private fun showNhapHocPhiDialog(sv: Map<String, Any>) {
        val uid = sv["uid"].toString()
        val name = sv["fullName"].toString()

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Nhập học phí: $name")

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 20, 60, 0)
        }

        val etHK = EditText(this).apply { hint = "Học kỳ (VD: 1)" }
        val etDaDong = EditText(this).apply { hint = "Số tiền đã đóng"; inputType = android.text.InputType.TYPE_CLASS_NUMBER }

        layout.addView(etHK)
        layout.addView(etDaDong)
        builder.setView(layout)

        builder.setPositiveButton("Lưu") { _, _ ->
            val hk = etHK.text.toString()
            val da = etDaDong.text.toString().toLongOrNull() ?: 0L

            val data = hashMapOf(
                "semester" to hk,
                "paid" to da,
                "updateAt" to com.google.firebase.Timestamp.now()
            )

            db.collection("Users").document(uid).collection("Economy").document("HK$hk")
                .set(data)
                .addOnSuccessListener { 
                    Toast.makeText(this, "Đã cập nhật học phí học kỳ $hk cho $name", Toast.LENGTH_SHORT).show() 
                }
        }
        builder.setNegativeButton("Hủy", null)
        builder.show()
    }
}
