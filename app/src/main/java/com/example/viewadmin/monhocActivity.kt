package com.example.viewadmin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewsinhvien.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class monhoc : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monhoc)

        db = FirebaseFirestore.getInstance()
        loadSubjectsList()
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAddSubject)
        btnAdd.setOnClickListener {
            showAddSubjectDialog() // Hàm hiện Dialog nhập liệu đã viết ở trên
        }
        val btnBackHome = findViewById<Button>(R.id.btnBackAdmin)
        btnBackHome.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)

            finish()
        }
    }
    private fun showAddSubjectDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Cấu hình Môn học mới")

        // Tạo Layout chứa các ô nhập
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 30, 60, 0)
        }
        // Các ô nhập liệu thông minh
        val etCode = EditText(this).apply { hint = "Mã môn (VD: AND101)" }
        val etName = EditText(this).apply { hint = "Tên môn học"
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS}
        val etCredits = EditText(this).apply {
            hint = "Số tín chỉ"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val etSemester = EditText(this).apply {
            hint = "Thuộc học kỳ (1-8)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        // Checkbox cho môn bắt buộc
        val cbRequired = android.widget.CheckBox(this).apply {
            text = "Môn học bắt buộc"
            isChecked = true
        }

        layout.addView(etCode)
        layout.addView(etName)
        layout.addView(etCredits)
        layout.addView(etSemester)
        layout.addView(cbRequired)
        builder.setView(layout)

        builder.setPositiveButton("Lưu vào Hệ thống") { _, _ ->
            val code = etCode.text.toString().trim()
            val name = etName.text.toString().trim()
            val cre = etCredits.text.toString().toIntOrNull() ?: 3
            val sem = etSemester.text.toString().toIntOrNull() ?: 1
            val req = cbRequired.isChecked

            if (code.isNotEmpty() && name.isNotEmpty()) {
                saveSubjectToFirestore(code, name, cre, sem, req)
            }
        }
        builder.setNegativeButton("Hủy", null)
        val dialog = builder.create()
        dialog.show()

// Fix lỗi gõ dấu bằng cách ép Dialog nhận diện input tốt hơn
        dialog.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        dialog.window?.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
    private fun saveSubjectToFirestore(code: String, name: String, cre: Int, sem: Int, req: Boolean) {
        val subjectMap = hashMapOf(
            "subjectCode" to code,
            "subjectName" to name,
            "credits" to cre,        // Kiểu Number
            "semester" to sem,       // Kiểu Number (Dùng để lọc cho SV)
            "isRequired" to req      // Kiểu Boolean
        )

        db.collection("All_Subjects")
            .document(code) // Dùng Mã môn làm ID luôn cho dễ quản lý
            .set(subjectMap)
            .addOnSuccessListener {
                Toast.makeText(this, "Đã lưu môn $name thành công!", Toast.LENGTH_SHORT).show()
                // Sau khi lưu xong, danh sách ở màn hình Admin sẽ tự load lại
                loadSubjectsList()
            }
    }
    private fun loadSubjectsList() {
        db.collection("All_Subjects")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val listData = mutableListOf<Map<String, Any>>()
                for (doc in querySnapshot) {
                    listData.add(doc.data)
                }

                // Gán vào RecyclerView
                val adapter = SubjectAdapter(listData)
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewSubjects)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = adapter

                // Thông báo nếu danh sách trống
                if (listData.isEmpty()) {
                    Toast.makeText(this, "Chưa có môn học nào!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Lỗi tải môn học: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
