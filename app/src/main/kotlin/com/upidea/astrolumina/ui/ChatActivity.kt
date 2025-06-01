package com.upidea.astrolumina.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.entity.Message
import com.upidea.astrolumina.data.local.AppDatabase
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {

    private lateinit var messageListView: ListView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val userName = intent.getStringExtra("userName") ?: "Kullanıcı"
        val currentUser = "Ben" // Giriş yapan kullanıcı ismi gelecekte dinamik olabilir

        findViewById<TextView>(R.id.textUserName).text = userName

        messageListView = findViewById(R.id.messageListView)
        messageInput = findViewById(R.id.editTextMessage)
        sendButton = findViewById(R.id.buttonSend)

        chatAdapter = ChatAdapter(this, messageList)
        messageListView.adapter = chatAdapter

        val db = AppDatabase.getDatabase(this)
        val dao = db.messageDao()

        // Önceki mesajları yükle
        lifecycleScope.launch {
            val messages = dao.getChatMessages(currentUser, userName)
            messageList.clear()
            messageList.addAll(messages.map { "${it.sender}: ${it.content}" })
            chatAdapter.notifyDataSetChanged()
            messageListView.smoothScrollToPosition(messageList.size - 1)
        }

        sendButton.setOnClickListener {
            val content = messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                val message = Message(sender = currentUser, receiver = userName, content = content)

                lifecycleScope.launch {
                    dao.insertMessage(message)

                    messageList.add("${message.sender}: ${message.content}")
                    chatAdapter.notifyDataSetChanged()
                    messageInput.setText("")
                    messageListView.smoothScrollToPosition(messageList.size - 1)

                    // Karşıdan sahte bir yanıt ekleyelim (demo amaçlı)
                    val reply = Message(sender = userName, receiver = currentUser, content = "Harika, devam edelim!")
                    dao.insertMessage(reply)

                    messageList.add("${reply.sender}: ${reply.content}")
                    chatAdapter.notifyDataSetChanged()
                    messageListView.smoothScrollToPosition(messageList.size - 1)
                }
            } else {
                Toast.makeText(this, "Lütfen mesaj yazın", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
