package com.example.S8107253assingment2

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Intent
import org.json.JSONObject

class DashboardActivity : AppCompatActivity() {

    private val dashboardViewModel: DashboardViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val padding = (20 * resources.displayMetrics.density).toInt()
            view.setPadding(
                bars.left + padding,
                bars.top + padding,
                bars.right + padding,
                bars.bottom + padding
            )
            insets
        }

        val count = findViewById<TextView>(R.id.entityCount)
        val progress = findViewById<ProgressBar>(R.id.dashboardProgress)
        val message = findViewById<TextView>(R.id.dashboardMessage)
        val retryButton = findViewById<MaterialButton>(R.id.retryButton)
        val list = findViewById<RecyclerView>(R.id.entitiesList)

        val adapter = EntityAdapter { entity ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("entity", JSONObject(entity).toString())
            startActivity(intent)
        }
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        val keypass = intent.getStringExtra("keypass").orEmpty()

        retryButton.setOnClickListener { dashboardViewModel.load(keypass) }

        dashboardViewModel.state.observe(this) { state ->
            progress.visibility =
                if (state is DashboardUiState.Loading) View.VISIBLE else View.GONE

            message.visibility =
                if (state is DashboardUiState.Error) View.VISIBLE else View.GONE

            retryButton.visibility =
                if (state is DashboardUiState.Error && keypass.isNotBlank())
                    View.VISIBLE else View.GONE

            when (state) {
                is DashboardUiState.Success -> {
                    adapter.submitItems(state.response.entities)
                    count.text = "${state.response.entityTotal} items"
                    if (state.response.entities.isEmpty()) {
                        message.text = "No items found."
                        message.visibility = View.VISIBLE
                    }
                }
                is DashboardUiState.Error -> message.text = state.message
                else -> Unit
            }
        }

        if (dashboardViewModel.state.value == DashboardUiState.Idle) {
            dashboardViewModel.load(keypass)
        }
    }
}