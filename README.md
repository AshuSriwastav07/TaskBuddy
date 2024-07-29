
# Task Buddy

TaskBuddy is an Android application designed to help users manage their tasks efficiently. Users can create, edit, and delete tasks.
Manage and Help user for efficiently.






## Features

- User Registration and Authentication
- Task Creation and Management
- Real-time Search Functionality
- Time Picker Task
# Tech Stack

- **Kotlin**: Programming language used for Android development.
- **Firebase Authentication**: For user registration and authentication.
- **Firebase Firestore**: For storing and retrieving task data.
- **RecyclerView**: For displaying tasks in a list.
- **SearchView**: For filtering tasks.

## Prerequisites

- Android Studio installed on your computer.
- A Firebase project set up with Firebase Authentication and Firestore enabled.
## Open the project in Android Studio:
- Open Android Studio.
- Select Open an existing project.
- Navigate to the cloned repository and select it.

## Configure Firebase:
- Add the google-services.json file to the app directory. You can obtain this file from the Firebase console after setting up your Firebase project.

## Add Dependencies:
- Ensure all required dependencies are added to your build.gradle files. These include Firebase Auth, Firestore, and other Android libraries.

## Project-level build.gradle:


  plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
  }

## App-level build.gradle:

    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)

## Project-level build.gradle:


    defaultConfig {
        applicationId = "com.example.task_buddy"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation ("com.google.android.material:material:1.13.0-alpha04")
    implementation ("com.squareup.picasso:picasso:2.8")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


## Run the project:

- Connect your Android device or start an emulator.
- Click on the Run button in Android Studio to build and run the project.

# Usage
## User Registration and Authentication

- Register:

- Open the app and navigate to the registration screen.
- Enter your email, password, and display name, then click Register.
- The app will create a new user in Firebase and update the user's profile with the display name.

## Login:

- Enter your registered email and password, then click Login.
- The app will authenticate the user and navigate to the main task management screen.

# Task Management
## Create a Task:

- Click on the Add Task button.
- Enter task details including name, description, and due time.
- Click Save to create the task.

## Edit a Task:

- Click on a Task Button on Task and Select Edit option 
- Modify the task details and click Save to update the task.

## 3 Delete a Task:

- Click on Task Button ans Select Delete option

## Taks Complete 
- Click on Taks Button and select Make Complete Option
## Search Functionality
- Use the SearchView at the top of the screen to filter tasks by name or description in real-time.
