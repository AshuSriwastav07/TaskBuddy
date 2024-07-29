package com.example.task_buddy

import android.app.ActivityManager.TaskDescription
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task_buddy.DataManage.DataModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class TaskEditPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_edit_page)

        // Initialise and declare variable and item
        val TaskId:String=intent.getStringExtra("TaskID").toString()
        val TaskName:EditText=findViewById(R.id.EnterEditTaskName)
        val TaskDescription:EditText=findViewById(R.id.EnterEditTaskDetails)
        val TaskClock:FloatingActionButton=findViewById(R.id.timeEditSelectButton)
        val TaskTime:TextView=findViewById(R.id.EditshowTime)
        val SaveEditTask:Button=findViewById(R.id.AddEditTaskButton)
        val firestore = FirebaseFirestore.getInstance().collection("Tasks_Data").document(TaskId)  // make firestore Var

        // Get Data from FireStore to show in Edit Task
        firestore.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val dataModel = DataModel(  //set all Data in DataModel
                        task_id = document.getString("task_id") ?: document.id,
                        task_name = document.getString("task_name") ?: "",
                        task_data = document.getString("task_details") ?: "",
                        task_status = document.getString("task_status") ?: "",
                        task_time = document.getString("task_due_time") ?: "",
                        user_id = document.getString("user_id") ?: ""

                    )

                    //Show Data in Data Fields
                    TaskName.setText(dataModel.task_name)
                    TaskDescription.setText(dataModel.task_data)
                    TaskTime.text = dataModel.taskDueTime

                } else {
                    Log.d("FireStore", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("FireStore", "get failed with ", exception)
            }


        //select Task Time
        TaskClock.setOnClickListener {
            // make instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting our hour, minute.
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            // initializing Time Picker Dialog
            val timePickerDialog = TimePickerDialog(
                this,
                { view, hourOfDay, minute ->
                    // on below line we are setting selected
                    // time in our text view.
                    TaskTime.setText("$hourOfDay:$minute")
                },
                hour,
                minute,
                false
            )
            // at last we are calling show to
            // display our time picker dialog.
            timePickerDialog.show()
        }


        //Update Data in Task

        SaveEditTask.setOnClickListener {
            val updatedData = hashMapOf<String, Any>(   //make HashMap and add all updated data
                "task_name" to TaskName.text.toString().trim(),
                "task_due_time" to TaskTime.text.toString().trim(),
                "task_details" to TaskDescription.text.toString().trim(),
                "task_status" to "Incomplete"
            )

            firestore.update(updatedData)  //set updated data in .update() to send in fireStore
                .addOnSuccessListener {
                    Toast.makeText(this, "Task Edited Successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Task Edit Failed! Try Again.", Toast.LENGTH_LONG).show()
                }
        }


    }
}