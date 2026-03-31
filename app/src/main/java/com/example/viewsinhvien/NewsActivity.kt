package com.example.viewsinhvien

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.viewsinhvien.R

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val bangTinTuc = findViewById<TableLayout>(R.id.bang_tin_tuc)

        // Noi dung mo phong y het trong anh
        themHangMoi(bangTinTuc, "Hóa đơn điện tử ngày : 2025-12-29 15:53:57.", "KHTC", "29/12/2025")
        themHangMoi(bangTinTuc, "SINH VIÊN CHẤM ĐIỂM RÈN LUYỆN NĂM HỌC 2025-2026-HK01 THÀNH CÔNG", "22DH114533", "27/12/2025 01:09:00")
        themHangMoi(bangTinTuc, "NHẮC NHỞ SINH VIÊN CHƯA THỰC HIỆN ĐÁNH GIÁ RÈN LUYỆN NĂM HỌC 2025-2026 HỌC KỲ HK01", "Phòng CT-CTSV", "26/12/2025")
        themHangMoi(bangTinTuc, "Hóa đơn điện tử ngày : 30/09/2025 1:47:18 PM", "KHTC", "30/09/2025")
        themHangMoi(bangTinTuc, "Hóa đơn điện tử ngày : 09/09/2025 4:37:15 PM", "KHTC", "09/09/2025")
        themHangMoi(bangTinTuc, "YÊU CẦU ĐĂNG KÝ THỦ TỤC TRỰC TUYẾN ĐÃ XỬ LÝ THÀNH CÔNG", "22DH114533", "05/09/2025 09:33:48")
        themHangMoi(bangTinTuc, "SINH VIÊN CHẤM ĐIỂM RÈN LUYỆN NĂM HỌC 2024-2025-HK03 THÀNH CÔNG", "22DH114533", "22/07/2025 22:13:00")
        themHangMoi(bangTinTuc, "CƠ HỘI THỰC TẬP CÙNG HIW25!", "Nguyễn Phước Đại", "01/07/2025")

        // tạo thêm bảng tin nếu muốn (nhưng dành cho admin)
    }

    private fun themHangMoi(table: TableLayout, tieuDe: String, nguoiGui: String, thoiGian: String) {
        val row = TableRow(this)
        val lp = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
        row.layoutParams = lp
        row.setPadding(0, 1, 0, 1)
        row.setBackgroundColor(Color.parseColor("#CCCCCC"))

        val paramsTieuDe = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 3f)
        paramsTieuDe.setMargins(1, 1, 1, 1)
        val txtTieuDe = TextView(this)
        txtTieuDe.text = tieuDe
        txtTieuDe.setTextColor(Color.parseColor("#1A73E8"))
        txtTieuDe.setPadding(15, 20, 15, 20)
        txtTieuDe.setBackgroundColor(Color.WHITE)

        val paramsNguoiGui = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
        paramsNguoiGui.setMargins(0, 1, 1, 1)
        val txtNguoiGui = TextView(this)
        txtNguoiGui.text = nguoiGui
        txtNguoiGui.gravity = Gravity.CENTER
        txtNguoiGui.setPadding(10, 20, 10, 20)
        txtNguoiGui.setBackgroundColor(Color.WHITE)

        val paramsThoiGian = TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1f)
        paramsThoiGian.setMargins(0, 1, 1, 1)
        val txtThoiGian = TextView(this)
        txtThoiGian.text = thoiGian
        txtThoiGian.gravity = Gravity.CENTER
        txtThoiGian.setPadding(10, 20, 10, 20)
        txtThoiGian.setBackgroundColor(Color.WHITE)

        row.addView(txtTieuDe, paramsTieuDe)
        row.addView(txtNguoiGui, paramsNguoiGui)
        row.addView(txtThoiGian, paramsThoiGian)

        table.addView(row)
    }
}