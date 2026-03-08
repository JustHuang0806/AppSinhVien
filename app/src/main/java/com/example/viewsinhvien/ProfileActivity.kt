package com.example.appsinhvien_kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.viewsinhvien.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var editMaSV: EditText
    private lateinit var editHoTen: EditText
    private lateinit var editGioiTinh: EditText
    private lateinit var editCCCD: EditText
    private lateinit var editDanToc: EditText
    private lateinit var editQuocGia: EditText
    private lateinit var editDiDong: EditText
    private lateinit var editEmailCaNhan: EditText
    private lateinit var editTenLH: EditText
    private lateinit var editSdtLH: EditText
    private lateinit var editDcLH: EditText
    private lateinit var btnCapNhat: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        khoiTaoThanhPhan()
        thietLapSuKien()
    }

    private fun khoiTaoThanhPhan() {
        editMaSV = findViewById(R.id.edit_msv)
        editHoTen = findViewById(R.id.edit_hoten)
        editGioiTinh = findViewById(R.id.edit_gioitinh)
        editCCCD = findViewById(R.id.edit_cccd)
        editDanToc = findViewById(R.id.edit_dantoc)
        editQuocGia = findViewById(R.id.edit_quocgia)
        editDiDong = findViewById(R.id.edit_didong)
        editEmailCaNhan = findViewById(R.id.edit_email_canhan)
        editTenLH = findViewById(R.id.edit_ten_nguoi_lh)
        editSdtLH = findViewById(R.id.edit_sdt_nguoi_lh)
        editDcLH = findViewById(R.id.edit_dc_nguoi_lh)
        btnCapNhat = findViewById(R.id.btn_cap_nhat)

        // Luc moi vao tat ca se trong (Empty)
            lamTrongThongTin()
    }

    private fun lamTrongThongTin() {
        editMaSV.setText("")
        editHoTen.setText("")
        editGioiTinh.setText("")
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
        btnCapNhat.setOnClickListener {
            xuLyCapNhatDuLieu()
        }
    }

    private fun xuLyCapNhatDuLieu() {
        val maSV = editMaSV.text.toString()
        val hoTen = editHoTen.text.toString()

        if (maSV.isEmpty() || hoTen.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Mã SV và Họ tên!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Đã cập nhật thông tin cho sinh viên: $hoTen", Toast.LENGTH_LONG).show()
        }
    }
}