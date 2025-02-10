package com.example.qualif.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.qualif.databinding.ItemCardBinding
import com.example.qualif.model.Item

class ItemAdapter(
    private val context: Context,
    private val itemList: List<Item>,
    private val onItemClick: ((Item) -> Unit)? = null  // Click listener (optional)
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.tvItemName.text = item.name
        holder.binding.tvItemDescription.text = item.description
        holder.binding.tvItemPrice.text = "Price: $${item.price}"
        holder.binding.tvItemVersion.text = "MC Version: ${item.compatibleMinecraftVersion}"

        // Handle item click
        holder.binding.cvItemCard.setOnClickListener {
            onItemClick?.invoke(item)  // Trigger callback function
        }
    }
}
