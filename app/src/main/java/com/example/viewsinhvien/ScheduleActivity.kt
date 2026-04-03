package com.example.viewsinhvien

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.viewsinhvien.model.Course
import com.example.viewsinhvien.model.SharedData

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
        spnNamHoc.setSelection(3)

        val dsHocKy = listOf("Học kỳ 1", "Học kỳ 2", "Học kỳ 3")
        val adpHocKy = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dsHocKy)
        spnHocKy.adapter = adpHocKy

        val commonListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (p0 == spnHocKy) {
                    capNhatDanhSachTuan(position + 1)
                }
                capNhatBangTheoLoc()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spnNamHoc.onItemSelectedListener = commonListener
        spnHocKy.onItemSelectedListener = commonListener
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

        if (namHoc == "2025-2026") {
            SharedData.dsDaDangKy.forEachIndexed { index, pair ->
                val mon = pair.first
                val lop = pair.second

                themHangVaoBang(
                    (index + 1).toString(),
                    mon.maMon,
                    mon.tenMon,
                    mon.tinChi.toString(),
                    lop.thu,
                    lop.tiet,
                    "Phòng B.46",
                    lop.giangVien
                )
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
            tv.setPadding(20, 25, 20, 25)
            tv.gravity = Gravity.CENTER
            tv.setTextColor(Color.BLACK)
            tv.setBackgroundResource(android.R.drawable.divider_horizontal_bright)
            row.addView(tv)
        }
        bangTKB.addView(row)
    }
}