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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

class DangKyMonHoc : AppCompatActivity() {

    private lateinit var tableMonHocMo: TableLayout
    private lateinit var tableDaDangKy: TableLayout
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dangkymonhoc_activity)

        tableMonHocMo = findViewById(R.id.table_mon_hoc_mo)
        tableDaDangKy = findViewById(R.id.table_da_dang_ky)

        loadDanhSachMonMoTuFirebase()
        loadMonDaDangKyTuFirebase()

        // GỌI HÀM NÀY 1 LẦN ĐỂ LÀM MỚI DỮ LIỆU KHÁC NHAU CHO MỖI MÔN
         //fastPopulateDiverseClasses()
    }

    private fun fastPopulateDiverseClasses() {
        val giaoVienList = listOf("GV. Nguyễn Văn Hoàn", "TS. Trần Minh Tâm", "ThS. Lê Thị Lan", 
                                "GV. Phạm Hải Nam", "PGS.TS Nguyễn Bích Hà", "ThS. Đặng Tuấn Anh",
                                "GV. Ngô Minh Hiếu", "TS. Vũ Thị Thảo", "GV. Lê Văn Tám")
        
        val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7")
        val tietList = listOf("1-3", "4-6", "7-9", "10-12")

        db.collection("All_Subjects").get().addOnSuccessListener { documents ->
            val batch = db.batch()
            for (doc in documents) {
                // Tạo danh sách lớp ngẫu nhiên cho môn này (từ 2 đến 3 lớp)
                val countLop = Random.nextInt(2, 4)
                val diverseClasses = mutableListOf<Map<String, String>>()
                
                val usedSchedules = mutableSetOf<String>()
                
                while (diverseClasses.size < countLop) {
                    val thu = thuList.random()
                    val tiet = tietList.random()
                    val key = "$thu-$tiet"
                    
                    if (!usedSchedules.contains(key)) {
                        usedSchedules.add(key)
                        diverseClasses.add(mapOf(
                            "thu" to thu,
                            "tiet" to tiet,
                            "giangVien" to giaoVienList.random()
                        ))
                    }
                }
                
                batch.update(doc.reference, "classes", diverseClasses)
            }
            
            batch.commit().addOnSuccessListener {
                Toast.makeText(this, "Đã cập nhật lịch học KHÁC NHAU cho mỗi môn!", Toast.LENGTH_LONG).show()
                loadDanhSachMonMoTuFirebase()
            }
        }
    }

    private fun loadDanhSachMonMoTuFirebase() {
        val count = tableMonHocMo.childCount
        if (count > 1) tableMonHocMo.removeViews(1, count - 1)

        db.collection("All_Subjects")
            .get()
            .addOnSuccessListener { documents ->
                val tempMap = mutableMapOf<String, MutableList<Course>>()

                for (doc in documents) {
                    val sem = doc.getLong("semester")?.toString() ?: "Khác"
                    
                    val classesRaw = doc.get("classes") as? List<Map<String, Any>> ?: listOf()
                    val listLop = classesRaw.map { c ->
                        LopHocPhan(
                            thu = c["thu"]?.toString() ?: "Chưa rõ",
                            tiet = c["tiet"]?.toString() ?: "Chưa rõ",
                            giangVien = c["giangVien"]?.toString() ?: "Chưa rõ"
                        )
                    }

                    val course = Course(
                        maMon = doc.getString("subjectCode") ?: "",
                        tenMon = doc.getString("subjectName") ?: "",
                        tinChi = doc.getLong("credits")?.toInt() ?: 0,
                        dsLop = if (listLop.isNotEmpty()) listLop else listOf(
                            LopHocPhan("Tùy chọn", "Liên hệ khoa", "Đang cập nhật")
                        ),
                        semester = sem
                    )
                    
                    if (!tempMap.containsKey(sem)) tempMap[sem] = mutableListOf()
                    tempMap[sem]?.add(course)
                }

                hienThiLenBang(tempMap)
            }
    }

    private fun hienThiLenBang(data: Map<String, List<Course>>) {
        val count = tableMonHocMo.childCount
        if (count > 1) tableMonHocMo.removeViews(1, count - 1)

        for (semKey in data.keys.sorted()) {
            val dsMon = data[semKey] ?: listOf()
            
            val rowHk = TableRow(this)
            rowHk.addView(TextView(this).apply {
                text = "Học kỳ $semKey"; setPadding(10, 30, 10, 10)
                setTextColor(Color.RED); typeface = Typeface.DEFAULT_BOLD
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
            .setTitle("Chọn lớp: ${mon.tenMon}")
            .setItems(options) { _, which ->
                val lopChon = mon.dsLop[which]
                thucHienLuuDangKy(mon, lopChon)
            }
            .show()
    }

    private fun thucHienLuuDangKy(mon: Course, lop: LopHocPhan) {
        val uid = auth.currentUser?.uid ?: return

        val dataSave = hashMapOf(
            "subjectCode" to mon.maMon,
            "subjectName" to mon.tenMon,
            "credits" to mon.tinChi,
            "dayOfWeek" to lop.thu,
            "lessonTime" to lop.tiet,
            "teacher" to lop.giangVien,
            "room" to "Phòng B.46",
            "semester" to mon.semester
        )

        db.collection("Users").document(uid).collection("Schedules")
            .document(mon.maMon)
            .set(dataSave)
            .addOnSuccessListener {
                Toast.makeText(this, "Đã đăng ký: ${mon.tenMon}", Toast.LENGTH_SHORT).show()
                loadMonDaDangKyTuFirebase()
            }
    }

    private fun loadMonDaDangKyTuFirebase() {
        val uid = auth.currentUser?.uid ?: return
        db.collection("Users").document(uid).collection("Schedules")
            .get()
            .addOnSuccessListener { documents ->
                val count = tableDaDangKy.childCount
                if (count > 1) tableDaDangKy.removeViews(1, count - 1)

                for (doc in documents) {
                    val row = TableRow(this)
                    row.addView(taoTextView(doc.getString("subjectCode") ?: ""))
                    row.addView(taoTextView("${doc.getString("subjectName")}\n(${doc.getString("dayOfWeek")} - ${doc.getString("lessonTime")})"))
                    row.addView(taoTextView(doc.get("credits")?.toString() ?: "", true))

                    val btnXoa = Button(this).apply {
                        text = "Xóa"; setTextColor(Color.WHITE); setBackgroundColor(Color.RED)
                        setOnClickListener { xoaDangKy(doc.id) }
                    }
                    row.addView(btnXoa)
                    tableDaDangKy.addView(row)
                }
            }
    }

    private fun xoaDangKy(maMon: String) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("Users").document(uid).collection("Schedules").document(maMon)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Đã hủy đăng ký", Toast.LENGTH_SHORT).show()
                loadMonDaDangKyTuFirebase()
            }
    }

    private fun taoTextView(text: String, isCenter: Boolean = false) = TextView(this).apply {
        this.text = text; setPadding(10, 20, 10, 20)
        if (isCenter) gravity = Gravity.CENTER
        setTextColor(Color.BLACK)
    }
}
