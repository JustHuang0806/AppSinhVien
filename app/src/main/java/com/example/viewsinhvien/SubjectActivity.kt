package com.example.viewsinhvien

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.viewsinhvien.model.MonHoc


class SubjectActivity : AppCompatActivity() {

    private lateinit var spinnerChuyenNganh: Spinner
    private lateinit var containerBangDiem: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        spinnerChuyenNganh = findViewById(R.id.spinner_chuyen_nganh)
        containerBangDiem = findViewById(R.id.container_bang_diem)

        setupSpinner()
    }

    private fun setupSpinner() {
        val nganhHoc = arrayOf("Công Nghệ Phần Mềm", "Trí tuệ nhân tạo", "Hệ thống thông tin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nganhHoc)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChuyenNganh.adapter = adapter

        spinnerChuyenNganh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                hienThiDuLieuTheoNganh(nganhHoc[position])
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun hienThiDuLieuTheoNganh(tenNganh: String) {
        containerBangDiem.removeAllViews()

        val mapDuLieu = when (tenNganh) {
            "Công Nghệ Phần Mềm" -> layFullExcelCNPM()
            "Trí tuệ nhân tạo" -> layDuLieuAI()
            else -> layDuLieuHTTT()
        }

        for ((hocKy, dsMon) in mapDuLieu) {
            veBangHocKy(hocKy, dsMon)
        }
    }

    private fun veBangHocKy(tenHK: String, dsMon: List<MonHoc>) {
        val tvHocKy = TextView(this).apply {
            text = tenHK
            textSize = 17f
            setTypeface(null, Typeface.BOLD)
            setTextColor(Color.parseColor("#003366"))
            setPadding(0, 30, 0, 15)
        }
        containerBangDiem.addView(tvHocKy)

        val table = TableLayout(this).apply {
            layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
            setBackgroundColor(Color.parseColor("#CCCCCC"))
            setPadding(1, 1, 1, 1)
        }

        val headerRow = TableRow(this).apply { setBackgroundColor(Color.parseColor("#F0F0F0")) }
        val tieuDe = arrayOf("STT", "Mã môn", "Tên môn", "Tín chỉ")
        tieuDe.forEach { text -> taoCell(headerRow, text, true) }
        table.addView(headerRow)

        var tongTin = 0
        dsMon.forEachIndexed { index, mon ->
            val row = TableRow(this).apply { setBackgroundColor(Color.WHITE) }
            taoCell(row, (index + 1).toString(), false)
            taoCell(row, mon.maMH, false)
            taoCell(row, mon.tenMH, false)
            taoCell(row, mon.tinChi.toString(), false)
            table.addView(row)
            tongTin += mon.tinChi
        }

        val footerRow = TableRow(this).apply { setBackgroundColor(Color.parseColor("#E3F2FD")) }
        val tvTongLabel = TextView(this).apply {
            text = "Tổng số tín chỉ học kỳ: "
            setTypeface(null, Typeface.BOLD)
            setPadding(15, 15, 15, 15)
            gravity = Gravity.END
        }
        val params = TableRow.LayoutParams().apply { span = 3 }
        footerRow.addView(tvTongLabel, params)

        val tvTongGiaTri = TextView(this).apply {
            text = tongTin.toString()
            setTypeface(null, Typeface.BOLD)
            setPadding(15, 15, 15, 15)
            gravity = Gravity.CENTER
        }
        footerRow.addView(tvTongGiaTri)

        table.addView(footerRow)
        containerBangDiem.addView(table)
    }

    private fun taoCell(row: TableRow, content: String, isBold: Boolean) {
        val tv = TextView(this).apply {
            text = content
            setPadding(12, 12, 12, 12)
            if (isBold) setTypeface(null, Typeface.BOLD)
            layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f).apply {
                setMargins(1, 1, 1, 1)
            }
            setBackgroundColor(Color.WHITE)
            gravity = if (content.length < 5) Gravity.CENTER else Gravity.START
        }
        row.addView(tv)
    }

    private fun layFullExcelCNPM(): Map<String, List<MonHoc>> {
        return linkedMapOf(
            "Học kỳ 1" to listOf(
                MonHoc("100", "Triết học Mác - Lênin", 0, 0, "", 3),
                MonHoc("101", "Tiếng Anh Cơ Bản 1", 0, 0, "", 4),
                MonHoc("102", "Nhập môn CNTT", 0, 0, "", 2),
                MonHoc("103", "Nhập môn Lập Trình", 0, 0, "", 4),
                MonHoc("104", "Toán rời rạc", 0, 0, "", 3),
                MonHoc("105", "Toán giải tích", 0, 0, "", 3)
            ),
            "Học kỳ 2" to listOf(
                MonHoc("106", "Giáo dục quốc phòng", 0, 0, "", 3),
                MonHoc("107", "Kinh tế chính trị Mác- Lênin", 0, 0, "", 2),
                MonHoc("108", "Tiếng Anh Cơ Bản 2", 0, 0, "", 4),
                MonHoc("109", "Cơ sở dữ liệu", 0, 0, "", 4),
                MonHoc("110", "Đại số tuyến tính", 0, 0, "", 3),
                MonHoc("111", "Kỹ thuật lập trình", 0, 0, "", 4)
            ),
            "Học kỳ 3" to listOf(
                MonHoc("112", "Chủ nghĩa xã hội khoa học", 0, 0, "", 2),
                MonHoc("113", "Tiếng Anh cơ bản", 0, 0, "", 4),
                MonHoc("114", "Cấu trúc dữ liệu và giải thuật", 0, 0, "", 4),
                MonHoc("115", "Hệ điều hành", 0, 0, "", 4),
                MonHoc("116", "Kỹ năng mềm", 0, 0, "", 2),
                MonHoc("117", "Lập trình web", 0, 0, "", 4)
            ),
            "Học kỳ 4" to listOf(
                MonHoc("118", "Đại cương pháp luật VN", 0, 0, "", 2),
                MonHoc("119", "Tư tưởng Hồ Chí Minh", 0, 0, "", 2),
                MonHoc("120", "Lý thuyết đồ thị", 0, 0, "", 3),
                MonHoc("121", "Mạng máy tính", 0, 0, "", 4),
                MonHoc("122", "Lập trình thiết bị di động", 0, 0, "", 4),
                MonHoc("123", "Phân tích thiết kế PM", 0, 0, "", 4)
            ),
            "Học kỳ hè" to listOf(
                MonHoc("124", "Lịch sử Đảng CSVN", 0, 0, "", 2),
                MonHoc("125", "Xác suất thống kê", 0, 0, "", 3),
                MonHoc("126", "Công nghệ phần mềm", 0, 0, "", 4),
                MonHoc("127", "Cơ sở dữ liệu nâng cao", 0, 0, "", 4)
            ),
            "Học kỳ 5" to listOf(
                MonHoc("130", "Kiểm định chất lượng PM", 0, 0, "", 4),
                MonHoc("131", "Đồ án phần mềm", 0, 0, "", 3)
            )
        )
    }

    private fun layDuLieuAI() = mapOf(
        "Chuyên ngành AI - HK 4" to listOf(
            MonHoc("AI201", "Toán cho AI", 0, 0, "", 3),
            MonHoc("AI202", "Học máy cơ bản", 0, 0, "", 4),
            MonHoc("AI203", "Lập trình Python", 0, 0, "", 4)
        )
    )

    private fun layDuLieuHTTT() = mapOf(
        "Chuyên ngành HTTT - HK 4" to listOf(
            MonHoc("IS301", "Quản trị CSDL", 0, 0, "", 3),
            MonHoc("IS302", "Hệ thống ERP", 0, 0, "", 4),
            MonHoc("IS303", "Khai phá dữ liệu", 0, 0, "", 3)
        )
    )
}