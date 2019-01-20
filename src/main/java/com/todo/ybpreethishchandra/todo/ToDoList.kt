package com.todo.ybpreethishchandra.todo

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.util.*


class ToDoList : AppCompatActivity(), AdapterView.OnItemLongClickListener {
    lateinit var signout: Button
    lateinit var name: String
    lateinit var email: String
    lateinit var id1: String
    var cat = arrayListOf<String>()
    lateinit var lv: ListView
    lateinit var adapter: lvAdapter
    lateinit var catstring:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)
        getFromServer()
        supportActionBar?.setTitle("Your Activities")
        var intentobj: Intent = intent
        name = intentobj.getStringExtra("name")
        email = intentobj.getStringExtra("email")
        id1 = intentobj.getStringExtra("id")
        val tv: TextView = findViewById(R.id.welcomeText)

        tv.text = "Welcome ${name[0].toUpperCase()}${name.substring(1)},"
        try {
            var fs: FileInputStream = openFileInput("$id1$email")
            var s = fs.readBytes().toString(Charset.defaultCharset())
            var cat1 = s.split("\n")
            fs.close()
            for (i in 0..cat1.lastIndex)
                cat.add(cat1[i])
            } catch (e: FileNotFoundException) {
                var fs: FileOutputStream = openFileOutput("$id1$email", MODE_PRIVATE)
                fs.write("Work\nSchool\nWash".toByteArray())
                cat.add("Work")
                cat.add("School")
                cat.add("Wash")

                fs.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        lv = findViewById(R.id.listView)
        adapter = lvAdapter(applicationContext, cat)
        lv.adapter = adapter
        lv.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                if (cat.get(position) != "Nothing Pending!!") {
                    var password = intent.getStringExtra("password")
                    var intentobj1 = Intent(applicationContext, GridView::class.java)
                    intentobj1.putExtra("category", cat[position])
                    intentobj1.putExtra("id", id1)
                    intentobj1.putExtra("password", password)
                    intentobj1.putExtra("name", name)
                    intentobj1.putExtra("email", email)
                    startActivity(intentobj1)
                }
        }
        lv.setOnItemLongClickListener(this)
    }

    override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        var dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Delete")
        dialogBuilder.setMessage("Are You sure you want to delete ${cat[position]}?")
        dialogBuilder.setPositiveButton(R.string.button_positivedelete, DialogInterface.OnClickListener { _, _ ->
            if (cat.size > 1) {
                var delcategory=cat[position]
                var rem=cat.removeAt(position)
                adapter.notifyDataSetChanged()
                try {
                    var fs1: FileOutputStream = openFileOutput("$id1$email", MODE_PRIVATE)
                    var first = cat.get(0)
                    fs1.write(first.toByteArray())
                    for (i in 1..cat.lastIndex)
                        fs1.write("\n${cat[i]}".toByteArray())
                    fs1.close()
                    fs1=openFileOutput(id1+delcategory,MODE_PRIVATE)
                    fs1.write("Nothing Pending!!� �#faee78".toByteArray())
                    fs1.close()
                    var ref= FirebaseDatabase.getInstance().getReference("userCategory")
                    ref.child(id1).setValue(category(id1,"${cat.joinToString("�")}"))
                    ref=FirebaseDatabase.getInstance().getReference("data")
                    ref.child("$id1$rem").removeValue()
                    adapter = lvAdapter(this, cat)
                    lv.adapter = adapter

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()

                } catch (e: IOException) {
                    e.printStackTrace()

                }
            } else {
                cat.add("Nothing Pending!!")
                var delcategory=cat[0]
                var rem=cat.removeAt(0)
                adapter.notifyDataSetChanged()
                try {
                    var fs1: FileOutputStream = openFileOutput("$id1$email", MODE_PRIVATE)
                    fs1.write("Nothing Pending!!".toByteArray())
                    fs1.close()
                    fs1=openFileOutput(id1+delcategory,MODE_PRIVATE)
                    fs1.write("Nothing Pending!!� �#faee78".toByteArray())
                    var ref= FirebaseDatabase.getInstance().getReference("userCategory")
                    ref.child(id1).setValue(category(id1,"${cat.joinToString("�")}"))
                    ref=FirebaseDatabase.getInstance().getReference("data")
                    ref.child("$id1$rem").removeValue()
                    lv.adapter= lvAdapter(this,cat)
                    adapter.notifyDataSetChanged()
                    fs1.close()

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()

                } catch (e: IOException) {
                    e.printStackTrace()

                }

            }
        })
        dialogBuilder.setNegativeButton(R.string.button_negative, DialogInterface.OnClickListener { _, _ -> })
        var alertdialog = dialogBuilder.create()
        alertdialog.show()
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.signOut -> {
                try {
                    var fs: FileOutputStream = openFileOutput("remember", MODE_PRIVATE)
                    fs.write("".toByteArray())
                    fs.close()
                    fs= openFileOutput("$id1$email", MODE_PRIVATE)
                    for (i in 0..(cat.lastIndex - 1))
                        fs.write("${cat.get(i)}\n".toByteArray())
                    fs.write(cat.get(cat.lastIndex).toByteArray())
                    fs.close()
                    var ref= FirebaseDatabase.getInstance().getReference("userCategory")
                    ref.child(id1).setValue(category(id1,"${cat.joinToString("�")}"))
                    val intent: Intent = Intent(applicationContext, Login::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            R.id.menu_add -> {
                var dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setTitle("Add Category")
                val view = layoutInflater.inflate(R.layout.add_category, null)
                dialogBuilder.setView(view)
                dialogBuilder.setPositiveButton(R.string.button_positive, DialogInterface.OnClickListener { _, _ ->
                    val et = view.findViewById<EditText>(R.id.newcat)
                    val newcat = et.text.toString()
                    if (cat.get(0) == "Nothing Pending!!") {
                        cat[0] = newcat
                        try {
                            var fs: FileOutputStream = openFileOutput("$id1$email", MODE_PRIVATE)
                            for (i in 0..(cat.lastIndex - 1))
                                fs.write("${cat.get(i)}\n".toByteArray())
                            fs.write(cat.get(cat.lastIndex).toByteArray())
                            fs.close()
                            var ref= FirebaseDatabase.getInstance().getReference("userCategory")
                            ref.child(id1).setValue(category(id1,"${cat.joinToString("�")}"))
                            ref=FirebaseDatabase.getInstance().getReference("data")
                            ref.child("$id1$newcat").setValue(details("$id1$newcat","Nothing Pending!!� �#faee78"))
                            lv.adapter=lvAdapter(this,cat)
                            adapter.notifyDataSetChanged()


                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        if (!newcat.isEmpty() && newcat != "Nothing Pending!!") {
                            cat.add(newcat)
                            adapter.notifyDataSetChanged()
                            try {
                                var fs: FileOutputStream = openFileOutput("$id1$email", MODE_PRIVATE)
                                for (i in 0..(cat.lastIndex - 1))
                                    fs.write("${cat.get(i)}\n".toByteArray())
                                fs.write(cat.get(cat.lastIndex).toByteArray())
                                fs.close()
                                var ref= FirebaseDatabase.getInstance().getReference("userCategory")
                                ref.child(id1).setValue(category(id1,"${cat.joinToString("�")}"))
                                ref=FirebaseDatabase.getInstance().getReference("data")
                                ref.child("$id1$newcat").setValue(details("$id1$newcat","Nothing Pending!!� �#faee78"))
                                lv.adapter=lvAdapter(this,cat)
                                adapter.notifyDataSetChanged()


                            } catch (e: FileNotFoundException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }

                        } else {
                            if (newcat == "")
                                Toast.makeText(this, "Nothing added", Toast.LENGTH_SHORT).show()
                            else
                                Toast.makeText(this, "Cannot add \"Nothing Pending!!\"", Toast.LENGTH_SHORT).show()
                        }
                    }

                })
                dialogBuilder.setNegativeButton(R.string.button_negative, DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(this, "", Toast.LENGTH_SHORT)
                })
                var alertdialog = dialogBuilder.create()
                alertdialog.show()

            }
            R.id.AccountSettings -> {
                var intentobj: Intent = Intent(applicationContext, AccountSettings::class.java)
                intentobj.putExtra("id", id1)

                intentobj.putExtra("password", intent.getStringExtra("password"))
                intentobj.putExtra("name", name)
                intentobj.putExtra("email", email)
                startActivity(intentobj)

            }
            R.id.help -> {
                var intentobj: Intent = Intent(applicationContext, Help::class.java)
                startActivity(intentobj)
            }
            R.id.menu_refresh->{
                if(catstring!=" "){
                    var abc=catstring.split("�")
                    var cat2=ArrayList<String>()
                    for(i in abc){
                        cat2.add(i)
                    }
                    cat=cat2
                    try {
                        var fs: FileOutputStream = openFileOutput("$id1$email", MODE_PRIVATE)
                        for (i in 0..(cat.lastIndex - 1))
                            fs.write("${cat.get(i)}\n".toByteArray())
                        fs.write(cat.get(cat.lastIndex).toByteArray())
                        fs.close()
                     } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                     } catch (e: IOException) {
                         e.printStackTrace()
                }
                    lv.adapter=lvAdapter(this,cat)
                    adapter.notifyDataSetChanged()
                }
                else{
                    Toast.makeText(applicationContext,"Nothing in server",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getFromServer(){
        var ref= FirebaseDatabase.getInstance().getReference("userCategory")
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (i in p0.children) {
                        val u = i.getValue(category::class.java)
                        if(u!!.id==id1)
                            catstring=u.categories
                    }
                }
                else{
                    catstring=" "
                }
            }
        })
    }

    override fun onBackPressed() {
        try {
            var fs: FileOutputStream = openFileOutput("$id1$email", MODE_PRIVATE)
            for (i in 0..(cat.lastIndex - 1))
                fs.write("${cat.get(i)}\n".toByteArray())
            fs.write(cat.get(cat.lastIndex).toByteArray())
            fs.close()
            var ref= FirebaseDatabase.getInstance().getReference("userCategory")
            ref.child(id1).setValue(category(id1,"${cat.joinToString("�")}"))
            lv.adapter=lvAdapter(this,cat)
            adapter.notifyDataSetChanged()


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        super.onBackPressed()
    }
    override fun onDestroy() {

        var ref= FirebaseDatabase.getInstance().getReference("userCategory")
        ref.child(id1).setValue(category(id1,"${cat.joinToString("�")}"))
        super.onDestroy()
    }
}