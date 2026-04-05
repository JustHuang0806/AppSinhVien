package com.example.viewsinhvien.model

data class LopHocPhan(val thu: String, val tiet: String, val giangVien: String)

// Thêm trường semester để quản lý lịch học theo kỳ
data class Course(
    val maMon: String, 
    val tenMon: String, 
    val tinChi: Int, 
    val dsLop: List<LopHocPhan> = listOf(),
    val semester: String = "" 
)

object SharedData {
    val dsDaDangKy = mutableListOf<Pair<Course, LopHocPhan>>()
}
