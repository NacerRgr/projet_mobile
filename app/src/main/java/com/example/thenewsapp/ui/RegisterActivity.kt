package com.example.thenewsapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.thenewsapp.databinding.FragmentArticleBinding
import com.example.thenewsapp.databinding.RegisterBinding
import com.example.thenewsapp.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterBinding
    private lateinit var firebaseDb : FirebaseDatabase
    private lateinit var databaseReference : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=RegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDb = FirebaseDatabase.getInstance()
        databaseReference = firebaseDb.reference.child("users")

        binding.RegisterBtn.setOnClickListener{
            val signupUsername = binding.username.text.toString()
            val signupEmail = binding.email.text.toString()
            val pwd = binding.password.text.toString()

            if(signupEmail.isNotEmpty() && signupEmail.isNotEmpty() && pwd.isNotEmpty()){
                signupUser(signupUsername,pwd,signupEmail)
            }
            else{
                Toast.makeText(this@RegisterActivity,"you should enter all the fields",Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun signupUser(username:String , password:String , email:String)
    {
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    val id = databaseReference.push().key
                    val userData = User(id,username,password ,email)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@RegisterActivity,"Signup Successfully",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity,LoginActivity::class.java))




                }else
                {
                    Toast.makeText(this@RegisterActivity,"User already signup",Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@RegisterActivity,"DB ERROR",Toast.LENGTH_SHORT).show()
            }
        })
    }


}