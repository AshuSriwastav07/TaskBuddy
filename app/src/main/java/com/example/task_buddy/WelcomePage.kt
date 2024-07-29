package com.example.task_buddy

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomePage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_page)

    }

    fun openSignUp(view: View) { //send user to signup page
        val intent= Intent(this,SignupPage::class.java)
        startActivity(intent)
        finish()
    }

    fun openLogIn(view: View) { //send user to login page
        val intent= Intent(this,LogInPage::class.java)
        startActivity(intent)
        finish()
    }

}