package com.example.gym_access_management_system

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginPage : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        databaseHelper = DatabaseHelper(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvLoginError = findViewById<TextView>(R.id.loginError)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateUser(email, password)) {
                val intent = Intent(this, DetailsView::class.java)
                intent.putExtra("loggedInUserEmail", email)
                startActivity(intent)
                finish()
            } else {
                tvLoginError.text = "Invalid email or password"
            }
        }
    }

    private fun validateUser(email: String, password: String): Boolean {
        val db = databaseHelper.readableDatabase

        val projection = arrayOf("id")
        val selection = "email = ? AND password = ?"
        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(
            "users",
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val isValid = cursor.moveToFirst()
        cursor.close()
        return isValid
    }
}