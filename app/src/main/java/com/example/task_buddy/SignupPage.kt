package com.example.task_buddy

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignupPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_page)

        //Layout Item Initialize and declare
        val Username:EditText=findViewById(R.id.SignUpEnterUserName)
        val Email:EditText=findViewById(R.id.SignUpEnterEmail)
        val Password:EditText=findViewById(R.id.SignUpEnterPassword)
        val SignUpButton:Button=findViewById(R.id.SignUpButton)


        SignUpButton.setOnClickListener {

            // local variables Initialization
            val email:String
            val password:String
            val username:String
            val auth:FirebaseAuth=FirebaseAuth.getInstance()

            //get Text from EditText
            email= Email.text.toString()
            username= Username.text.toString()
            password= Password.text.toString()

            //check if any Exit text if null or empty or not
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this,"Enter Email!",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Enter Password!",Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }
            if(TextUtils.isEmpty(username)){
                Toast.makeText(this,"Enter Username!",Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }

            //start SignUp
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information


                        Toast.makeText(this,"Registration successfully",Toast.LENGTH_LONG).show()
                        val user = auth.currentUser
                        val intent= Intent(this,LogInPage::class.java)
                        startActivity(intent)
                        finish()


                        val profileUpdates = UserProfileChangeRequest.Builder()  //this will set Username into Display name
                            .setDisplayName(username)
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    Log.d("RegisterActivity", "User profile updated.")
                                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                                    // Redirect to another activity if needed
                                } else {
                                    Log.d("RegisterActivity", "User profile update failed.")
                                    Toast.makeText(this, "Profile update failed.", Toast.LENGTH_SHORT).show()
                                }
                            }


                    } else { //Give Details what is wrong in given data

                        // If sign in fails, display a message to the user.
                        val returnValue:String=task.exception.toString()
                        if(returnValue.contains("The email address is already in use by another account")){
                            Toast.makeText(this,"The email address is already in use by another account",Toast.LENGTH_LONG).show()
                        }

                        else if(returnValue.contains("The email address is badly formatted")) {
                            Toast.makeText(this,"The email address is badly formatted! Please Use Correct Email",Toast.LENGTH_LONG).show()
                        }

                        else if(returnValue.contains("Password should be at least 6 characters")){
                            Toast.makeText(this,"Password should be at least 6 characters.",Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(this,"Something Went Wrong!",Toast.LENGTH_LONG).show()

                        }


                    }
                }
        }

    }

    fun openSignUp(view: View) {

    }

    fun openLogin(view: View) {
        val intent= Intent(this,LogInPage::class.java)
        startActivity(intent)
        finish() // clear backstack
    }

}