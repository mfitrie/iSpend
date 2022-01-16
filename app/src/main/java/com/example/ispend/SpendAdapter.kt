package com.example.ispend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.spend_holder.view.*

class SpendAdapter(
    var spends: List<Spend>
) : RecyclerView.Adapter<SpendAdapter.SpendViewHolder>(){

    inner class SpendViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.spend_holder, parent, false)
        return SpendViewHolder(view)
    }

    override fun onBindViewHolder(holder: SpendViewHolder, position: Int) {
        holder.itemView.apply {
            spend_id.text = spends[position].spend_id.toString()
            spend_value.text = spends[position].spend_value.toString()
            spend_type.text = spends[position].spend_type
            spend_date.text = spends[position].spend_date
        }
    }

    override fun getItemCount(): Int {
        return spends.size
    }
}