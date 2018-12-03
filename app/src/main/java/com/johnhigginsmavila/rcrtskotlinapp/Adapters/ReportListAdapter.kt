package com.johnhigginsmavila.rcrtskotlinapp.Adapters

import android.content.Context
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.johnhigginsmavila.rcrtskotlinapp.Controller.App
import com.johnhigginsmavila.rcrtskotlinapp.Model.Report
import com.johnhigginsmavila.rcrtskotlinapp.R

class ReportListAdapter (val context: Context, val reports: ArrayList<Report>, val reportClick: (Report)-> Unit) : RecyclerView.Adapter<ReportListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.report_list_item, parent, false)
        return Holder(view, reportClick)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return reports.count()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bindCategory(reports[position], context)
    }

    inner class Holder(itemView: View, val itemClick: (Report) -> Unit) : RecyclerView.ViewHolder(itemView) {

        val id = itemView?.findViewById<TextView>(R.id.reportIdTxt)
        val title = itemView?.findViewById<TextView>(R.id.titleTxt)
        val status = itemView?.findViewById<TextView>(R.id.categoryTxt)
        val description = itemView?.findViewById<TextView>(R.id.titleTxt)
        val preview = itemView?.findViewById<ImageView>(R.id.previewImg)

        fun bindCategory (report: Report, context: Context) {
            id?.text = report._id
            title?.text = report.title
            status?.text = report.status
            description?.text = report.description
            if (report.medias != null && report.medias!!.count() > 0) {
                preview.visibility = View.VISIBLE
                // preview.setImageURI(Uri.parse(report.medias[0].metaData.getString("secure_url")))
                Glide.with(App.prefs.context).load(Uri.parse(report.medias!![0].metaData.getString("secure_url"))).into(preview)
            } else {
                preview.visibility = View.GONE
            }
            itemView.setOnClickListener { itemClick(report) }
        }
    }
}