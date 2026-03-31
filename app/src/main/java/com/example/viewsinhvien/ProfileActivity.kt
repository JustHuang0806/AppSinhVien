package com.example.viewsinhvien

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.* // Thêm RadioButton và RadioGroup
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.content.Context
import android.view.WindowManager

class ProfileActivity : AppCompatActivity() {

    private lateinit var editMaSV: EditText
    private lateinit var editHoTen: EditText
    private lateinit var editNgaySinh: EditText
    private lateinit var rgGioiTinh: RadioGroup
    private lateinit var rbNam: RadioButton
    private lateinit var rbNu: RadioButton
    private lateinit var editNganhHoc: EditText

    private lateinit var editCCCD: EditText
    private lateinit var editDanToc: EditText
    private lateinit var editQuocGia: EditText
    private lateinit var editDiDong: EditText
    private lateinit var editEmailCaNhan: EditText
    private lateinit var editTenLH: EditText
    private lateinit var editSdtLH: EditText
    private lateinit var editDcLH: EditText
    private lateinit var btnCapNhat: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var currentStudentEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        currentStudentEmail = sharedPref.getString("USER_EMAIL", "") ?: ""

        khoiTaoThanhPhan()
        val currentUID = auth.currentUser?.uid
        if (currentUID != null) {
            taiDuLieuProfile(currentUID)
        } else {
            Toast.makeText(this, "Phiên đăng nhập hết hạn!", Toast.LENGTH_SHORT).show()
            finish()
        }
        thietLapSuKien()
    }

    private fun khoiTaoThanhPhan() {
        editMaSV = findViewById(R.id.edit_msv)
        editMaSV.isEnabled = false
        editHoTen = findViewById(R.id.edit_hoten)
        editNgaySinh = findViewById(R.id.edit_ngaysinh)

        rgGioiTinh = findViewById(R.id.rg_gioitinh)
        rbNam = findViewById(R.id.rb_nam)
        rbNu = findViewById(R.id.rb_nu)
        editNganhHoc = findViewById(R.id.edit_nganhhoc)
        editNganhHoc.isEnabled = false

        editCCCD = findViewById(R.id.edit_cccd)
        editDanToc = findViewById(R.id.edit_dantoc)
        editQuocGia = findViewById(R.id.edit_quocgia)
        editDiDong = findViewById(R.id.edit_didong)
        editEmailCaNhan = findViewById(R.id.edit_email_canhan)
        editTenLH = findViewById(R.id.edit_ten_nguoi_lh)
        editSdtLH = findViewById(R.id.edit_sdt_nguoi_lh)
        editDcLH = findViewById(R.id.edit_dc_nguoi_lh)
        btnCapNhat = findViewById(R.id.btn_cap_nhat)

        lamTrongThongTin()
    }

    private fun lamTrongThongTin() {
        editMaSV.setText("")
        editHoTen.setText("")
        editNgaySinh.setText("")
        rbNam.isChecked = true

        editCCCD.setText("")
        editDanToc.setText("")
        editQuocGia.setText("")
        editDiDong.setText("")
        editEmailCaNhan.setText("")
        editTenLH.setText("")
        editSdtLH.setText("")
        editDcLH.setText("")
    }

    private fun thietLapSuKien() {
        editNgaySinh.setOnClickListener {
            hienThiDatePicker()
        }

        btnCapNhat.setOnClickListener {
            xuLyCapNhatDuLieu()
        }
    }

    private fun hienThiDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val dateString = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            editNgaySinh.setText(dateString)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun xuLyCapNhatDuLieu() {
        val uid = auth.currentUser?.uid ?: return

        val hoTen = editHoTen.text.toString().trim()
        if (hoTen.isEmpty()) {
            editHoTen.error = "Không được để trống họ tên"
            return
        }

        val userMap = hashMapOf(
            "fullName" to editHoTen.text.toString().trim(),
            "birthday" to editNgaySinh.text.toString(),
            "gender" to if (rbNam.isChecked) "Nam" else "Nữ",
            "major" to editNganhHoc.text.toString(),
            "cccd" to editCCCD.text.toString().trim(),
            "ethnicity" to editDanToc.text.toString().trim(),
            "country" to editQuocGia.text.toString().trim(),
            "phone" to editDiDong.text.toString().trim(),
            "personalEmail" to editEmailCaNhan.text.toString().trim(),
            "contactName" to editTenLH.text.toString().trim(),
            "contactPhone" to editSdtLH.text.toString().trim(),
            "contactAddress" to editDcLH.text.toString().trim()
        )

        // Cập nhật Firestore bằng SetOptions.merge() để không mất dữ liệu Admin cấp
        db.collection("Users").document(uid)
            .set(userMap, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Lỗi cập nhật: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun taiDuLieuProfile(uid: String) {
        db.collection("Users").document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    // Đổ dữ liệu từ Firestore vào EditText
                    editMaSV.setText(doc.getString("studentCode"))
                    editHoTen.setText(doc.getString("fullName"))
                    editNgaySinh.setText(doc.getString("birthday"))

                    val gioiTinh = doc.getString("gender")
                    if (gioiTinh == "Nữ") rbNu.isChecked = true else rbNam.isChecked = true
                    editNganhHoc.setText(doc.getString("major"))

                    editCCCD.setText(doc.getString("cccd"))
                    editDanToc.setText(doc.getString("ethnicity"))
                    editQuocGia.setText(doc.getString("country"))
                    editDiDong.setText(doc.getString("phone"))
                    editEmailCaNhan.setText(doc.getString("personalEmail"))

                    editTenLH.setText(doc.getString("contactName"))
                    editSdtLH.setText(doc.getString("contactPhone"))
                    editDcLH.setText(doc.getString("contactAddress"))
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Lỗi tải dữ liệu: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
