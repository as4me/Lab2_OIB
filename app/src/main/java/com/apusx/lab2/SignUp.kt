package com.apusx.lab2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.apusx.lab2.databinding.ActivityHomeBinding
import com.apusx.lab2.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth
        val db = Firebase.firestore

        val sharedPref = getSharedPreferences(
            "AUTH", Context.MODE_PRIVATE)

        binding.buttonSignup.setOnClickListener {
            val email: String = binding.nameSignup.text.toString().trim()
            val password: String = binding.passwordSignup.text.toString().trim()

            if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Заполни поля для ввода данных", Toast.LENGTH_SHORT).show()
            } else {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            with (sharedPref.edit()) {
                                putString("password", password)
                                apply()
                                Log.d("Firebase", "createUserWithEmail:success")
                                db.collection("users")
                                    .add(User(email,task.result?.user?.uid.toString(),binding.fioSignup.text.trim().toString(),binding.fioSignup3.text.trim().toString()
                                        ,binding.fioSignup2.text.trim().toString(),binding.fioSignup4.text.trim().toString(),binding.fioSignup6.text.trim().toString(),
                                        binding.fioSignup5.text.trim().toString()))
                                    .addOnSuccessListener {
                                            documentReference ->
                                        Log.d("Firebase", "DocumentSnapshot written with ID: ${documentReference.id}")
                                    }
                                startActivity(Intent(applicationContext,HomeActivity::class.java))
                                finish()
                            }


                        } else {
                            Log.d("Firebase", "createUserWithEmail:fail")
                        }
                    }

            }

        }


        binding.back.setOnClickListener {
            finish()
        }
    }
}