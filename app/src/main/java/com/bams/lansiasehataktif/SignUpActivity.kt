package com.bams.lansiasehataktif

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etSignUpEmail: EditText = findViewById(R.id.etSignUpEmail)
        val etSignUpPassword: EditText = findViewById(R.id.etSignUpPassword)
        val btnSignUp: Button = findViewById(R.id.btnSignUp)

        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        btnSignUp.setOnClickListener {
            val email = etSignUpEmail.text.toString()
            val password = etSignUpPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                editor.putString(email, password)
                editor.apply()
                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
