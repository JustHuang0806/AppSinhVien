package com.example.viewsinhvien

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import android.view.LayoutInflater
import com.google.firebase.auth.EmailAuthProvider

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
    private lateinit var btnDoiMatKhau: Button
    
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

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
        btnDoiMatKhau = findViewById(R.id.btn_doi_mat_khau)
    }

    private fun thietLapSuKien() {
        editNgaySinh.setOnClickListener { hienThiDatePicker() }
        btnCapNhat.setOnClickListener { xuLyCapNhatDuLieu() }
        btnDoiMatKhau.setOnClickListener { hienThiDialogDoiMatKhau() }
    }

    private fun hienThiDialogDoiMatKhau() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Đổi mật khẩu")

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(60, 20, 60, 0)
        }

        val etOldPass = EditText(this).apply { hint = "Mật khẩu cũ"; inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD }
        val etNewPass = EditText(this).apply { hint = "Mật khẩu mới"; inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD }
        val etConfirmPass = EditText(this).apply { hint = "Xác nhận mật khẩu mới"; inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD }

        layout.addView(etOldPass); layout.addView(etNewPass); layout.addView(etConfirmPass)
        builder.setView(layout)

        builder.setPositiveButton("Đổi mật khẩu") { _, _ ->
            val oldPass = etOldPass.text.toString()
            val newPass = etNewPass.text.toString()
            val confirmPass = etConfirmPass.text.toString()

            if (newPass != confirmPass) {
                Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }
            if (newPass.length < 6) {
                Toast.makeText(this, "Mật khẩu phải từ 6 ký tự!", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            xuLyDoiMatKhauFirebase(oldPass, newPass)
        }
        builder.setNegativeButton("Hủy", null)
        builder.show()
    }

    private fun xuLyDoiMatKhauFirebase(oldPass: String, newPass: String) {
        val user = auth.currentUser
        val email = user?.email ?: return

        // Bước 1: Xác thực lại người dùng bằng mật khẩu cũ
        val credential = EmailAuthProvider.getCredential(email, oldPass)
        user.reauthenticate(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Bước 2: Đổi mật khẩu mới
                user.updatePassword(newPass).addOnCompleteListener { updateTask ->
                    if (updateTask.isSuccessful) {
                        Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Lỗi: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Mật khẩu cũ không chính xác!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hienThiDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, y, m, d ->
            editNgaySinh.setText("$d/${m + 1}/$y")
        }, year, month, day).show()
    }

    private fun xuLyCapNhatDuLieu() {
        val uid = auth.currentUser?.uid ?: return
        val userMap = hashMapOf(
            "fullName" to editHoTen.text.toString().trim(),
            "birthday" to editNgaySinh.text.toString(),
            "gender" to if (rbNam.isChecked) "Nam" else "Nữ",
            "cccd" to editCCCD.text.toString().trim(),
            "ethnicity" to editDanToc.text.toString().trim(),
            "country" to editQuocGia.text.toString().trim(),
            "phone" to editDiDong.text.toString().trim(),
            "personalEmail" to editEmailCaNhan.text.toString().trim(),
            "contactName" to editTenLH.text.toString().trim(),
            "contactPhone" to editSdtLH.text.toString().trim(),
            "contactAddress" to editDcLH.text.toString().trim()
        )

        db.collection("Users").document(uid).set(userMap, SetOptions.merge())
            .addOnSuccessListener { Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show() }
    }

    private fun taiDuLieuProfile(uid: String) {
        db.collection("Users").document(uid).get().addOnSuccessListener { doc ->
            if (doc != null && doc.exists()) {
                editMaSV.setText(doc.getString("studentCode"))
                editHoTen.setText(doc.getString("fullName"))
                editNgaySinh.setText(doc.getString("birthday"))
                if (doc.getString("gender") == "Nữ") rbNu.isChecked = true else rbNam.isChecked = true
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
    }
}
