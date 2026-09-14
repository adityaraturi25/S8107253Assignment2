package com.example.S8107253assingment2

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import org.json.JSONObject

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val container = findViewById<LinearLayout>(R.id.detailsContainer)
        val photoCard = findViewById<MaterialCardView>(R.id.detailsPhotoCard)
        val photo = findViewById<ImageView>(R.id.detailsPhoto)

        // Keep the photo area hidden if the dish is unknown.
        photoCard.visibility = View.GONE

        val entityJson = intent.getStringExtra("entity")
        if (entityJson.isNullOrBlank()) {
            addField(container, "No item details were provided.")
            return
        }

        try {
            val entity = JSONObject(entityJson)

            val dishName = entity.keys().asSequence()
                .firstOrNull { it.equals("dishName", ignoreCase = true) }
                ?.let { entity.optString(it) }
                .orEmpty()

            val photoRes = when (dishName.trim().lowercase()) {
                "sushi" -> R.drawable.dish_sushi
                "pizza" -> R.drawable.dish_pizza
                "tacos" -> R.drawable.dish_tacos
                "croissant" -> R.drawable.dish_croissant
                "pad thai" -> R.drawable.dish_pad_thai
                "hamburger" -> R.drawable.dish_hamburger
                "curry" -> R.drawable.dish_curry
                else -> null
            }

            if (photoRes != null) {
                photo.setImageResource(photoRes)
                photo.contentDescription = "Photo of $dishName"
                photoCard.visibility = View.VISIBLE
            }

            val keys = entity.keys().asSequence().toList()
                .sortedBy { it.equals("description", ignoreCase = true) }

            for (key in keys) {
                val label = key.replaceFirstChar { it.uppercase() }
                addField(container, "$label\n${entity.optString(key)}")
            }
        } catch (e: Exception) {
            addField(container, "Could not display this item.")
        }
    }

    private fun addField(container: LinearLayout, content: String) {
        val density = resources.displayMetrics.density
        fun dp(value: Int) = (value * density).toInt()

        val rawLabel = content.substringBefore('\n')
        val value = content.substringAfter('\n', "")
        val label = when (rawLabel.lowercase()) {
            "dishname" -> "Dish name"
            "mainingredient" -> "Main ingredient"
            "mealtype" -> "Meal type"
            else -> rawLabel
        }
        val isDescription = rawLabel.equals("description", ignoreCase = true)

        val card = MaterialCardView(this).apply {
            radius = dp(20).toFloat()
            cardElevation = dp(2).toFloat()
            setCardBackgroundColor(
                Color.parseColor(if (isDescription) "#FFE6D3" else "#FFFFFF")
            )
            strokeColor = Color.parseColor("#EED9CA")
            strokeWidth = dp(1)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dp(12)
            }
        }

        val textContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(16), dp(18), dp(16))
        }

        val labelView = TextView(this).apply {
            text = label.uppercase()
            textSize = 11f
            setTextColor(Color.parseColor("#A75338"))
        }
        textContainer.addView(labelView)

        if (value.isNotEmpty()) {
            val valueView = TextView(this).apply {
                text = value
                textSize = if (isDescription) 16f else 18f
                setTextColor(Color.parseColor("#30231E"))
                setPadding(0, dp(6), 0, 0)
            }
            textContainer.addView(valueView)
        }

        card.addView(textContainer)
        container.addView(card)
    }
}