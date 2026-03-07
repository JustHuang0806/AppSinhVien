package com.example.viewsinhvien

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.util.*

class EconomyActivity : AppCompatActivity() {

    private lateinit var containerHocKy: LinearLayout
    private val formatTien = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_economy)

        containerHocKy = findViewById(R.id.danh_sach_hoc_ky)
        //GMT +7 Hà nội
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
        val namHienTai = calendar.get(Calendar.YEAR)


        //  2022-2023
        val ds22_HK1 = layDuLieuMauHocKy1_22_23()
        hienThiBangTaiChinh("Học kỳ 1", "2022-2023", ds22_HK1)
        if (!kiemTraDaDongDu(ds22_HK1)) return // Nếu nợ HK1 thì dừng, không hiện các bảng sau

        val ds22_HK2 = layDuLieuMauHocKy2_22_23()
        hienThiBangTaiChinh("Học kỳ 2", "2022-2023", ds22_HK2)
        if (!kiemTraDaDongDu(ds22_HK2)) return

        val ds22_HK3 = layDuLieuMauHocKy3_22_23()
        hienThiBangTaiChinh("Học kỳ 3", "2022-2023", ds22_HK3)
        if (!kiemTraDaDongDu(ds22_HK3)) return

        //  2023-2024
        val ds23_HK1 = layDuLieuHK1_23_24()
        hienThiBangTaiChinh("Học kỳ 1", "2023-2024", ds23_HK1)
        if (!kiemTraDaDongDu(ds23_HK1)) return

        val ds23_HK2 = layDuLieuHK2_23_24()
        hienThiBangTaiChinh("Học kỳ 2", "2023-2024", ds23_HK2)
        if (!kiemTraDaDongDu(ds23_HK2)) return

        val ds23_HK3 = layDuLieuHK3_23_24()
        hienThiBangTaiChinh("Học kỳ 3", "2023-2024", ds23_HK3)
        if (!kiemTraDaDongDu(ds23_HK3)) return

        //  2024-2025
        val ds24_HK1 = layDuLieuHK1_24_25()
        hienThiBangTaiChinh("Học kỳ 1", "2024-2025", ds24_HK1)

        val ds24_HK2 = layDuLieuHK2_24_25()
        hienThiBangTaiChinh("Học kỳ 1", "2024-2025", ds24_HK2)

        val ds24_HK3 = layDuLieuHK3_24_25()
        hienThiBangTaiChinh("Học kỳ 1", "2024-2025", ds24_HK1)

    }

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun hienThiBangTaiChinh(tenHK: String, namHoc: String, danhSachMon: List<MonHoc>) {
        val viewBang = layoutInflater.inflate(R.layout.item_table_economy, null)
        val txtTieuDe = viewBang.findViewById<TextView>(R.id.tv_tieu_de_bang)
        val tableLayout = viewBang.findViewById<TableLayout>(R.id.table_noi_dung)
        val txtTongPhaiDong = viewBang.findViewById<TextView>(R.id.tv_tong_cong)

        txtTieuDe.text = "Năm học: $namHoc, Học kỳ: $tenHK"

        var tongPhaiDong = 0L

        for (mon in danhSachMon) {
            val row = TableRow(this)

            val ConNo = mon.phaiDong - mon.daDong

            // Thêm các cột dữ liệu
            row.addView(taoOText(mon.maMH))
            row.addView(taoOText(mon.tenMH))
            row.addView(taoOText(formatTien.format(mon.phaiDong)))
            row.addView(taoOText(formatTien.format(mon.daDong)))
            row.addView(taoOText(mon.ngayDong))
            row.addView(taoOText("0")) // Miễn giảm
            row.addView(taoOText(formatTien.format(ConNo)))

            tableLayout.addView(row)
            tongPhaiDong += mon.phaiDong
        }

        // Cập nhật tổng tiền học phí cho bảng này
        txtTongPhaiDong.text = "Tổng phải đóng học kỳ: ${formatTien.format(tongPhaiDong)} VNĐ"

        // Thêm toàn bộ bảng vào màn hình chính
        containerHocKy.addView(viewBang)
    }

    private fun taoOText(noiDung: String): TextView {
        return TextView(this).apply {
            text = noiDung
            setPadding(10, 10, 10, 10)
            setBackgroundResource(R.drawable.border_table)
            gravity = Gravity.CENTER

            // textSize dùng Float
            textSize = 12f

            setTextColor(android.graphics.Color.BLACK)
        }
    }

    private fun kiemTraDaDongDu(list: List<MonHoc>): Boolean {
        for (m in list) if (m.phaiDong > m.daDong) return false
        return true
    }

    // (2022 - 2023)
    private fun layDuLieuMauHocKy1_22_23() = listOf(
        MonHoc("111111", "Giải tích", 3600000, 3600000, "02/08/2022"),
        MonHoc("111112", "Toán Rời Rạc",  3600000, 3600000,"02/08/2022"),
        MonHoc("111113", "Nhập môn Lập trình Lý Thuyết", 2400000, 1200000, "02/08/2022"),
        MonHoc("111113", "Nhập môn Lập trình Thực Hành", 1200000, 1200000, "02/08/2022")
    )

    private fun layDuLieuMauHocKy2_22_23() = listOf(
        MonHoc("111114", "Cơ sở dữ liệu", 3600000, 3600000, "20/11/2022"),
        MonHoc("111115", "Tiếng Anh 1", 2400000, 1200000, "20/11/2022"),
        MonHoc("111116", "Kỹ Thuật lập Trình Lý Thuyết", 2400000, 1800000, "20/11/2022"),
        MonHoc("111116", "Kỹ Thuật lập Trình Thực Hành", 1200000,  0, "20/11/2022")
    )

    private fun layDuLieuMauHocKy3_22_23() = listOf(
        MonHoc("111117", "Giáo Dục Quốc Phòng", 3600000, 3600000, "11/05/2022")
    )

    // (2023 - 2024)
    private fun layDuLieuHK1_23_24() = listOf(
        MonHoc("231101", "Cấu trúc dữ liệu", 5400000, 5400000, "15/08/2023"),
        MonHoc("231102", "Lập trình hướng đối tượng", 5400000, 5400000, "15/08/2023")
    )

    private fun layDuLieuHK2_23_24() = listOf(
        MonHoc("232103", "Hệ điều hành", 3600000, 0, "Chưa đóng")
    )

    private fun layDuLieuHK3_23_24() = listOf(
        MonHoc("232103", "Hệ điều hành", 3600000, 0, "Chưa đóng")
    )

    // (2024 - 2025)
    private fun layDuLieuHK1_24_25() = listOf(
        MonHoc("241105", "Phân tích hệ thống", 5400000, 0, "Chưa đóng")
    )

    private fun layDuLieuHK2_24_25() = listOf(
        MonHoc("241105", "Phân tích hệ thống", 5400000, 0, "Chưa đóng")
    )

    private fun layDuLieuHK3_24_25() = listOf(
        MonHoc("241105", "Phân tích hệ thống", 5400000, 0, "Chưa đóng")
    )
}

data class MonHoc(val maMH: String, val tenMH: String, val phaiDong: Long, val daDong: Long, val ngayDong: String)