package com.example.task_buddy.DataManage

class DataModel() {
    lateinit var task_id:String
    lateinit var task_name:String
    lateinit var task_data:String
    lateinit var taskDueTime:String
    lateinit var task_status:String
    lateinit var user_id:String


    constructor(task_id: String, task_name: String, task_data: String, task_status: String,task_time:String,user_id:String) : this() {
        this.task_id = task_id
        this.task_name = task_name
        this.task_data = task_data
        this.task_status = task_status
        this.taskDueTime = task_time
        this.user_id = user_id
    }
}