package com.example.viewsinhvien

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Adapter từng Học kỳ
class HocKyAdapter(
    private val danhSachHocKy: List<Semester>,
    private val khiBamChiTiet: (Subject) -> Unit
) : RecyclerView.Adapter<HocKyAdapter.HocKyViewHolder>() {

    class HocKyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTieuDeHocKy: TextView = view.findViewById(R.id.tv_semester_title)
        val rvDanhSachMonHoc: RecyclerView = view.findViewById(R.id.rv_subjects)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HocKyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.khung_hoc_ky, parent, false)
        return HocKyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HocKyViewHolder, position: Int) {
        val hocKy = danhSachHocKy[position]
        holder.tvTieuDeHocKy.text = hocKy.title

        holder.rvDanhSachMonHoc.layoutManager = LinearLayoutManager(holder.itemView.context)
        holder.rvDanhSachMonHoc.adapter = MonHocAdapter(hocKy.subjects, khiBamChiTiet)
    }

    override fun getItemCount() = danhSachHocKy.size
}

// Adapter cho từng dòng Môn học
class MonHocAdapter(
    private val danhSachMon: List<Subject>,
    private val khiBamChiTiet: (Subject) -> Unit
) : RecyclerView.Adapter<MonHocAdapter.MonHocViewHolder>() {

    class MonHocViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvStt: TextView = view.findViewById(R.id.tv_stt)
        val tvMaMon: TextView = view.findViewById(R.id.tv_ma_mon_hoc)
        val tvTenMon: TextView = view.findViewById(R.id.tv_ten_mon)
        val tvTinChi: TextView = view.findViewById(R.id.tv_tin_chi)
        val tvDiemHe4: TextView = view.findViewById(R.id.tv_diem_he_4)
        val tvDiemChu: TextView = view.findViewById(R.id.tv_diem_chu)
        val ivKetQua: ImageView = view.findViewById(R.id.iv_ket_qua)
        val btnChiTiet: TextView = view.findViewById(R.id.tv_btn_chi_tiet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonHocViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.diem_mon_hoc, parent, false)
        return MonHocViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonHocViewHolder, position: Int) {
        val mon = danhSachMon[position]

        holder.tvStt.text = (position + 1).toString()
        holder.tvMaMon.text = mon.MaSinhVien.toString()
        holder.tvTenMon.text = mon.TenSinhVien
        holder.tvTinChi.text = mon.TinChi.toString()

        holder.tvDiemHe4.text = "%.2f".format(mon.Diemhe4)
        holder.tvDiemChu.text = mon.DiemChu

        holder.ivKetQua.setImageResource(if (mon.isPassed) R.drawable.ic_tichxanh else R.drawable.ic_xmaudo)

        holder.btnChiTiet.setOnClickListener {
            khiBamChiTiet(mon)
        }
    }

    override fun getItemCount() = danhSachMon.size
}