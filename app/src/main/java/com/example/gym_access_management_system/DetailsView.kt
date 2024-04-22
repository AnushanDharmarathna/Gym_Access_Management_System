package com.example.gym_access_management_system

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DetailsView : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvPackage: TextView
    private lateinit var tvFee: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_view)

        databaseHelper = DatabaseHelper(this)

        tvName = findViewById(R.id.nameView)
        tvPhone = findViewById(R.id.phoneView)
        tvEmail = findViewById(R.id.emailView)
        tvPackage = findViewById(R.id.packageView)
        tvFee = findViewById(R.id.feeView)

        val user = getLoggedInUser()

        if (user != null) {
            tvName.text = user.fullName
            tvPhone.text = user.phone
            tvEmail.text = user.email
            tvPackage.text = user.selectedOption

            tvFee.text = calculateFee(user.selectedOption)
        }
    }

    private fun getLoggedInUser(): User? {
        val db = databaseHelper.readableDatabase

        val projection = arrayOf(
            "fullName",
            "phone",
            "selectedOption",
            "email"
        )

        val cursor = db.query(
            "users",
            projection,
            null,
            null,
            null,
            null,
            null
        )

        var user: User? = null
        with(cursor) {
            if (moveToFirst()) {
                val fullName = getString(getColumnIndexOrThrow("fullName"))
                val phone = getString(getColumnIndexOrThrow("phone"))
                val selectedOption = getString(getColumnIndexOrThrow("selectedOption"))
                val email = getString(getColumnIndexOrThrow("email"))
                user = User(fullName, phone, selectedOption, email)
            }
            close()
        }

        return user
    }

    private fun calculateFee(selectedOption: String): String {
        return when (selectedOption) {
            "3 Months Plan" -> "Rs. 3,000.00"
            "6 Months Plan" -> "Rs. 5,000.00"
            "12 Months Plan" -> "Rs. 10,000.00"
            else -> "N/A"
        }
    }

    data class User(
        val fullName: String,
        val phone: String,
        val selectedOption: String,
        val email: String
    )
}