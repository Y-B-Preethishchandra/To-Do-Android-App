package com.todo.ybpreethishchandra.todo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class lvAdapter(context: Context, cat:ArrayList<String>): BaseAdapter(){
    private val  mContext: Context =context
    private val al=cat
    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        val layoutInflater= LayoutInflater.from(mContext)
        val rowMain=layoutInflater.inflate(R.layout.row,viewGroup,false)
        val textV: TextView =rowMain.findViewById(R.id.CategoryName)
        textV.text=al.get(position)
        return rowMain
    }
    override fun getItem(position: Int): Any {
        return al[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getCount(): Int {
        return al.size
    }
}