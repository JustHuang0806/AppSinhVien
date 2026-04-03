package com.example.viewadmin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.viewsinhvien.R

class ScoreAdminAdapter(private val scoreList: List<Map<String, Any>>) :
    RecyclerView.Adapter<ScoreAdminAdapter.ScoreViewHolder>() {

    class ScoreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSubject = view.findViewById<TextView>(R.id.tvAdminSubjectName)
        val tvSemester = view.findViewById<TextView>(R.id.tvAdminSemester)
        val tvGrade = view.findViewById<TextView>(R.id.tvAdminGrade)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_score_admin, parent, false)
        return ScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScoreViewHolder, position: Int) {
        val score = scoreList[position]
        holder.tvSubject.text = score["subjectName"].toString()
        holder.tvSemester.text = score["semester"].toString()
        holder.tvGrade.text = score["grade"].toString()
    }

    override fun getItemCount() = scoreList.size
}