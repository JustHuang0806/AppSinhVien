package com.example.viewsinhvien

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.example.viewsinhvien.model.MonHoc
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SubjectActivity : AppCompatActivity() {

    private lateinit var containerBangDiem: LinearLayout
    private lateinit var spinnerChuyenNganh: Spinner
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        spinnerChuyenNganh = findViewById(R.id.spinner_chuyen_nganh)
        containerBangDiem = findViewById(R.id.container_bang_diem)

        setupSpinner()
    }

    private fun setupSpinner() {
        // Chỉ để duy nhất 1 chuyên ngành là Công nghệ phần mềm
        val nganhHoc = arrayOf("Công Nghệ Phần Mềm")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nganhHoc)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChuyenNganh.adapter = adapter

        // Spinner luôn hiện
        spinnerChuyenNganh.visibility = View.VISIBLE

        spinnerChuyenNganh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Tự động load dữ liệu khi chuyên ngành được chọn
                loadAllSubjectsFromFirebase()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun loadAllSubjectsFromFirebase() {
        containerBangDiem.removeAllViews()
        
        val loadingText = TextView(this).apply { 
            text = "Đang tải chương trình đào tạo..." 
            setPadding(20, 20, 20, 20)
            gravity = Gravity.CENTER
        }
        containerBangDiem.addView(loadingText)

        db.collection("All_Subjects")
            .orderBy("semester", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                containerBangDiem.removeView(loadingText)
                
                if (querySnapshot.isEmpty) {
                    val emptyText = TextView(this).apply { 
                        text = "Chưa có dữ liệu môn học."
                        gravity = Gravity.CENTER
                        setPadding(0, 50, 0, 0)
                    }
                    containerBangDiem.addView(emptyText)
                    return@addOnSuccessListener
                }

                val semesterMap = mutableMapOf<Int, MutableList<MonHoc>>()
                for (doc in querySnapshot) {
                    val semesterNum = doc.getLong("semester")?.toInt() ?: 1
                    val mon = MonHoc(
                        maMH = doc.getString("subjectCode") ?: "",
                        tenMH = doc.getString("subjectName") ?: "",
                        phaiDong = 0, daDong = 0, ngayDong = "",
                        tinChi = doc.getLong("credits")?.toInt() ?: 0
                    )
                    if (!semesterMap.containsKey(semesterNum)) {
                        semesterMap[semesterNum] = mutableListOf()
                    }
                    semesterMap[semesterNum]?.add(mon)
                }

                for (semKey in semesterMap.keys.sorted()) {
                    val dsMon = semesterMap[semKey] ?: listOf()
                    veBangHocKy("Học kỳ $semKey", dsMon)
                }
            }
            .addOnFailureListener { e ->
                containerBangDiem.removeView(loadingText)
                Toast.makeText(this, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun veBangHocKy(tenHK: String, dsMon: List<MonHoc>) {
        val tvHocKy = TextView(this).apply {
            text = tenHK
            textSize = 18f
            setTypeface(null, Typeface.BOLD)
            setTextColor("#003366".toColorInt())
            setPadding(10, 40, 10, 20)
        }
        containerBangDiem.addView(tvHocKy)

        val headerLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor("#F0F2F5".toColorInt())
            weightSum = 10f
        }
        
        taoCell(headerLayout, "STT", true, 1f)
        taoCell(headerLayout, "Mã môn", true, 2f)
        taoCell(headerLayout, "Tên môn học", true, 5f)
        taoCell(headerLayout, "Tín chỉ", true, 2f)
        
        containerBangDiem.addView(headerLayout)

        var tongTin = 0
        dsMon.forEachIndexed { index, mon ->
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                weightSum = 10f
            }
            taoCell(row, (index + 1).toString(), false, 1f)
            taoCell(row, mon.maMH, false, 2f)
            taoCell(row, mon.tenMH, false, 5f)
            taoCell(row, mon.tinChi.toString(), false, 2f)
            
            containerBangDiem.addView(row)
            tongTin += mon.tinChi
        }

        val footer = TextView(this).apply {
            val textValue = "Tổng số tín chỉ học kỳ: $tongTin"
            text = textValue
            setTypeface(null, Typeface.BOLD)
            setPadding(20, 15, 20, 15)
            gravity = Gravity.END
            setBackgroundResource(R.drawable.border_table)
            setTextColor("#003366".toColorInt())
        }
        containerBangDiem.addView(footer)
    }

    private fun taoCell(parent: LinearLayout, content: String, isBold: Boolean, weight: Float) {
        val tv = TextView(this).apply {
            text = content
            setPadding(15, 20, 15, 20)
            if (isBold) setTypeface(null, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, weight)
            setBackgroundResource(R.drawable.border_table)
            gravity = if (weight < 3f) Gravity.CENTER else Gravity.START or Gravity.CENTER_VERTICAL
            setTextColor(Color.BLACK)
            textSize = 13f
        }
        parent.addView(tv)
    }
}
