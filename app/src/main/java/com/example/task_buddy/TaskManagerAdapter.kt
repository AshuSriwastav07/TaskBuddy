package com.example.task_buddy

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.task_buddy.DataManage.DataModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore


class TaskManagerAdapter(private var taskDataSet: ArrayList<DataModel>) :
    RecyclerView.Adapter<TaskManagerAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            //Initialise all the variable and item
        var taskName: TextView = view.findViewById(R.id.taskName)
        var taskDetails: TextView = view.findViewById(R.id.taskDetails)
        var taskStatus: TextView = view.findViewById(R.id.taskStatus)
        var taskDueTime: TextView = view.findViewById(R.id.taskDueTime)
        var TaskCardView:CardView=view.findViewById(R.id.taskCardView)
        var openTaskButton: FloatingActionButton = view.findViewById(R.id.openTaskButton)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.task_item_layout, viewGroup, false)  //set item layout int recyclerview

        return ViewHolder(view)
    }


    // set All the fetch data in item view
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val task = taskDataSet[position]
        viewHolder.taskName.text = task.task_name
        viewHolder.taskDetails.text = task.task_data
        viewHolder.taskStatus.text = task.task_status
        viewHolder.taskDueTime.text = "Task Due Time : " + task.taskDueTime

        //if Task is Complete set BG color To green
        if(task.task_status=="Complete"){
            viewHolder.TaskCardView.setCardBackgroundColor(ContextCompat.getColor(viewHolder.itemView.context, R.color.taskComplete))
        }

        //click on floating button to edit delete or update task

        viewHolder.openTaskButton.setOnClickListener{

            val popup = PopupMenu( viewHolder.itemView.context,viewHolder.openTaskButton)  //popup menu on button click
            popup.inflate(R.menu.task_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) { // get Item ID
                    R.id.action_edit -> {  //If Item ID match with this edit
                        val intent= Intent(viewHolder.itemView.context,TaskEditPage::class.java)  //send user to edit task page
                        intent.putExtra("TaskID",task.task_id)  //with task ID
                        viewHolder.itemView.context.startActivity(intent)
                        true
                    }
                    R.id.action_delete -> {  //if ID match with delete
                        FirebaseFirestore.getInstance().collection("Tasks_Data").document(task.task_id).delete().addOnSuccessListener {
                            Toast.makeText(viewHolder.itemView.context,"Task Deleted Successfully!", Toast.LENGTH_LONG).show()  //Delete task from firestore using .delete() and show Toast
                        }
                        true
                    }
                    R.id.action_complete -> {  // if ID match with complete call makeTaskComplete Function
                        makeTaskComplete(task.task_id,viewHolder.itemView.context)
                        true
                    }
                    else -> false
                }
            }

            popup.show() //show menu on click

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = taskDataSet.size

    // check Task status
    fun makeTaskComplete(taskID:String,view:Context){
        val updatedData = hashMapOf<String, Any>(
           "task_status" to "Complete"
        )

        val firestore = FirebaseFirestore.getInstance().collection("Tasks_Data").document(taskID) //user taskID and update task status in firestore
        firestore.update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(view , "Task Complete", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(view, "Task Edit Failed! Try Again.", Toast.LENGTH_LONG).show()
            }

        }

    fun updateData(newTaskDataSet: ArrayList<DataModel>) {  // show task list after search
        taskDataSet = newTaskDataSet
        notifyDataSetChanged()
    }
    }

