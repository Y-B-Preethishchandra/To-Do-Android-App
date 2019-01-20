package com.todo.ybpreethishchandra.todo

import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_grid_view.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class GridView : AppCompatActivity() {
    lateinit var id: String
    lateinit var category: String
    lateinit var adapter: gvAdapter
    var todolist = arrayListOf<todo>()
    lateinit var list : List<String>
    lateinit var datstring:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_view)
        category = intent.getStringExtra("category")
        id =  intent.getStringExtra("id")
        getFromServer()
        supportActionBar?.setTitle(category)
        try {
            var fs: FileInputStream = openFileInput("$id$category")
            var s = fs.readBytes().toString(Charset.defaultCharset())
            list = s.split("\n")
            fs.close()
            for (i in list) {
                var temp = i.split("�")
                var todoinstance = todo(temp[0], temp[1],temp[2])
                todolist.add(todoinstance)
            }
        } catch (e: FileNotFoundException) {
            var fs: FileOutputStream = openFileOutput("$id$category", MODE_PRIVATE)
            fs.write("Nothing Pending!!� ".toByteArray())
            todolist.add(todo("Nothing Pending!!"," ","#faee78"))
            fs.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        adapter = gvAdapter(this, todolist)
        gv.adapter = adapter
        gv.onItemLongClickListener=AdapterView.OnItemLongClickListener { parent, view, position, _ ->
            var clipdata= ClipData.newPlainText("","")
            var dragshadowbuilder= android.view.View.DragShadowBuilder(view)
            view!!.startDrag(clipdata,dragshadowbuilder,view,0)
        }
        gv.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if(todolist.get(position).title!="Nothing Pending!!") {
                var intentobj = Intent(applicationContext, todoView::class.java)
                var bundle=Bundle()
                bundle.putSerializable("todolist",todolist)
                intentobj.putExtras(bundle)
                intentobj.putExtra("title",todolist.get(position).title)
                intentobj.putExtra("content",todolist.get(position).content)
                intentobj.putExtra("position",position)
                intentobj.putExtra("id",id)
                intentobj.putExtra("category",category)
                intentobj.putExtra("password",intent.getStringExtra("password"))
                intentobj.putExtra("name",intent.getStringExtra("name"))
                intentobj.putExtra("email",intent.getStringExtra("email"))
                startActivity(intentobj)
                finish()
            }
        }
    }

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    var inflater=menuInflater
    inflater.inflate(R.menu.main_menu,menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when(item?.itemId){
        R.id.signOut->{
            try{
                var fs: FileOutputStream =openFileOutput("remember", AppCompatActivity.MODE_PRIVATE)
                fs.write("".toByteArray())
                val intent: Intent = Intent(applicationContext,Login::class.java)
                startActivity(intent)
                finish()
            }
            catch(e: FileNotFoundException){
                e.printStackTrace()
            }
            catch(e: IOException){
                e.printStackTrace()
            }
        }
        R.id.menu_add->{
            var intentobj = Intent(applicationContext, todoView::class.java)
            intentobj.putExtra("title","")
            intentobj.putExtra("content","")
            intentobj.putExtra("id",id)
            var bundle=Bundle()
            bundle.putSerializable("todolist",todolist)
            intentobj.putExtras(bundle)
            if(todolist.get(0).title=="Nothing Pending!!")
                intentobj.putExtra("position",0)
            else
                intentobj.putExtra("position",todolist.lastIndex+1)
            intentobj.putExtra("password",intent.getStringExtra("password"))
            intentobj.putExtra("name",intent.getStringExtra("name"))
            intentobj.putExtra("email",intent.getStringExtra("email"))
            intentobj.putExtra("category",category)
            startActivity(intentobj)
            finish()
        }
        R.id.AccountSettings->{
            var intentobj: Intent = Intent(applicationContext,AccountSettings::class.java)
            intentobj.putExtra("id",id)
            intentobj.putExtra("password",intent.getStringExtra("password"))
            intentobj.putExtra("name",intent.getStringExtra("name"))
            intentobj.putExtra("email",intent.getStringExtra("email"))
            startActivity(intentobj)
        }
        R.id.help->{
            var intentobj: Intent = Intent(applicationContext,Help::class.java)
            startActivity(intentobj)
        }
        R.id.menu_refresh->{
            if(datstring!=" "){
                var todolist2=ArrayList<todo>()
                var det=datstring.split("#�#")
                for(i in det){
                    var j=i.split("�")
                    todolist2.add(todo(j[0],j[1],j[2]))
                }
                todolist=todolist2
                gv.adapter=gvAdapter(this,todolist)
                adapter.notifyDataSetChanged()
                try {
                    var id = intent.getStringExtra("id")
                    var category = intent.getStringExtra("category")
                    var fs1: FileOutputStream = openFileOutput("$id$category", MODE_PRIVATE)
                    var first = todolist.get(0).title + "�" + todolist.get(0).content+"�"+todolist.get(0).color
                    fs1.write(first.toByteArray())
                    for (i in 1..todolist.lastIndex)
                        fs1.write("\n${todolist.get(i).title}�${todolist.get(i).content}�${todolist.get(i).color}".toByteArray())
                    fs1.close()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()

                } catch (e: IOException) {
                    e.printStackTrace()

                }
            }
            else{
                Toast.makeText(applicationContext,"empty",Toast.LENGTH_SHORT).show()
            }
        }
    }
    return super.onOptionsItemSelected(item)
}
    override fun onBackPressed() {
        try {
            var datalist=ArrayList<String>()
            var id = intent.getStringExtra("id")
            var category = intent.getStringExtra("category")
            var fs1: FileOutputStream = openFileOutput("$id$category", MODE_PRIVATE)
            var first = todolist.get(0).title + "�" + todolist.get(0).content+"�"+todolist.get(0).color
            fs1.write(first.toByteArray())
            for (i in 1..todolist.lastIndex)
                fs1.write("\n${todolist.get(i).title}�${todolist.get(i).content}�${todolist.get(i).color}".toByteArray())
            fs1.close()
            for (i in 0..todolist.lastIndex)
                datalist.add("${todolist.get(i).title}�${todolist.get(i).content}�${todolist.get(i).color}")
            var datastring=datalist.joinToString("\n")
            var ref= FirebaseDatabase.getInstance().getReference("data")
            ref.child("$id$category").setValue(details("$id$category",datastring))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()

        } catch (e: IOException) {
            e.printStackTrace()

        }
        finish()
        super.onBackPressed()
    }
    private fun getFromServer(){
        var ref= FirebaseDatabase.getInstance().getReference("data")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (i in p0.children) {
                        val u = i.getValue(details::class.java)
                        if(u!!.id=="$id$category")
                            datstring=u.details
                    }
                }
                else{
                    datstring=" "
                }
            }
        })
    }
}






