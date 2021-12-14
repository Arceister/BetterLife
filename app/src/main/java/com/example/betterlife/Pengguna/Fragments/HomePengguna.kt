package com.example.betterlife.Pengguna.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.betterlife.Pengguna.Authentication.LoginPengguna
import com.example.betterlife.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePengguna.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePengguna: Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        firebaseAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        sharedPreferences = activity?.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home_pengguna, container, false)

        val userNameMessage = sharedPreferences.getString("USERNAME_KEY", "0")

        val documentLoad = fStore.collection("users").document(userNameMessage!!)
        if (documentLoad != null) {
            documentLoad.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        view.findViewById<TextView>(R.id.tv_nama_pengguna_home).text = document.getString("nama")!!
                            .split(" ")[0]
                        view.findViewById<TextView>(R.id.tv_nama_bukan_pengguna_home).text = (document.getString("nama")!!.split(" ")[0]) + "?"
                    }
                    Log.d("Gagal", "No such document")
                }
        }

        view.findViewById<Button>(R.id.button_logout_pengguna_home).setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.remove("USERNAME_KEY")
            editor.apply()

            firebaseAuth.signOut()
            val intent = Intent(activity, LoginPengguna::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePengguna.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePengguna().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}