package com.apusx.lab2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.apusx.lab2.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // ...
        // Initialize Firebase Auth
        auth = Firebase.auth

        val sharedPref = getSharedPreferences(
            "AUTH", Context.MODE_PRIVATE)

        binding.signinbtn.setOnClickListener{
            startActivity(Intent(this,SignUp::class.java))
        }

        binding.signupbtn.setOnClickListener{
            val email: String = binding.email.text.toString().trim()
            val password: String = binding.password.text.toString().trim()

            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Заполни поля для ввода данных", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {

                            with (sharedPref.edit()) {
                                putString("password", password)
                                apply()
                                Log.d("Firebase", "signInWithEmail:success")
                                startActivity(Intent(applicationContext,HomeActivity::class.java))
                                finish()
                            }

                        } else {
                            Log.w(
                                "Firebase",
                                "signInWithEmail:failure " + task.exception?.message.toString()
                            )
                        }
                    }
            }

        }

    }



    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            // TO-DO go to next activity
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

}