package com.example.betterlife.Pengguna.Fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.betterlife.Pengguna.Fragments.UbahData.UbahDataAnak
import com.example.betterlife.Pengguna.Fragments.UbahData.UbahDataWali
import com.example.betterlife.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfilPengguna.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfilPengguna : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_profil_pengguna, container, false)

        val userName = sharedPreferences.getString("USERNAME_KEY", "0")

        val documentLoad = fStore.collection("users").document(userName!!)
        if (documentLoad != null) {
            documentLoad.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        view.findViewById<TextView>(R.id.tv_spec_email_ll_pengguna).text = document.getString("email")
                        view.findViewById<TextView>(R.id.tv_spec_username_ll_pengguna).text = document.getString("username")
                        view.findViewById<TextView>(R.id.tv_spec_nama_anak_ll_pengguna).text = document.getString("nama_anak")
                        view.findViewById<TextView>(R.id.tv_spec_nohp_anak_ll_pengguna).text = document.getString("nohp_anak")
                        view.findViewById<TextView>(R.id.tv_spec_nama_wali_ll_pengguna).text = document.getString("nama")
                        view.findViewById<TextView>(R.id.tv_spec_nohp_wali_ll_pengguna).text = document.getString("nohp_wali")
                    }
                    Log.d("Gagal", "No such document")
                }
        }

        view.findViewById<Button>(R.id.button_profile_ubah_data_anak_pengguna).setOnClickListener {
            val ubahDataAnak = UbahDataAnak()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayoutPengguna, ubahDataAnak)
            transaction?.commit()
        }

        view.findViewById<Button>(R.id.button_profile_ubah_data_wali_pengguna).setOnClickListener {
            val ubahDataWali = UbahDataWali()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayoutPengguna, ubahDataWali)
            transaction?.commit()
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
         * @return A new instance of fragment ProfilPengguna.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfilPengguna().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}