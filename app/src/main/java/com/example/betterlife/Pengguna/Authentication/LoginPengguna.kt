package com.example.betterlife.Pengguna.Authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.betterlife.Pengguna.Fragments.HomePengguna
import com.example.betterlife.Pengguna.NavigasiPengguna
import com.example.betterlife.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginPengguna : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_pengguna)
        firebaseAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)

        findViewById<Button>(R.id.buttonLoginPengguna).setOnClickListener {
            validateLoginData()
        }

        findViewById<Button>(R.id.buttonRegisterDariLoginPengguna).setOnClickListener {
            val intent = Intent(this, RegisterPengguna::class.java)
            startActivity(intent)
        }
    }

    private fun validateLoginData() {
        val userNameEditText = findViewById<EditText>(R.id.etUsernameLoginPengguna)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordLoginPengguna)

        val username = findViewById<EditText>(R.id.etUsernameLoginPengguna).text.toString().trim()
        val password = findViewById<EditText>(R.id.etPasswordLoginPengguna).text.toString().trim()

        if (TextUtils.isEmpty(username)) {
            userNameEditText.error = "Masukkan Username!"
        } else if (TextUtils.isEmpty(password)) {
            passwordEditText.error = "Masukkan Password!"
        } else {
            doLogin(username, password)
        }
    }

    private fun doLogin(username: String, password: String) {
        val document = fStore.collection("users").document(username)
        if (document != null) {
            document.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if (document.getString("email") != null) {
                            var emailUser = document.getString("email")!!
                            firebaseAuth.signInWithEmailAndPassword(emailUser, password)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Berhasil Login", Toast.LENGTH_LONG).show()
                                    redirectToHome(username)
                                }
                        } else {
                            Toast.makeText(this, "Username tidak ditemukan!", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "Username tidak ditemukan!", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Gagal karena ${exception}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun redirectToHome(username: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("USERNAME_KEY", username)
        editor.apply()

        val intent = Intent(this, NavigasiPengguna::class.java)
        intent.putExtra("Username", username)
        startActivity(intent)
        finish()
    }
}