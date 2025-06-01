package com.upidea.astrolumina.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upidea.astrolumina.R
import com.upidea.astrolumina.data.local.entity.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val currentUser: String,
    private val messages: List<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == currentUser) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_sent, parent, false)
            SentMessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_message_received, parent, false)
            ReceivedMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        val timeFormatted = formatTimestamp(message.timestamp)

        when (holder) {
            is SentMessageViewHolder -> {
                holder.messageText.text = message.content
                holder.messageTime.text = timeFormatted
            }
            is ReceivedMessageViewHolder -> {
                holder.messageText.text = message.content
                holder.messageTime.text = timeFormatted
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    class SentMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.textMessageContent)
        val messageTime: TextView = view.findViewById(R.id.textMessageTime)
    }

    class ReceivedMessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageText: TextView = view.findViewById(R.id.textMessageContent)
        val messageTime: TextView = view.findViewById(R.id.textMessageTime)
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
