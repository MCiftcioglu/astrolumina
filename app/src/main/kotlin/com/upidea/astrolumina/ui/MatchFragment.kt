package com.upidea.astrolumina.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.AppDatabase
import com.upidea.astrolumina.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserListAdapter

    // Uyum tablosu
    private val elementUyumu = mapOf(
        "Ateş" to listOf("Ateş", "Hava"),
        "Toprak" to listOf("Toprak", "Su"),
        "Hava" to listOf("Hava", "Ateş"),
        "Su" to listOf("Su", "Toprak")
    )

    private val burcElementleri = mapOf(
        "Koç" to "Ateş",
        "Boğa" to "Toprak",
        "İkizler" to "Hava",
        "Yengeç" to "Su",
        "Aslan" to "Ateş",
        "Başak" to "Toprak",
        "Terazi" to "Hava",
        "Akrep" to "Su",
        "Yay" to "Ateş",
        "Oğlak" to "Toprak",
        "Kova" to "Hava",
        "Balık" to "Su"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_match, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadUsersFromDatabase()

        return view
    }

    private fun loadUsersFromDatabase() {
        val kullanicininBurcu = "Koç"
        val kullanicininElementi = burcElementleri[kullanicininBurcu] ?: "Ateş"
        val uyumluElementler = elementUyumu[kullanicininElementi] ?: listOf("Ateş", "Hava")

        lifecycleScope.launch(Dispatchers.IO) {
            val userDao = AppDatabase.getDatabase(requireContext()).userDao()
            val allUsers = userDao.getAllUsers()

            val uyumluKullanicilar = allUsers.filter { user ->
                val element = burcElementleri[user.sunSign] ?: "Ateş"
                uyumluElementler.contains(element)
            }

            withContext(Dispatchers.Main) {
                adapter = UserListAdapter(uyumluKullanicilar) { selectedUser ->
                    val intent = Intent(requireContext(), ChatActivity::class.java)
                    intent.putExtra("userName", selectedUser.name)
                    startActivity(intent)
                }
                recyclerView.adapter = adapter
            }
        }
    }
}
