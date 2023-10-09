package com.sepuh.ujian2.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.sepuh.ujian2.R
import com.sepuh.ujian2.model.User
import com.sepuh.ujian2.services.ApiServiceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddData : Fragment() {
    private lateinit var etNama: EditText
    private lateinit var etAlamat: EditText
    private lateinit var etJumlahOutstanding: EditText
    private lateinit var btnTambahData: Button

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_data, container, false)

        etNama = view.findViewById(R.id.etNama)
        etAlamat = view.findViewById(R.id.etAlamat)
        etJumlahOutstanding = view.findViewById(R.id.etJumlahOutstanding)
        btnTambahData = view.findViewById(R.id.btnTambahData)

        val user = arguments?.getSerializable("user") as? User
        if (user != null) {
            etNama.setText(user.nama)
            etAlamat.setText(user.alamat)
            etJumlahOutstanding.setText(user.jumlah_outstanding.toString())
        }
        btnTambahData.setOnClickListener {
            tambahDataUser()
        }
        return view
    }

    private fun tambahDataUser() {
        val nama = etNama.text.toString()
        val alamat = etAlamat.text.toString()
        val jumlahOutstanding = etJumlahOutstanding.text.toString().toInt()

        val apiService = ApiServiceFactory.create()
        val user = arguments?.getSerializable("user") as? User

        if (user != null) {
            if (user.id > 0) {
                apiService.editUser(user.id, User(user.id, nama, alamat, jumlahOutstanding))
                    .enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    requireContext(),
                                    "Data berhasil diubah",
                                    Toast.LENGTH_SHORT
                                ).show()
                                requireActivity().supportFragmentManager.popBackStack()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Gagal mengubah data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "Terjadi kesalahan saat mengubah data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        } else {
            val newUser = User(0, nama, alamat, jumlahOutstanding) // Set ID pengguna ke 0 di sini
            apiService.addUser(newUser).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Data berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                        val userListFragment = UserFragment()
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, userListFragment)
                            .addToBackStack(null)
                            .commit()
                    } else {
                        // Penanganan kesalahan jika API merespons dengan kesalahan
                        Toast.makeText(
                            requireContext(),
                            "Gagal menyimpan data",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // Handle kesalahan jika pemanggilan API gagal
                    Toast.makeText(
                        requireContext(),
                        "Terjadi kesalahan saat menyimpan data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddData().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}