package es.ilerna.proyectodam.vehiclegest.backend

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import es.ilerna.proyectodam.vehiclegest.ui.MainActivity


/*class UpdateCourse : AppCompatActivity() {
    // creating variables for our edit text
    private var courseNameEdt: EditText? = null
    private var courseDurationEdt: EditText? = null
    private var courseDescriptionEdt: EditText? = null

    // creating a strings for storing our values from Edittext fields.
    private var courseName: String? = null
    private var courseDuration: String? = null
    private var courseDescription: String? = null

    // creating a variable for firebasefirestore.
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_course)
        val courses: Courses? = intent.getSerializableExtra("course") as Courses?

        // getting our instance from Firebase Firestore.
        db = FirebaseFirestore.getInstance()

        // initializing our edittext and buttons
        courseNameEdt = findViewById(R.id.idEdtCourseName)
        courseDescriptionEdt = findViewById(R.id.idEdtCourseDescription)
        courseDurationEdt = findViewById(R.id.idEdtCourseDuration)

        // creating variable for button
        val updateCOurseBtn = findViewById<Button>(R.id.idBtnUpdateCourse)
        val deleteBtn = findViewById<Button>(R.id.idBtnDeleteCourse)
        courseNameEdt.setText(courses.getCourseName())
        courseDescriptionEdt.setText(courses.getCourseDescription())
        courseDurationEdt.setText(courses.getCourseDuration())

        // adding on click listener for delete button
        deleteBtn.setOnClickListener { // calling method to delete the course.
            deleteCourse(courses)
        }
        updateCOurseBtn.setOnClickListener {
            courseName = courseNameEdt.getText().toString()
            courseDescription = courseDescriptionEdt.getText().toString()
            courseDuration = courseDurationEdt.getText().toString()

            // validating the text fields if empty or not.
            if (TextUtils.isEmpty(courseName)) {
                courseNameEdt.setError("Please enter Course Name")
            } else if (TextUtils.isEmpty(courseDescription)) {
                courseDescriptionEdt.setError("Please enter Course Description")
            } else if (TextUtils.isEmpty(courseDuration)) {
                courseDurationEdt.setError("Please enter Course Duration")
            } else {
                // calling a method to update our course.
                // we are passing our object class, course name,
                // course description and course duration from our edittext field.
                updateCourses(courses, courseName!!, courseDescription!!, courseDuration!!)
            }
        }
    }

  *//*  private fun deleteCourse(courses: Courses?) {
        // below line is for getting the collection
        // where we are storing our courses.
        db!!.collection("Courses").document
            // after that we are getting the document
        // which we have to delete.
        (courses.getId()).delete // after passing the document id we are calling
        // delete method to delete this document.
        ().addOnCompleteListener // after deleting call on complete listener
        // method to delete this data.
        OnCompleteListener<Void?> { task ->
            // inside on complete method we are checking
            // if the task is success or not.
            if (task.isSuccessful) {
                // this method is called when the task is success
                // after deleting we are starting our MainActivity.
                Toast.makeText(
                    this@UpdateCourse,
                    "Course has been deleted from Database.",
                    Toast.LENGTH_SHORT
                ).show()
                val i = Intent(this@UpdateCourse, MainActivity::class.java)
                startActivity(i)
            } else {
                // if the delete operation is failed
                // we are displaying a toast message.
                Toast.makeText(this@UpdateCourse, "Fail to delete the course. ", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
*//*
    private fun updateCourses(
        courses: Courses?,
        courseName: String,
        courseDescription: String,
        courseDuration: String
    ) {
        // inside this method we are passing our updated values
        // inside our object class and later on we
        // will pass our whole object to firebase Firestore.
        val updatedCourse = Courses(courseName, courseDescription, courseDuration)

        // after passing data to object class we are
        // sending it to firebase with specific document id.
        // below line is use to get the collection of our Firebase Firestore.
        db!!.collection("Courses").document // below line is use toset the id of
        // document where we have to perform
        // update operation.
        courses.getId().set // after setting our document id we are
        // passing our whole object class to it.
        updatedCourse.addOnSuccessListener // after passing our object class we are
        // calling a method for on success listener.
        OnSuccessListener<Void?> { // on successful completion of this process
            // we are displaying the toast message.
            Toast.makeText(this@UpdateCourse, "Course has been updated..", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener(OnFailureListener
        // inside on failure method we are
        // displaying a failure message.
        {
            Toast.makeText(this@UpdateCourse, "Fail to update the data..", Toast.LENGTH_SHORT)
                .show()
 }       })
    }*/

