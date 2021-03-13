package com.yolo.rollataskone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.yolo.rollataskone.ItemClickListener
import com.yolo.rollataskone.R
import com.yolo.rollataskone.data.Item

class ItemAdapter(private val context: Context,
                  private val items: List<Item>,
                  private val itemClickListener: ItemClickListener
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.item_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item,parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.text = items[position].text
        holder.button.setOnClickListener {
            itemClickListener.onItemClickListener(items[position])
        }
    }
}