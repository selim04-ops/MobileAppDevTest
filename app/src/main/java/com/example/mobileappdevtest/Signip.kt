package com.example.mobileappdevtest

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView

class Signip : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var profileImage: CircleImageView
    private lateinit var useruri:String
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signip)
        val etName = findViewById<EditText>(R.id.et_name)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val checkboxTerms = findViewById<CheckBox>(R.id.checkbox_terms)
        val radioGroupGender = findViewById<RadioGroup>(R.id.radio_group_gender)
        val btnSignUp = findViewById<Button>(R.id.btn_sign_up)
        profileImage = findViewById(R.id.profile_image)
        auth = FirebaseAuth.getInstance()
        profileImage.setOnClickListener {
            // Create an intent to pick an image
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        // Handle sign-up button click
        btnSignUp.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val isTermsAccepted = checkboxTerms.isChecked
            val selectedGenderId = radioGroupGender.checkedRadioButtonId
             // Ensure this is set when the user selects an image
            // Validate fields
            if (name.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!isTermsAccepted) {
                Toast.makeText(this, "Please accept the terms", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val gender = when (selectedGenderId) {
                R.id.rb_male -> "Male"
                R.id.rb_female -> "Female"
                else -> null
            }
            if (gender == null) {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (useruri == null) {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Create user with Firebase Authentication
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener

                        // Upload image to Firebase Storage
                        val storageReference = FirebaseStorage.getInstance().reference
                            .child("UserImages")
                            .child("$userId.jpg")

                        val uploadTask = storageReference.putFile(imageUri)
                        uploadTask.continueWithTask { task ->
                            if (!task.isSuccessful) {
                                task.exception?.let { throw it }
                            }
                            storageReference.downloadUrl
                        }.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val downloadUri = task.result.toString()

                                // Create User object
                                val user = User(userId, name, email, password, gender, downloadUri)

                                // Save user to Firebase Realtime Database
                                val databaseReference = FirebaseDatabase.getInstance().reference.child("Users")
                                    .child(userId)

                                databaseReference.setValue(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this, HomePage::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to save user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            // Get the selected image URI
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                imageUri=selectedImageUri
            }
            useruri=selectedImageUri.toString()
            // Set the image URI to the CircleImageView
            profileImage.setImageURI(selectedImageUri)
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show()
        }
    }


}