package com.example.task_buddy

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class LogInPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in_page)

        //Initialise all the variable and item
        val enterEmail:EditText=findViewById(R.id.LoginEnterEmail)
        val enterPassword:EditText=findViewById(R.id.LoginEnterPassword)
        val LoginButton:Button=findViewById(R.id.Loginbutton)
        val auth:FirebaseAuth=FirebaseAuth.getInstance()


        LoginButton.setOnClickListener {

            //Get Email and Password from Edit Text
            val emailAddress:String=enterEmail.text.toString()
            val password:String=enterPassword.text.toString()

            //Check if Any Field is empty or not
            if(TextUtils.isEmpty(emailAddress)){
                Toast.makeText(this,"Enter Email Address!",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"Enter Password!",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            //Start Authorization process
            auth.signInWithEmailAndPassword(emailAddress,password).addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){  //on Complete
                    Toast.makeText(this,"Login Successful",Toast.LENGTH_LONG).show()
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{ //On Error
                    val returnVale=task.exception.toString()

                    if(returnVale.contains("The supplied auth credential is incorrect")){
                        Toast.makeText(this,"Wrong Email/Password, Please Enter Correct Credentials!",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this,"Something went wrong, Please Try Again!",Toast.LENGTH_LONG).show()
                    }
                }
            }

        }




    }

    fun openSignUp(view: View) {
        val intent= Intent(this,SignupPage::class.java)
        startActivity(intent)
        finish()
    }

}