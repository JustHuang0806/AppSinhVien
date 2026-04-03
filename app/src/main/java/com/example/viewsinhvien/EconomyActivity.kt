package com.example.viewsinhvien

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat
import java.util.Calendar
import java.util.TimeZone
import com.example.viewsinhvien.model.MonHoc


class EconomyActivity : AppCompatActivity() {

    private lateinit var containerHocKy: LinearLayout
    private val formatTien = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_economy)

        containerHocKy = findViewById(R.id.danh_sach_hoc_ky)

        // Hiển thị tất cả học kỳ mà không dùng return để tránh ngắt quãng
        
        // 2022-2023
        val ds22_HK1 = layDuLieuMauHocKy1_22_23()
        hienThiBangTaiChinh("Học kỳ 1", "2022-2023", ds22_HK1)

        val ds22_HK2 = layDuLieuMauHocKy2_22_23()
        hienThiBangTaiChinh("Học kỳ 2", "2022-2023", ds22_HK2)

        val ds22_HK3 = layDuLieuMauHocKy3_22_23()
        hienThiBangTaiChinh("Học kỳ 3", "2022-2023", ds22_HK3)

        // 2023-2024
        val ds23_HK1 = layDuLieuHK1_23_24()
        hienThiBangTaiChinh("Học kỳ 1", "2023-2024", ds23_HK1)

        val ds23_HK2 = layDuLieuHK2_23_24()
        hienThiBangTaiChinh("Học kỳ 2", "2023-2024", ds23_HK2)

        val ds23_HK3 = layDuLieuHK3_23_24()
        hienThiBangTaiChinh("Học kỳ 3", "2023-2024", ds23_HK3)

        // 2024-2025
        val ds24_HK1 = layDuLieuHK1_24_25()
        hienThiBangTaiChinh("Học kỳ 1", "2024-2025", ds24_HK1)

        val ds24_HK2 = layDuLieuHK2_24_25()
        hienThiBangTaiChinh("Học kỳ 2", "2024-2025", ds24_HK2)
    }

    private fun hienThiBangTaiChinh(tenHK: String, namHoc: String, danhSachMon: List<MonHoc>) {
        val viewBang = layoutInflater.inflate(R.layout.item_table_economy, null)
        val txtTieuDe = viewBang.findViewById<TextView>(R.id.tv_tieu_de_bang)
        val tableLayout = viewBang.findViewById<TableLayout>(R.id.table_noi_dung)
        val txtTongPhaiDong = viewBang.findViewById<TextView>(R.id.tv_tong_cong)

        txtTieuDe.text = "Năm học: $namHoc, Học kỳ: $tenHK"

        var tongPhaiDong = 0L

        for (mon in danhSachMon) {
            val row = TableRow(this)
            val conNo = mon.phaiDong - mon.daDong

            row.addView(taoOText(mon.maMH))
            row.addView(taoOText(mon.tenMH))
            row.addView(taoOText(formatTien.format(mon.phaiDong)))
            row.addView(taoOText(formatTien.format(mon.daDong)))
            row.addView(taoOText(mon.ngayDong))
            row.addView(taoOText("0")) 
            row.addView(taoOText(formatTien.format(conNo)))

            tableLayout.addView(row)
            tongPhaiDong += mon.phaiDong
        }

        txtTongPhaiDong.text = "Tổng phải đóng học kỳ: ${formatTien.format(tongPhaiDong)} VNĐ"
        containerHocKy.addView(viewBang)
    }

    private fun taoOText(noiDung: String): TextView {
        return TextView(this).apply {
            text = noiDung
            setPadding(10, 10, 10, 10)
            setBackgroundResource(R.drawable.border_table)
            gravity = Gravity.CENTER
            textSize = 12f
            setTextColor(android.graphics.Color.BLACK)
        }
    }

    // Dữ liệu mẫu
    private fun layDuLieuMauHocKy1_22_23() = listOf(
        MonHoc("111111", "Giải tích", 3600000, 3600000, "02/08/2022", 3),
        MonHoc("111112", "Toán Rời Rạc", 3600000, 3600000, "02/08/2022", 3),
        MonHoc("111113", "Lập trình Lý Thuyết", 2400000, 1200000, "02/08/2022", 2)
    )

    private fun layDuLieuMauHocKy2_22_23() = listOf(
        MonHoc("111114", "Cơ sở dữ liệu", 3600000, 3600000, "20/11/2022", 3),
        MonHoc("111116", "Kỹ Thuật lập Trình", 3600000, 1800000, "20/11/2022", 4)
    )

    private fun layDuLieuMauHocKy3_22_23() = listOf(
        MonHoc("111117", "Giáo Dục Quốc Phòng", 3600000, 3600000, "11/05/2022", 3)
    )

    private fun layDuLieuHK1_23_24() = listOf(
        MonHoc("231101", "Cấu trúc dữ liệu", 5400000, 5400000, "15/08/2023", 4),
        MonHoc("231102", "Lập trình hướng đối tượng", 5400000, 5400000, "15/08/2023", 4)
    )

    private fun layDuLieuHK2_23_24() = listOf(
        MonHoc("232103", "Hệ điều hành", 3600000, 0, "Chưa đóng", 3)
    )

    private fun layDuLieuHK3_23_24() = listOf(
        MonHoc("233104", "Mạng máy tính", 3600000, 0, "Chưa đóng", 3)
    )

    private fun layDuLieuHK1_24_25() = listOf(
        MonHoc("241105", "Phân tích hệ thống", 5400000, 0, "Chưa đóng", 4)
    )

    private fun layDuLieuHK2_24_25() = listOf(
        MonHoc("242106", "Thiết kế phần mềm", 5400000, 0, "Chưa đóng", 4)
    )
}
