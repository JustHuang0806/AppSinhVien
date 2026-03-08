package com.example.viewsinhvien

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ScheduleActivity : AppCompatActivity() {

    private lateinit var spnNamHoc: Spinner
    private lateinit var spnHocKy: Spinner
    private lateinit var spnTuan: Spinner
    private lateinit var bangTKB: TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        anhXaThanhPhan()
        thietLapDuLieuLoc()
    }

    private fun anhXaThanhPhan() {
        spnNamHoc = findViewById(R.id.spn_nam_hoc)
        spnHocKy = findViewById(R.id.spn_hoc_ky)
        spnTuan = findViewById(R.id.spn_tuan)
        bangTKB = findViewById(R.id.bang_thoi_khoa_bieu)
    }

    private fun thietLapDuLieuLoc() {
        val dsNamHoc = listOf("2022-2023", "2023-2024", "2024-2025", "2025-2026")
        val adpNam = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dsNamHoc)
        spnNamHoc.adapter = adpNam
        spnNamHoc.setSelection(3) // Mặc định 2025-2026

        val dsHocKy = listOf("Học kỳ 1", "Học kỳ 2", "Học kỳ 3")
        val adpHocKy = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dsHocKy)
        spnHocKy.adapter = adpHocKy

        // Nếu chọn học kì 1 sẽ bắt đầu từ tuần 1, hc kì 2 từ tuần 18 và học kì 3 từ tuần 37
        spnHocKy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                capNhatDanhSachTuan(position + 1)
                capNhatBangTheoLoc()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun capNhatDanhSachTuan(hocKy: Int) {
        val dsTuan = mutableListOf<Int>()
        when (hocKy) {
            1 -> for (i in 1..12) dsTuan.add(i)
            2 -> for (i in 18..31) dsTuan.add(i)
            3 -> for (i in 37..48) dsTuan.add(i)
        }
        val adpTuan = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dsTuan)
        spnTuan.adapter = adpTuan
    }

    private fun capNhatBangTheoLoc() {
        val namHoc = spnNamHoc.selectedItem.toString()
        val count = bangTKB.childCount
        if (count > 1) {
            bangTKB.removeViews(1, count - 1)
        }

        // NOTE LAI TUNG NAM DE THEM MON O DAY
        when (namHoc) {
            "2022-2023" -> {  }
            "2023-2024" -> {  }
            "2024-2025" -> {  }
            "2025-2026" -> {
                themHangVaoBang("1", "252121015460", "Thuật toán đồ thị", "4", "Hai", "7-9", "PM20", "Nguyễn Thị Phương Trang")
                themHangVaoBang("2", "252123038402", "Lập trình trên thiết bị di động", "4", "Hai", "10-12", "B46", "Thái Thị Thanh Thảo")
            }
        }
    }

    private fun themHangVaoBang(stt: String, maLop: String, tenHP: String, stc: String, thu: String, tiet: String, phong: String, giaoVien: String) {
        val row = TableRow(this)
        row.setBackgroundColor(Color.WHITE)

        val fields = listOf(stt, maLop, tenHP, stc, thu, tiet, phong, giaoVien)
        for (text in fields) {
            val tv = TextView(this)
            tv.text = text
            tv.setPadding(15, 20, 15, 20)
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.BLACK)
            tv.setBackgroundResource(android.R.drawable.divider_horizontal_bright)
            row.addView(tv)
        }
        bangTKB.addView(row)
    }
}