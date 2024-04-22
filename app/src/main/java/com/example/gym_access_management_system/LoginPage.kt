package com.example.gym_access_management_system

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class LoginPage : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var tvLoginError: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        databaseHelper = DatabaseHelper(this)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tvLoginError = findViewById(R.id.loginError)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (validateUser(email, password)) {
                val intent = Intent(this, DetailsView::class.java)
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