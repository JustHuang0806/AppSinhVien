package com.example.viewsinhvien

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.viewsinhvien.model.LopHocPhan
import com.example.viewsinhvien.model.Course
import com.example.viewsinhvien.model.SharedData

class DangKyMonHoc : AppCompatActivity() {

    private lateinit var tableMonHocMo: TableLayout
    private lateinit var tableDaDangKy: TableLayout

    // Hàm tạo danh sách lớp mẫu nhanh
    private fun generateLopMau(gvName: String) = listOf(
        LopHocPhan("Thứ 2", "1-3", "GV. $gvName"),
        LopHocPhan("Thứ 4", "7-9", "GV. $gvName (Lớp 2)"),
        LopHocPhan("Thứ 7", "10-12", "GV. $gvName (Lớp 3)")
    )

    private val tatCaMonHoc = mapOf(
        "Học kỳ 1" to listOf(
            Course("100", "Triết học Mác - Lênin", 3, generateLopMau("Nguyễn Văn A")),
            Course("101", "Tiếng Anh Cơ Bản 1", 4, generateLopMau("Trần Thị B")),
            Course("102", "Nhập môn CNTT", 2, generateLopMau("Lê Văn C"))
        ),
        "Học kỳ 4" to listOf(
            Course("120", "Lý thuyết đồ thị", 3, generateLopMau("Nguyễn Thị Phương Trang")),
            Course("122", "Lập trình thiết bị di động", 4, generateLopMau("Thái Thị Thanh Thảo"))
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dangkymonhoc_activity)

        tableMonHocMo = findViewById(R.id.table_mon_hoc_mo)
        tableDaDangKy = findViewById(R.id.table_da_dang_ky)

        hienThiDanhSachMocHoc()
        capNhatBangDaDangKy() // Hiển thị dữ liệu cũ nếu có
    }

    private fun hienThiDanhSachMocHoc() {
        for ((hocKy, dsMon) in tatCaMonHoc) {
            val rowHk = TableRow(this)
            rowHk.addView(TextView(this).apply {
                text = hocKy; setPadding(10, 30, 10, 10)
                setTextColor(Color.parseColor("#D32F2F"))
                typeface = Typeface.DEFAULT_BOLD
            })
            tableMonHocMo.addView(rowHk)

            for (mon in dsMon) {
                val row = TableRow(this).apply {
                    setPadding(5, 5, 5, 5)
                    setBackgroundResource(android.R.drawable.list_selector_background)
                    isClickable = true
                }
                row.addView(taoTextView(mon.maMon))
                row.addView(taoTextView(mon.tenMon))
                row.addView(taoTextView(mon.tinChi.toString(), true))

                row.setOnClickListener { showDialogChonLop(mon) }
                tableMonHocMo.addView(row)
            }
        }
    }

    private fun showDialogChonLop(mon: Course) {
        val options = mon.dsLop.map { "${it.thu} | Tiết ${it.tiet} | ${it.giangVien}" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Chọn lớp học phần: ${mon.tenMon}")
            .setItems(options) { _, which ->
                val lopChon = mon.dsLop[which]

                if (SharedData.dsDaDangKy.any { it.first.maMon == mon.maMon }) {
                    Toast.makeText(this, "Môn này đã có trong danh sách!", Toast.LENGTH_SHORT).show()
                } else {
                    SharedData.dsDaDangKy.add(Pair(mon, lopChon))
                    Toast.makeText(this, "Đã đăng ký: ${mon.tenMon}", Toast.LENGTH_SHORT).show()
                    capNhatBangDaDangKy()
                }
            }
            .show()
    }

    private fun capNhatBangDaDangKy() {
        val count = tableDaDangKy.childCount
        if (count > 1) tableDaDangKy.removeViews(1, count - 1)

        for (item in SharedData.dsDaDangKy) {
            val mon = item.first
            val lop = item.second
            val row = TableRow(this)
            row.addView(taoTextView(mon.maMon))
            row.addView(taoTextView("${mon.tenMon}\n(${lop.thu} - Tiết ${lop.tiet})"))
            row.addView(taoTextView(mon.tinChi.toString(), true))

            val btnXoa = Button(this).apply {
                text = "Xóa"; setTextColor(Color.WHITE); setBackgroundColor(Color.RED)
                setOnClickListener {
                    SharedData.dsDaDangKy.remove(item)
                    capNhatBangDaDangKy()
                }
            }
            row.addView(btnXoa)
            tableDaDangKy.addView(row)
        }
    }

    private fun taoTextView(text: String, isCenter: Boolean = false) = TextView(this).apply {
        this.text = text; setPadding(10, 20, 10, 20)
        if (isCenter) gravity = Gravity.CENTER
        setTextColor(Color.BLACK)
    }
}
