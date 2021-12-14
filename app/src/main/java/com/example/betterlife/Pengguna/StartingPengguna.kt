package com.example.betterlife.Pengguna

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.betterlife.Pengguna.Authentication.LoginPengguna
import com.example.betterlife.Pengguna.Authentication.RegisterPengguna
import com.example.betterlife.R
import com.google.firebase.auth.FirebaseAuth

class StartingPengguna : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting_pengguna)

        firebaseAuth = FirebaseAuth.getInstance()
        checkLoggedIn()

        findViewById<Button>(R.id.daftar_akun_main_pengguna).setOnClickListener {
            val intent = Intent(this, RegisterPengguna::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.masuk_main_pengguna).setOnClickListener {
            val intent = Intent(this, LoginPengguna::class.java)
            startActivity(intent)
        }
    }

    private fun checkLoggedIn() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            startActivity(Intent(this, NavigasiPengguna::class.java))
            finish()
        }
    }
}