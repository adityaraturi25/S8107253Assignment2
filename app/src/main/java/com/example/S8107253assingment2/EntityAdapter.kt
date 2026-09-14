package com.example.S8107253assingment2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
        val summaryFields = entity.filterKeys {
            !it.equals("description", ignoreCase = true)
        }
        val firstField = summaryFields.entries.firstOrNull()
        val dishName = entity.entries
            .firstOrNull { it.key.equals("dishName", ignoreCase = true) }
            ?.value?.toString()
            ?: firstField?.value?.toString()
            ?: "Item ${position + 1}"

        holder.title.text = dishName
        holder.summary.text = summaryFields.entries
            .drop(1)
            .joinToString("\n") { (key, value) -> "$key: $value" }

        val imageResource = when (dishName.trim().lowercase()) {
            "sushi" -> R.drawable.dish_sushi
            "pizza" -> R.drawable.dish_pizza
            "tacos" -> R.drawable.dish_tacos
            "croissant" -> R.drawable.dish_croissant
            "pad thai" -> R.drawable.dish_pad_thai
            "hamburger" -> R.drawable.dish_hamburger
            "curry" -> R.drawable.dish_curry
            else -> null
        }

        if (imageResource != null) {
            holder.image.setImageResource(imageResource)
            holder.image.contentDescription = "Photo of $dishName"
            holder.image.visibility = View.VISIBLE
        } else {
            holder.image.setImageDrawable(null)
            holder.image.visibility = View.GONE
        }

        holder.itemView.setOnClickListener { onItemClick(entity) }
    }

    override fun getItemCount(): Int = entities.size

    class EntityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.entityImage)
        val title: TextView = itemView.findViewById(R.id.entityTitle)
        val summary: TextView = itemView.findViewById(R.id.entitySummary)
    }
}