package com.example.betterlife.Pengguna

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import com.example.betterlife.Pengguna.Authentication.LoginPengguna
import com.example.betterlife.Pengguna.Fragments.HomePengguna
import com.example.betterlife.Pengguna.Fragments.ProfilPengguna
import com.example.betterlife.R
import com.google.firebase.auth.FirebaseAuth

class NavigasiPengguna : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigasi_pengguna)

        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        firebaseAuth = FirebaseAuth.getInstance()

        logoutChecker()

        val fragmentHome = HomePengguna()
        val fragmentProfil = ProfilPengguna()
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutPengguna, fragmentHome)
            commit()
        }

        val gridLayoutHome = findViewById<GridLayout>(R.id.gridLayoutHomePengguna)
        gridLayoutHome.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutPengguna, fragmentHome)
                commit()
            }
        }

        val gridLayoutProfil = findViewById<GridLayout>(R.id.gridLayoutProfilPengguna)
        gridLayoutProfil.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayoutPengguna, fragmentProfil)
                commit()
            }
        }
    }

    private fun logoutChecker() {
        if (sharedPreferences.getString("USERNAME_KEY", "0") == null || firebaseAuth.currentUser == null) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.remove("USERNAME_KEY")
            firebaseAuth.signOut()
            val intent = Intent(this, LoginPengguna::class.java)
            startActivity(intent)
            finish()
        }
    }
}