package com.todo.ybpreethishchandra.todo

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.todo.ybpreethishchandra.todo.R.id.gv
import java.util.*

class gvAdapter : BaseAdapter {
    var List = ArrayList<todo>()
    var context: Context? = null

    constructor(context: Context, List: ArrayList<todo>) : super() {
        this.context = context
        this.List = List
    }

    override fun getCount(): Int {
        return List.size
    }

    override fun getItem(position: Int): Any {
        return List[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item= this.List[position]
        var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var View = inflator.inflate(R.layout.gvlayout,null)
        var txt=View.findViewById<TextView>(R.id.tvName)
        var bg=View.findViewById<LinearLayout>(R.id.gvlayoutview)
        bg.setBackgroundColor(Color.parseColor(item.color))
        txt.text= item.title
        View.setOnDragListener(object:View.OnDragListener{
            override fun onDrag(v: View?, event: DragEvent?): Boolean {
                when(event!!.action){
                    DragEvent.ACTION_DRAG_ENDED->{
                        val vi=event.localState as View
                        vi.visibility=android.view.View.VISIBLE
                    }
                    DragEvent.ACTION_DROP ->{
                        val vi=event.localState as View
                        if(v==vi){
                            notifyDataSetChanged()
                            vi.visibility=android.view.View.VISIBLE
                            return true
                        }

                        val txtele=vi.findViewById<TextView>(R.id.tvName)
                        val txtn=txtele.text
                        var selected=0
                        for (j in 0..List.lastIndex)
                            if(List[j].title==txtn)
                                selected=j
                        val target=v
                        var targeti=target!!.findViewById<TextView>(R.id.tvName)
                        var tarname=targeti.text
                        var targettodo=0
                        for (k in 0..List.lastIndex)
                            if(List[k].title==tarname)
                                targettodo=k
                        var remele=List.removeAt(selected)
                        List.add(targettodo,remele)
                        notifyDataSetChanged()
                        vi.visibility=android.view.View.VISIBLE
                    }
                }

                return true
            }

        })
        return View
    }
}
