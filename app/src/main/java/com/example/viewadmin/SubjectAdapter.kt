package com.example.viewadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewsinhvien.R
class SubjectAdapter(private val list: List<Map<String, Any>>) :
    RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCode = view.findViewById<TextView>(R.id.txtSubjectCode)
        val tvName = view.findViewById<TextView>(R.id.txtSubjectName)
        val tvInfo = view.findViewById<TextView>(R.id.txtSubjectInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        // Bạn có thể dùng lại item_student.xml hoặc tạo item_subject.xml tương tự
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_subjectadmin, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val item = list[position]
        holder.tvCode.text = item["subjectCode"].toString()
        holder.tvName.text = item["subjectName"].toString()

        val loai = if (item["isRequired"] == true) "Bắt buộc" else "Tự chọn"
        holder.tvInfo.text = "Kỳ: ${item["semester"]} | TC: ${item["credits"]} | $loai"
    }

    override fun getItemCount() = list.size
}