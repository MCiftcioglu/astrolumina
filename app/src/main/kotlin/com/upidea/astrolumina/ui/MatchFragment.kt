package com.upidea.astrolumina.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.User
import com.upidea.astrolumina.ui.ChatActivity
import com.upidea.astrolumina.ui.UserListAdapter

class MatchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: UserListAdapter

    private val sampleUsers = listOf(
        User(1, "Zeynep", "Koç", true),
        User(2, "Deniz", "Aslan", true),
        User(3, "Mert", "Boğa", false),
        User(4, "Selin", "Yay", true),
        User(5, "Burak", "Başak", false),
        User(6, "Ezgi", "İkizler", true)
    )

    private val burcElementleri = mapOf(
        "Koç" to "Ateş", "Aslan" to "Ateş", "Yay" to "Ateş",
        "Boğa" to "Toprak", "Başak" to "Toprak", "Oğlak" to "Toprak",
        "İkizler" to "Hava", "Terazi" to "Hava", "Kova" to "Hava",
        "Yengeç" to "Su", "Akrep" to "Su", "Balık" to "Su"
    )

    private val uyumHaritasi = mapOf(
        "Ateş" to listOf("Ateş", "Hava"),
        "Toprak" to listOf("Toprak", "Su"),
        "Hava" to listOf("Hava", "Ateş"),
        "Su" to listOf("Su", "Toprak")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_match, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewUsers)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val sharedPref = requireActivity().getSharedPreferences("AstroPrefs", Context.MODE_PRIVATE)
        val mySunSign = sharedPref.getString("sunSign", "Koç") ?: "Koç"
        val myElement = burcElementleri[mySunSign] ?: "Ateş"
        val uyumluElementler = uyumHaritasi[myElement] ?: listOf("Ateş")

        val filtrelenmisKullanicilar = sampleUsers.filter {
            val kullaniciElement = burcElementleri[it.sunSign] ?: "Ateş"
            uyumluElementler.contains(kullaniciElement)
        }

        adapter = UserListAdapter(filtrelenmisKullanicilar) { selectedUser ->
            val intent = Intent(requireContext(), ChatActivity::class.java)
            intent.putExtra("userName", selectedUser.name)
            startActivity(intent)
            Toast.makeText(requireContext(), "${selectedUser.name} ile kozmik uyum yakaladınız!", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        return view
    }
}
