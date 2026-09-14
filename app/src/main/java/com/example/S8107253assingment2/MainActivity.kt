package com.example.S8107253assingment2

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.content.Intent

class MainActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val usernameInput = findViewById<TextInputEditText>(R.id.usernameInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val loginButton = findViewById<MaterialButton>(R.id.loginButton)
        val errorText = findViewById<TextView>(R.id.errorText)
        val loadingProgress = findViewById<ProgressBar>(R.id.loadingProgress)

        loginButton.setOnClickListener {
            loginViewModel.login(
                usernameInput.text?.toString().orEmpty(),
                passwordInput.text?.toString().orEmpty()
            )
        }

        loginViewModel.state.observe(this) { state ->
            val loading = state is LoginUiState.Loading
            loginButton.isEnabled = !loading
            loadingProgress.visibility = if (loading) View.VISIBLE else View.GONE

            when (state) {
                is LoginUiState.Error -> {
                    errorText.text = state.message
                    errorText.visibility = View.VISIBLE
                }
                is LoginUiState.Success -> {
                    errorText.visibility = View.GONE
                    startActivity(
                        Intent(this, DashboardActivity::class.java)
                            .putExtra("keypass", state.keypass)
                    )
                    finish()
                }
                else -> errorText.visibility = View.GONE
            }
        }
    }
}