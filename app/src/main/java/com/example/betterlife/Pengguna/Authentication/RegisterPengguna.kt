package com.example.betterlife.Pengguna.Authentication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.betterlife.Pengguna.NavigasiPengguna
import com.example.betterlife.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterPengguna : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences

    private var nama_pengguna = ""
    private var username_pengguna = ""
    private var email_pengguna = ""
    private var password_pengguna = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_pengguna)

        firebaseAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()

        sharedPreferences = getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)

        findViewById<Button>(R.id.buttonRegisterPengguna).setOnClickListener {
            validasiData()
        }
    }

    private fun validasiData() {
        val nameEditText = findViewById<EditText>(R.id.etNamaRegPengguna)
        val userNameEditText = findViewById<EditText>(R.id.etUsernameRegPengguna)
        val emailEditText = findViewById<EditText>(R.id.etEmailRegPengguna)
        val passwordEditText = findViewById<EditText>(R.id.etPasswordRegPengguna)

        nama_pengguna = nameEditText.text.toString().trim()
        username_pengguna = userNameEditText.text.toString().trim()
        email_pengguna = emailEditText.text.toString().trim()
        password_pengguna = passwordEditText.text.toString().trim()

        if (TextUtils.isEmpty(nama_pengguna)) {
            nameEditText.error = "Masukkan Nama!"
        } else if (TextUtils.isEmpty(username_pengguna)) {
            userNameEditText.error = "Masukkan Username!"
        } else if (username_pengguna.length < 6) {
            userNameEditText.error = "Username harus lebih dari 6 Karakter!"
        } else if (username_pengguna.length > 12) {
            userNameEditText.error = "Username harus kurang dari 12 Karakter!"
        } else if (!(username_pengguna).matches(Regex("[A-Za-z0-9]*"))) {
            userNameEditText.error = "Username harus berupa alphanumeric!"
        } else if (TextUtils.isEmpty(email_pengguna)) {
            emailEditText.error = "Maukkan Email!"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email_pengguna).matches()) {
            emailEditText.error = "Email tidak sesuai format!"
        } else if (TextUtils.isEmpty(password_pengguna)) {
            passwordEditText.error = "Masukkan Password!"
        } else if (password_pengguna.length < 6) {
            passwordEditText.error = "Password harus lebih dari 6 Karakter!"
        } else if (password_pengguna.length > 12) {
            passwordEditText.error = "Password harus kurang dari 12 Karakter!"
        } else if (!(password_pengguna).matches(Regex("[A-Za-z0-9]*"))) {
            passwordEditText.error = "Password harus berupa alphanumeric!"
        } else {
            checkUsernameExists(nama_pengguna, username_pengguna, email_pengguna, password_pengguna)
        }
    }

    private fun checkUsernameExists(nama: String, username: String, email: String, password: String) {
        var documentUserName = fStore.collection("users").document(username)
        documentUserName.get()
            .addOnSuccessListener { document ->
                if (document.getString("email") != null) {
                    Toast.makeText(this, "Username already exists!", Toast.LENGTH_LONG).show()
                } else {
                    registerAccount(nama, username, email, password)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Gagal karena ${exception}", Toast.LENGTH_LONG).show()
            }
    }

    private fun registerAccount(nama: String, username: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val userEmail = firebaseUser!!.email
                val userId = firebaseUser!!.uid
                if (userEmail != null) {
                    inputDataToFirestore(userId, nama, username.lowercase(), userEmail, password)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Pesan Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun inputDataToFirestore(userId: String, nama: String, username: String, email: String, password: String) {
        var userData = hashMapOf(
            "userId" to userId,
            "nama" to nama,
            "username" to username,
            "email" to email,
            "password" to password,
            "nama_wali" to "-",
            "nohp_wali" to "-",
            "nama_anak" to "-",
            "nohp_anak" to "-"
        )

        fStore.collection("users").document(username)
            .set(userData)
            .addOnSuccessListener {
                Log.d("Sukses", "Pendaftaran Sukses")
                redirectToHome(username)
            }
            .addOnFailureListener { e ->
                Log.w("Gagal", "Error", e)
            }
    }

    private fun redirectToHome(username: String) {
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("USERNAME_KEY", username)
        editor.apply()

        Toast.makeText(this, "Login Sukses", Toast.LENGTH_LONG).show()
        val intent = Intent(this, NavigasiPengguna::class.java)
        startActivity(intent)
        finish()
    }
}