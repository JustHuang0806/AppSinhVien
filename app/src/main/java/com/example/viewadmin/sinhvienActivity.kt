package com.example.viewadmin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout // Cần thiết cho showAddStudentDialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.viewsinhvien.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.material.floatingactionbutton.FloatingActionButton // Cần thiết cho btnAdd

class sinhvien : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private var listData = mutableListOf<Map<String, Any>>()

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sinhvien_list)

        // Khởi tạo Firebase
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Ánh xạ RecyclerView
        recyclerView = findViewById(R.id.recyclerViewStudents)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val btnBack = findViewById<View>(R.id.btnBackAdmin)

        // Load dữ liệu lần đầu
        loadData()

        // 1. Logic Thêm Sinh Viên (Mở Dialog)
        val btnAdd = findViewById<FloatingActionButton>(R.id.btnAddStudent)
        btnAdd.setOnClickListener {
            showAddStudentDialog()
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun showAddStudentDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Thêm Sinh Viên Mới")

        // Tạo layout nhập liệu bằng code
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        container.setPadding(60, 20, 60, 0)

        val etMaSV = EditText(this).apply { hint = "Mã số sinh viên (VD: 22DH110)" }
        val etEmail = EditText(this).apply { hint = "Email sinh viên" }
        val etName = EditText(this).apply { hint = "Họ tên" }
        val etMajor = EditText(this).apply { hint = "Ngành học (VD: Công nghệ thông tin)" }
        val etLimit = EditText(this).apply {
            hint = "Giới hạn học kỳ (Ví dụ: 2)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        container.addView(etMaSV)
        container.addView(etEmail)
        container.addView(etName)
        container.addView(etMajor)
        container.addView(etLimit)
        builder.setView(container)

        builder.setPositiveButton("Tạo tài khoản") { _, _ ->
            val mssv = etMaSV.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val name = etName.text.toString().trim()
            val major = etMajor.text.toString().trim()
            val limitStr = etLimit.text.toString().trim()

            if (mssv.isNotEmpty() &&email.isNotEmpty() && name.isNotEmpty() && major.isNotEmpty() && limitStr.isNotEmpty()) {
                TaoSinhVien(email, name, limitStr.toInt(), mssv, major)
            } else {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Hủy", null)
        builder.show()
    }
    private fun showEditDialog(student: Map<String, Any>) {
        val uid = student["uid"].toString()

        // Lấy dữ liệu hiện tại từ Map để hiển thị lên các ô nhập
        val currentMSSV = student["studentCode"]?.toString() ?: ""
        val currentName = student["fullName"]?.toString() ?: ""
        val currentMajor = student["major"]?.toString() ?: ""
        val currentLimit = student["currentSemesterLimit"]?.toString() ?: "1"

        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Chỉnh sửa thông tin sinh viên")

        // Tạo Layout chứa các ô nhập
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 20, 60, 0)
        }

        // Khởi tạo các ô nhập liệu và đổ dữ liệu cũ vào
        val etMaSV = EditText(this).apply {
            hint = "Mã số sinh viên"
            setText(currentMSSV)
        }
        val etName = EditText(this).apply {
            hint = "Họ tên"
            setText(currentName)
            inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS
        }
        val etMajor = EditText(this).apply {
            hint = "Ngành học"
            setText(currentMajor)
        }
        val etLimit = EditText(this).apply {
            hint = "Giới hạn học kỳ"
            setText(currentLimit)
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }

        // Thêm các ô nhập vào Layout
        container.addView(etMaSV)
        container.addView(etName)
        container.addView(etMajor)
        container.addView(etLimit)
        builder.setView(container)

        builder.setPositiveButton("Lưu thay đổi") { _, _ ->
            val newMSSV = etMaSV.text.toString().trim()
            val newName = etName.text.toString().trim()
            val newMajor = etMajor.text.toString().trim()
            val newLimit = etLimit.text.toString().toIntOrNull() ?: 1

            if (newMSSV.isNotEmpty() && newName.isNotEmpty() && newMajor.isNotEmpty()) {
                // Tạo Map chứa các thông tin cần cập nhật
                val updateData = hashMapOf(
                    "studentCode" to newMSSV,
                    "fullName" to newName,
                    "major" to newMajor,
                    "currentSemesterLimit" to newLimit
                )

                // Cập nhật lên Firestore dùng .update() để giữ lại các trường khác (như email, role)
                db.collection("Users").document(uid)
                    .update(updateData as Map<String, Any>)
                    .addOnSuccessListener {
                        loadData() // Tải lại danh sách sau khi sửa thành công
                        Toast.makeText(this, "Đã cập nhật thông tin cho $newName", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Lỗi cập nhật: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Vui lòng không để trống thông tin!", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Hủy", null)

        val dialog = builder.create()
        dialog.show()
        // Fix lỗi gõ dấu tiếng Việt cho Dialog
        dialog.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
    private fun showDeleteConfirm(student: Map<String, Any>) {
        val uid = student["uid"].toString()
        val name = student["fullName"].toString()

        android.app.AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc chắn muốn xóa sinh viên $name?")
            .setPositiveButton("Xóa") { _, _ ->
                db.collection("Users").document(uid).delete()
                    .addOnSuccessListener {
                        loadData()
                        Toast.makeText(this, "Đã xóa sinh viên thành công", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun loadData() {
        db.collection("Users")
            .whereEqualTo("role", "sinhvien")
            .get()
            .addOnSuccessListener { documents ->
                listData.clear()
                for (doc in documents) {
                    listData.add(doc.data)
                }
                // Khởi tạo Adapter với listData
                studentAdapter = StudentAdapter(listData,
                    onItemClick = { student -> showEditDialog(student) },
                    onItemLongClick = { student -> showDeleteConfirm(student) }
                )
                recyclerView.adapter = studentAdapter
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
    }

    private fun TaoSinhVien(email: String, hoTen: String, hanDinhKy: Int, mssv: String, nganh: String) {
        val passwordMacDinh = "123456"

        auth.createUserWithEmailAndPassword(email, passwordMacDinh)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    val userMap = hashMapOf(
                        "uid" to uid,
                        "studentCode" to mssv,
                        "fullName" to hoTen,
                        "major" to nganh,
                        "email" to email,
                        "role" to "sinhvien",
                        "currentSemesterLimit" to hanDinhKy,
                        "isFirstLogin" to true
                    )

                    uid?.let {
                        db.collection("Users").document(it)
                            .set(userMap)
                            .addOnSuccessListener {
                                loadData()
                                Toast.makeText(this, "Đã tạo tài khoản cho $hoTen", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
