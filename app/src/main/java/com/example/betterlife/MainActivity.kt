package com.example.betterlife

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.betterlife.Pengguna.StartingPengguna
import com.example.betterlife.Psikolog.StartingPsikolog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lanjutSebagaiPengguna = findViewById<Button>(R.id.lanjut_user_main)
        val lanjutSebagaiPsikolog = findViewById<Button>(R.id.lanjut_psikolog_main)

        lanjutSebagaiPengguna.setOnClickListener{
            val intent = Intent(this, StartingPengguna::class.java)
            startActivity(intent)
        }

        lanjutSebagaiPsikolog.setOnClickListener {
            val intent = Intent(this, StartingPsikolog::class.java)
            startActivity(intent)
        }
    }
}