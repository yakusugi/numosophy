package com.numosophy.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.numosophy.R
import com.numosophy.entity.Sale

class SalesAdapter(private var salesList: List<Sale>) : RecyclerView.Adapter<SalesAdapter.SaleViewHolder>() {

    inner class SaleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.text_sale_title)
        val amountText: TextView = itemView.findViewById(R.id.text_sale_amount)
        val dateText: TextView = itemView.findViewById(R.id.text_sale_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sale, parent, false)
        return SaleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SaleViewHolder, position: Int) {
        val sale = salesList[position]
        holder.titleText.text = sale.title
        holder.amountText.text = "$${String.format("%.2f", sale.amount)}"
        holder.dateText.text = sale.date
    }

    override fun getItemCount(): Int = salesList.size

    fun updateSales(newSalesList: List<Sale>) {
        salesList = newSalesList
        notifyDataSetChanged()
    }
}
