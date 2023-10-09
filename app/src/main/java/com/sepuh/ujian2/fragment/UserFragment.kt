package com.sepuh.ujian2.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sepuh.ujian2.R
import com.sepuh.ujian2.adapter.UserAdapter
import com.sepuh.ujian2.model.User
import com.sepuh.ujian2.services.ApiService
import com.sepuh.ujian2.services.ApiServiceFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UserFragment : Fragment(),UserAdapter.OnDeleteClickListener, UserAdapter.OnEditClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserAdapter
    private lateinit var btnAddUser: FloatingActionButton
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText


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
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = UserAdapter()
        recyclerView.adapter = adapter

        setHasOptionsMenu(true)

        adapter.setOnDeleteClickListener(this)
        adapter.setOnEditClickListener(this)

        btnAddUser = view.findViewById(R.id.addUser)
        btnAddUser.setOnClickListener {
            // Menampilkan fragment AddUserFragment saat tombol "Tambah User" ditekan
            val addUserFragment = AddData()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, addUserFragment)
            transaction.addToBackStack(null) // Tambahkan ke back stack agar bisa kembali ke fragment sebelumnya
            transaction.commit()
        }

        searchButton = view.findViewById(R.id.btnSearch)
        searchEditText = view.findViewById(R.id.search)

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                searchUsers(query)
            } else {
                Toast.makeText(requireContext(), "Masukkan kata kunci pencarian", Toast.LENGTH_SHORT).show()
            }
        }

        val apiService = ApiServiceFactory.create()
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        adapter.setUsers(users)
                    }
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
            }
        })

        return view

    }

    private fun loadDataUser() {
        // Panggil API untuk mendapatkan data pengguna
        val apiService = ApiServiceFactory.create()
        apiService.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        adapter.setUsers(users)
                    }
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Handle kesalahan jika terjadi
                Toast.makeText(requireContext(), "Terjadi kesalahan saat mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchUsers(query: String) {
        val apiService = ApiServiceFactory.create()
        apiService.searchUsers(query).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    val users = response.body()
                    if (users != null) {
                        adapter.setUsers(users)
                    }
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                // Handle kesalahan saat permintaan pencarian gagal
                Toast.makeText(requireContext(), "Terjadi kesalahan saat mencari data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDeleteClick(user: User) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Hapus")
            .setMessage("Apakah Anda yakin ingin menghapus pengguna ini?")
            .setPositiveButton("Ya") { _, _ ->
                hapusDataUser(user.id)
            }
            .setNegativeButton("Batal", null)
            .create()

        alertDialog.show()
    }

    override fun onEditClick(user: User) {
        val bundle = Bundle()
        bundle.putSerializable("user", user)
        bundle.putSerializable("userID", user.id)

        val addUserFragment = AddData()
        addUserFragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, addUserFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun hapusDataUser(id: Int) {
        val apiService = ApiServiceFactory.create()

        apiService.hapusUser(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    // Data berhasil dihapus dari API
                    // Munculkan pesan sukses atau lakukan tindakan lain yang diperlukan
                    Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()

                    // Refresh daftar pengguna setelah penghapusan berhasil
                    loadDataUser()
                } else {
                    // Penanganan kesalahan jika API merespons dengan kesalahan
                    Toast.makeText(requireContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                // Handle kesalahan jika pemanggilan API gagal
                Toast.makeText(requireContext(), "Terjadi kesalahan saat menghapus data", Toast.LENGTH_SHORT).show()
            }
        })
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}