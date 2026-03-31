package com.example.viewadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewsinhvien.R

class StudentAdapter(private var studentList: List<Map<String, Any>>,
                     private val onItemClick: (Map<String, Any>) -> Unit,
                     private val onItemLongClick: (Map<String, Any>) -> Unit) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.txtStudentName)
        val email = view.findViewById<TextView>(R.id.txtStudentEmail)
        val semester = view.findViewById<TextView>(R.id.txtStudentSemester)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_studentadmin, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.name.text = student["fullName"].toString()
        holder.email.text = student["email"].toString()
        holder.semester.text = "Giới hạn học kỳ: ${student["currentSemesterLimit"]}"
        holder.itemView.setOnClickListener { onItemClick(student) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(student)
            true
        }
    }

    override fun getItemCount() = studentList.size
}
