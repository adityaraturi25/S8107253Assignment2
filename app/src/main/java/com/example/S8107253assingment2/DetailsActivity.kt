package com.example.S8107253assingment2

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        val entityJson = intent.getStringExtra("entity")

        if (entityJson.isNullOrBlank()) {
            addField(container, "No item details were provided.")
            return
        }

        try {
            val entity = JSONObject(entityJson)
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
        val spacing = (16 * resources.displayMetrics.density).toInt()
        val field = TextView(this).apply {
            text = content
            textSize = 17f
            setPadding(0, 0, 0, spacing)
        }
        container.addView(field)
    }
}