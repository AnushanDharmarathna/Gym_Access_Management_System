package com.example.gym_access_management_system

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast

class SignupPage : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        databaseHelper = DatabaseHelper(this)

        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val spinner = findViewById<Spinner>(R.id.spinner1)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnSignUp.setOnClickListener {
            val fullName = etFullName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val selectedOption = spinner.selectedItem.toString()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (isEmailUnique(email)) {
                saveUserToDatabase(fullName, phone, selectedOption, email, password)
                Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginPage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email is already used!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserToDatabase(fullName: String, phone: String, selectedOption: String, email: String, password: String) {
        val db = databaseHelper.writableDatabase

        val values = ContentValues().apply {
            put("fullName", fullName)
            put("phone", phone)
            put("selectedOption", selectedOption)
            put("email", email)
            put("password", password)
        }

        db?.insert("users", null, values)
        db?.close()
    }

    private fun isEmailUnique(email: String): Boolean {
        val db = databaseHelper.readableDatabase

        val projection = arrayOf("email")
        val selection = "email = ?"
        val selectionArgs = arrayOf(email)

        val cursor = db.query(
            "users",
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val isUnique = cursor.count == 0
        cursor.close()
        return isUnique
    }
}