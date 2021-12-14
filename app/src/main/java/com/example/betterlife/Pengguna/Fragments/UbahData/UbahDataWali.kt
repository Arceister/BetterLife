package com.example.betterlife.Pengguna.Fragments.UbahData

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.betterlife.Pengguna.Fragments.ProfilPengguna
import com.example.betterlife.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UbahDataWali.newInstance] factory method to
 * create an instance of this fragment.
 */
class UbahDataWali : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_ubah_data_wali, container, false)

        val userName = sharedPreferences.getString("USERNAME_KEY", "0")

        val documentLoad = fStore.collection("users").document(userName!!)
        if (documentLoad != null) {
            documentLoad.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        view.findViewById<EditText>(R.id.et_nama_ubah_data_wali_pengguna).setText(document.getString("nama"))
                        view.findViewById<EditText>(R.id.et_nohp_ubah_data_wali_pengguna).setText(document.getString("nohp_wali"))
                    }
                    Log.d("Gagal", "No such document")
                }
        }

        view.findViewById<Button>(R.id.button_kembali_ubah_data_wali_pengguna).setOnClickListener {
            redirectToProfile()
        }

        view.findViewById<Button>(R.id.button_save_changes_ubah_data_wali_pengguna).setOnClickListener {
            saveChangesToData(userName)
            redirectToProfile()
        }

        return view
    }

    private fun saveChangesToData(username: String) {
        val namaWaliEditText = view?.findViewById<EditText>(R.id.et_nama_ubah_data_wali_pengguna)
        val noHPWaliEditText = view?.findViewById<EditText>(R.id.et_nohp_ubah_data_wali_pengguna)

        var userData = hashMapOf(
            "nama_anak" to namaWaliEditText?.text.toString().trim(),
            "nohp_anak" to noHPWaliEditText?.text.toString().trim()
        )

        fStore.collection("users").document(username)
            .update("nama", namaWaliEditText?.text.toString().trim(),
                "nohp_wali", noHPWaliEditText?.text.toString().trim())
            .addOnSuccessListener {
                Log.d("Sukses", "Pengubahan Sukses")
            }
            .addOnFailureListener { e ->
                Log.w("Gagal", "Error", e)
            }
    }

    private fun redirectToProfile() {
        val profilPengguna = ProfilPengguna()
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.frameLayoutPengguna, profilPengguna)
            commit()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UbahDataWali.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UbahDataWali().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}