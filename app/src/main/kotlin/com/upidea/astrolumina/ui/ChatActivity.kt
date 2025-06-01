package com.upidea.astrolumina.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.AppDatabase
import com.upidea.astrolumina.data.local.entity.Message
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatAdapter: ChatAdapter

    private val messageList = mutableListOf<Message>()
    private lateinit var currentUser: String
    private lateinit var userName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.messageRecyclerView)
        messageInput = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        currentUser = "Ben"
        userName = intent.getStringExtra("userName") ?: "Kullanıcı"

        chatAdapter = ChatAdapter(currentUser, messageList)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dao = AppDatabase.getDatabase(this).messageDao()

        lifecycleScope.launch {
            val messages = dao.getChatMessages(currentUser, userName)
            messageList.clear()
            messageList.addAll(messages)
            chatAdapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(messageList.size - 1)
        }

        sendButton.setOnClickListener {
            val content = messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                val message = Message(
                    sender = currentUser,
                    receiver = userName,
                    content = content,
                    timestamp = System.currentTimeMillis()
                )

                lifecycleScope.launch {
                    dao.insertMessage(message)
                    messageList.add(message)
                    chatAdapter.notifyItemInserted(messageList.size - 1)
                    recyclerView.scrollToPosition(messageList.size - 1)
                    messageInput.setText("")

                    // Demo yanıt
                    val reply = Message(
                        sender = userName,
                        receiver = currentUser,
                        content = "Harika, devam edelim!",
                        timestamp = System.currentTimeMillis()
                    )
                    dao.insertMessage(reply)
                    messageList.add(reply)
                    chatAdapter.notifyItemInserted(messageList.size - 1)
                    recyclerView.scrollToPosition(messageList.size - 1)
                }
            } else {
                Toast.makeText(this, "Lütfen mesaj yazın", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
