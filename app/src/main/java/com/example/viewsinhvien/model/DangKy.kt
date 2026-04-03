package com.example.viewsinhvien.model

data class LopHocPhan(val thu: String, val tiet: String, val giangVien: String)

// Đổi tên từ monhoc thành Course để tránh xung đột với class MonHoc đã có
data class Course(val maMon: String, val tenMon: String, val tinChi: Int, val dsLop: List<LopHocPhan> = listOf())

object SharedData {
    val dsDaDangKy = mutableListOf<Pair<Course, LopHocPhan>>()
}
