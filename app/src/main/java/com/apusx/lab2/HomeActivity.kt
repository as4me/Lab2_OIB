package com.apusx.lab2

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.apusx.lab2.databinding.ActivityHomeBinding
import com.apusx.lab2.databinding.ActivityMainBinding
import com.apusx.lab2.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    fun <String> hasDuplicates(arr: Array<String>): Boolean {
        return arr.size != arr.distinct().count();
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val sharedPref = getSharedPreferences(
            "AUTH", Context.MODE_PRIVATE)
        val defaultValue = ""
        val password_old = sharedPref.getString("password", defaultValue)


        Log.d("Test",password_old.toString())
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.s1.setOnClickListener {
            Log.d("Test",password_old.toString())
            val user = Firebase.auth.currentUser
            val newPasswordverif = binding.editTextTextPersonName3.text.trim().toString()
            val newPassword = binding.editTextTextPersonName2.text.trim().toString()


            val chars: List<Char> = newPassword.toList()
            if(hasDuplicates(chars.toTypedArray())){
                Log.d("Test","Dublicate")
            }else{
                Log.d("Test"," Np Dublicate")
                if (binding.t1.text.trim().toString().equals(password_old)){
                    if (newPassword.equals(newPasswordverif)){
                        user!!.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d("Firebase", "User password updated.")
                                    Toast.makeText(this,"User password updated.",Toast.LENGTH_SHORT).show()
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(Intent(this,MainActivity::class.java))
                                }
                            }
                    } else{
                        Toast.makeText(this,"Check Pass",Toast.LENGTH_SHORT).show()
                    }
                } else{
                    Toast.makeText(this,"Check Current Password",Toast.LENGTH_SHORT).show()
                }
            }



        }



        binding.e1.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}