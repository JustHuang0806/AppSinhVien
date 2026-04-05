package com.example.viewsinhvien

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.DecimalFormat

class EconomyActivity : AppCompatActivity() {

    private lateinit var containerHocKy: LinearLayout
    private val formatTien = DecimalFormat("#,###")
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    // Đơn giá mẫu cho mỗi tín chỉ
    private val DON_GIA_TIN_CHI = 1200000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_economy)

        containerHocKy = findViewById(R.id.danh_sach_hoc_ky)

        reloadFinanceData()
    }

    private fun reloadFinanceData() {
        val uid = auth.currentUser?.uid ?: return
        containerHocKy.removeAllViews()

        // Bước 1: Lấy giới hạn học kỳ (currentSemesterLimit)
        db.collection("Users").document(uid).get()
            .addOnSuccessListener { userDoc ->
                val currentLimit = userDoc.getLong("currentSemesterLimit")?.toInt() ?: 1
                
                // Bước 2: Lấy tất cả môn đã đăng ký (Schedules)
                db.collection("Users").document(uid).collection("Schedules").get()
                    .addOnSuccessListener { scheduleDocs ->
                        val allRegisteredCourses = scheduleDocs.documents
                        
                        // Bước 3: Lấy dữ liệu đã đóng (Economy - do Admin nhập)
                        db.collection("Users").document(uid).collection("Economy").get()
                            .addOnSuccessListener { economyDocs ->
                                val paidMap = mutableMapOf<String, Long>()
                                for (eDoc in economyDocs) {
                                    paidMap[eDoc.id] = eDoc.getLong("paid") ?: 0L
                                }

                                // Nhóm các môn theo học kỳ và lọc theo currentLimit
                                val semesterGroups = mutableMapOf<Int, MutableList<Map<String, Any>>>()
                                
                                for (sDoc in allRegisteredCourses) {
                                    val semStr = sDoc.getString("semester") ?: "1"
                                    val semNum = semStr.filter { it.isDigit() }.toIntOrNull() ?: 1
                                    
                                    // CHỈ HIỂN THỊ HỌC KỲ HIỆN TẠI TRỞ VỀ TRƯỚC
                                    if (semNum <= currentLimit) {
                                        if (!semesterGroups.containsKey(semNum)) {
                                            semesterGroups[semNum] = mutableListOf()
                                        }
                                        semesterGroups[semNum]?.add(sDoc.data ?: mapOf())
                                    }
                                }

                                // Hiển thị bảng cho từng học kỳ hợp lệ
                                if (semesterGroups.isEmpty()) {
                                    val tv = TextView(this).apply { text = "Chưa có dữ liệu học phí." }
                                    containerHocKy.addView(tv)
                                } else {
                                    for (sem in semesterGroups.keys.sorted()) {
                                        val courses = semesterGroups[sem] ?: listOf()
                                        val paidAmt = paidMap["HK$sem"] ?: 0L
                                        hienThiBangHocPhiChiTiet(sem, courses, paidAmt)
                                    }
                                }
                            }
                    }
            }
    }

    private fun hienThiBangHocPhiChiTiet(semesterNum: Int, courses: List<Map<String, Any>>, daDong: Long) {
        val viewBang = layoutInflater.inflate(R.layout.item_table_economy, null)
        val txtTieuDe = viewBang.findViewById<TextView>(R.id.tv_tieu_de_bang)
        val tableLayout = viewBang.findViewById<TableLayout>(R.id.table_noi_dung)
        val txtTongCong = viewBang.findViewById<TextView>(R.id.tv_tong_cong)

        txtTieuDe.text = "Học kỳ: $semesterNum"

        var tongPhaiDongHocKy = 0L

        // Hiển thị chi tiết từng môn
        for (mon in courses) {
            val name = mon["subjectName"]?.toString() ?: "Môn học"
            val code = mon["subjectCode"]?.toString() ?: "-"
            val tinChi = (mon["credits"] as? Long) ?: 3L
            val phaiDongMon = tinChi * DON_GIA_TIN_CHI
            
            val row = TableRow(this)
            row.addView(taoOText(code, 70))
            
            val tvName = taoOText(name, 220)
            tvName.gravity = Gravity.START
            row.addView(tvName)

            row.addView(taoOText(formatTien.format(phaiDongMon), 110))
            row.addView(taoOText("-", 110)) // Cột đã đóng để trống cho từng dòng
            row.addView(taoOText("-", 110)) // Cột còn nợ để trống cho từng dòng

            tableLayout.addView(row)
            tongPhaiDongHocKy += phaiDongMon
        }

        // Dòng tổng kết cho học kỳ
        val conNo = tongPhaiDongHocKy - daDong
        txtTongCong.text = "Tổng phải đóng: ${formatTien.format(tongPhaiDongHocKy)} | Đã đóng: ${formatTien.format(daDong)} | Còn nợ: ${formatTien.format(conNo)} VNĐ"
        
        containerHocKy.addView(viewBang)
    }

    private fun taoOText(noiDung: String, widthDp: Int): TextView {
        val density = resources.displayMetrics.density
        return TextView(this).apply {
            text = noiDung
            setPadding(12, 20, 12, 20)
            setBackgroundResource(R.drawable.border_table)
            gravity = Gravity.CENTER
            textSize = 14f
            setTextColor(android.graphics.Color.BLACK)
            width = (widthDp * density).toInt()
        }
    }
}
