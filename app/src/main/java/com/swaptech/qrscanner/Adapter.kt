package com.swaptech.qrscanner

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(private var items: MutableList<HistoryItem>, private val contentClickListener: OnQRResultClick): RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView by lazy {
            itemView.findViewById(R.id.qr_result_tv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.history_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if(URLValidator.validate(item.value).first) {
            item.value.let {
                val spannableString = SpannableString(it)
                spannableString.setSpan(UnderlineSpan(), 0, it.length, 0)
                spannableString
            }
        }
        holder.textView.text = item.value

        holder.textView.setOnClickListener {
            contentClickListener.onQRResultClicked(item, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateAdapter(newItems: MutableList<HistoryItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    interface OnQRResultClick {
        fun onQRResultClicked(item: HistoryItem, position: Int)
    }
}