package com.example.thenewsapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thenewsapp.databinding.LoginBinding
import com.example.thenewsapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var firebaseDb: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDb = FirebaseDatabase.getInstance()
        databaseReference = firebaseDb.reference.child("users")

        binding.loginButton.setOnClickListener {
            val loginUsername = binding.username.text.toString()
            val loginPassword = binding.password.text.toString()

            if (loginUsername.isNotEmpty() && loginPassword.isNotEmpty()) {
                loginUser(loginUsername, loginPassword)
            } else {
                Toast.makeText(this@LoginActivity, "You should enter all the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.redirectSignUp.setOnClickListener {


            startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))

        }
    }

    private fun loginUser(username: String, password: String) {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.pwd == password) {
                            Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, NewsActivity::class.java))
                            finish()
                            return
                        }
                    }
                    Toast.makeText(this@LoginActivity, "Incorrect password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "DB ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
