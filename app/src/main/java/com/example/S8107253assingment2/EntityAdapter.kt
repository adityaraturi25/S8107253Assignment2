package com.example.S8107253assingment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EntityAdapter(
    private val onItemClick: (Map<String, Any?>) -> Unit
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    private var entities: List<Map<String, Any?>> = emptyList()

    fun submitItems(items: List<Map<String, Any?>>) {
        entities = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entity, parent, false)
        return EntityViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        val entity = entities[position]
        val summaryFields = entity.filterKeys { !it.equals("description", ignoreCase = true) }
        val firstField = summaryFields.entries.firstOrNull()

        holder.title.text = firstField?.value?.toString() ?: "Item ${position + 1}"
        holder.summary.text = summaryFields.entries
            .drop(1)
            .joinToString("\n") { (key, value) -> "$key: $value" }

        holder.itemView.setOnClickListener { onItemClick(entity) }
    }

    override fun getItemCount(): Int = entities.size

    class EntityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.entityTitle)
        val summary: TextView = itemView.findViewById(R.id.entitySummary)
    }
}