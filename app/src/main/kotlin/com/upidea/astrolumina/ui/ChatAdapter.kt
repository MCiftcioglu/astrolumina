package com.upidea.astrolumina.ui


import android.content.Context
import android.view.*
import android.widget.*
import com.upidea.astrolumina.R

class ChatAdapter(private val context: Context, private val messages: List<String>) : BaseAdapter() {

    override fun getCount(): Int = messages.size
    override fun getItem(position: Int): Any = messages[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)  // ← Bu satır düzeltildi
        val textView = view.findViewById<TextView>(android.R.id.text1)    // ← Bu satır da düzeltildi
        textView.text = messages[position]
        return view
    }

}
