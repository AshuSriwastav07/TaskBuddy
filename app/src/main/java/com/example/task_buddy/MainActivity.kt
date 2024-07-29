package com.example.task_buddy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.task_buddy.DataManage.DataModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.net.URL

class MainActivity : AppCompatActivity() {

    // Declare all the variables and item
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: TaskManagerAdapter
    private lateinit var taskDataSet: ArrayList<DataModel>
    private lateinit var db: FirebaseFirestore
    private lateinit var taskListener: ListenerRegistration
    private val TAG = "FireStoreData"
    val UID:String= FirebaseAuth.getInstance().currentUser?.uid.toString()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentUser = auth.currentUser
        if (currentUser == null) { //Check user in Login or not
            reload()
        }

        //Initialise variables an item
        taskRecyclerView = findViewById(R.id.taskRecyclerView)
        taskDataSet = ArrayList()
        val viewManager = LinearLayoutManager(this)
        val addTaskButton: FloatingActionButton = findViewById(R.id.floatingActionButton)
        db = FirebaseFirestore.getInstance()
        val userDisplayName:TextView=findViewById(R.id.userName)
        val userProfileImage:ImageView=findViewById(R.id.userProfileImage)
        val imageURL: String = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
        val userName:String=FirebaseAuth.getInstance().currentUser?.displayName.toString()
        var searchView:SearchView=findViewById(R.id.searchView)


        //Run Query to search specific Task
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText) // call filterData Function with newText Parameter
                return false
            }
        })


        //sent User to Add Task Page
        addTaskButton.setOnClickListener {
            val intent = Intent(this, AddTask::class.java)
            startActivity(intent)
        }


        //Check URL Image is null or not (if Null set image to gone)
        if(imageURL=="null" || imageURL == ""){  // Check if ProfileImageURL  is null or Not
            userDisplayName.text = userName
            FirebaseAuth.getInstance().currentUser?.reload()

        }else{
            userProfileImage.visibility=View.VISIBLE
            userDisplayName.text = userName
            Picasso.get().load(imageURL).into(userProfileImage);
            FirebaseAuth.getInstance().currentUser?.reload()
        }

        // Set Recyclerview with adapter and data
        recyclerViewAdapter = TaskManagerAdapter(taskDataSet)
        taskRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = recyclerViewAdapter
        }


        //Setup Logout Option

        userDisplayName.setOnLongClickListener{

            val popupMenu = PopupMenu(this, userDisplayName)
            popupMenu.inflate(R.menu.logout_menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) { // get Item ID
                    R.id.logout -> {
                        Firebase.auth.signOut()
                        reload()
                        Toast.makeText(this,"Log Out Successful",Toast.LENGTH_LONG).show()

                    }
                }

                true
            }

            popupMenu.show()

            true
        }


        // Real-time update listener (It Will update data in Realtime UI)

        taskListener = db.collection("Tasks_Data")  //Get Tasks Data from FireStore
            .whereEqualTo("user_id",UID)                   //Get Login User Data only
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                //Check fetch Data is null or not
                if (snapshots != null) {
                    taskDataSet.clear()     //clear ArrayList
                    for (document in snapshots) {
                        Log.d(TAG, "${document.id} => ${document.data}")

                        val dataModel = DataModel(
                            task_id = document.getString("task_id") ?: document.id,  //get data from document and add into given dataModel var
                            task_name = document.getString("task_name") ?: "",
                            task_data = document.getString("task_details") ?: "",
                            task_status = document.getString("task_status") ?: "",
                            task_time = document.getString("task_due_time") ?: "",
                            user_id = document.getString("user_id") ?: ""

                        )
                        taskDataSet.add(dataModel) //Add data in ArrayList

                    }
                    recyclerViewAdapter.notifyDataSetChanged()
                } else {
                    Log.d(TAG, "Current data: null")
                }
            }

            }

    override fun onDestroy() { // Remove the listener when the activity is destroyed to prevent memory leaks
        super.onDestroy()
        taskListener.remove()
    }

    private fun reload() { //Send user to Login Page when call
        val intent = Intent(this, LogInPage::class.java)
        startActivity(intent)
        finish() // clear backstack
    }

    private fun filterData(query: String?) {  //filter data to show after search
        val filteredList = if (query.isNullOrEmpty()) {  // if search bar is empty or null all task will show
            taskDataSet
        } else {
            val lowerCaseQuery = query.lowercase() //change search bar text in lowe case
            taskDataSet.filter { //Check if Entered TExt is available in taskDataSet or not
                it.task_name.lowercase().contains(lowerCaseQuery) ||
                        it.task_data.lowercase().contains(lowerCaseQuery)
            } as ArrayList<DataModel>
        }
        recyclerViewAdapter.updateData(filteredList)  //sent updates data in adapter view
    }


}
