package com.example.task_buddy

import android.app.TimePickerDialog
import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        //Initialise all the item and variable
        val selectTime:FloatingActionButton=findViewById(R.id.timeSelectButton)
        val showTime:TextView=findViewById(R.id.showTime)
        val UID:String= FirebaseAuth.getInstance().currentUser?.uid.toString()
        val AddTaskButton:Button=findViewById(R.id.AddTaskButton)
        val TaskName:TextView=findViewById(R.id.EnterTaskName)
        val TaskDetails:TextView=findViewById(R.id.EnterTaskDetails)
        val db = Firebase.firestore


        //Time Picker
        selectTime.setOnClickListener {
            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting our hour, minute.
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // on below line we are initializing
            // our Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                { _, hourOfDay, minuteOfHour ->
                    // Format the selected time in 12-hour format with AM/PM
                    val selectedTime = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minuteOfHour)
                    }
                    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    showTime.text = timeFormat.format(selectedTime.time)
                },
                hour,
                minute,
                false // Use 12-hour format
            )

            // Show the TimePickerDialog
            timePickerDialog.show()
        }

        AddTaskButton.setOnClickListener{
            val user = hashMapOf(           //create a HashMap of all the data to Save
                "task_name" to "${TaskName.text}",
                "task_details" to "${TaskDetails.text}",
                "task_due_time" to "${showTime.text}",
                "task_status" to "Incomplete",
                "user_id" to UID,
            )

            // Add a new document with a auto generated ID
            db.collection("Tasks_Data")
                .add(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task Save Successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Task Save Failed! Try Again.", Toast.LENGTH_LONG).show()
                }
        }

    }
}