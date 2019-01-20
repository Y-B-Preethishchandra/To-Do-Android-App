package com.todo.ybpreethishchandra.todo

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.text.Layout
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import petrov.kristiyan.colorpicker.ColorPicker
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class todoView : AppCompatActivity(), View.OnClickListener {
    lateinit var content:String
    var todolist= ArrayList<todo>()
    var position=0
    lateinit var titleView:EditText
    lateinit var contentView:EditText
    lateinit var layout:View
    var bckcolor:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_view)
        var bundle=getIntent().getExtras()
        todolist=bundle.getSerializable("todolist") as ArrayList<todo>
        position=intent.getIntExtra("position",0)
        supportActionBar?.hide()
        titleView = findViewById(R.id.taskTitle)
        contentView = findViewById(R.id.taskContent)
        titleView.hint = "Title"
        contentView.hint = "Content"
        layout=findViewById<ConstraintLayout>(R.id.todoviewpage)

        if(position>todolist.lastIndex)
            layout.setBackgroundColor(Color.parseColor("#faee78"))
        else
            layout.setBackgroundColor(Color.parseColor(todolist.get(position).color))
        if (intent.getStringExtra("title") != ""){
            titleView.setText(intent.getStringExtra("title"))
            contentView.setText(intent.getStringExtra("content"))
        }
        layout=findViewById<ConstraintLayout>(R.id.todoviewpage)
        var saveButton: ImageView =findViewById(R.id.saveButton)
        var deleteButton:ImageView=findViewById(R.id.deleteButton)
        var share:ImageView=findViewById(R.id.share)
        var close:ImageView=findViewById(R.id.close)
        var bgclr:ImageView=findViewById(R.id.backgroundColor)
        saveButton.setOnClickListener(this)
        deleteButton.setOnClickListener(this)
        share.setOnClickListener(this)
        close.setOnClickListener(this)
        bgclr.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.backgroundColor->{
                opencolorpicker()
            }
            R.id.saveButton->{
                var flag=0
                for(i in todolist)
                    if (i.title==titleView.text.toString())
                        flag=1
                if(intent.getStringExtra("title")==titleView.text.toString())
                    flag=0
                if(flag==0){
                if((titleView.text.toString()!="")&&(titleView.text.toString()!=" ")&&(titleView.text.toString()!="Nothing Pending!!")){

                if(position<=todolist.lastIndex) {
                    if (bckcolor.equals(""))
                        bckcolor=todolist.get(position).color
                    todolist.set(position, todo(titleView.text.toString(), contentView.text.toString(), bckcolor))
                    bckcolor = todolist.get(position).color

                }
                else
                    todolist.add(todo(titleView.text.toString(),contentView.text.toString(),"#faee78"))
                try {
                    var id=intent.getStringExtra("id")
                    var category=intent.getStringExtra("category")
                    var fs1: FileOutputStream = openFileOutput("$id$category",MODE_PRIVATE)
                    var first = todolist.get(0).title+"�"+todolist.get(0).content+"�"+todolist.get(0).color
                    fs1.write(first.toByteArray())
                    var datalist=ArrayList<String>()
                    for (i in 1..todolist.lastIndex)
                        fs1.write("\n${todolist.get(i).title}�${todolist.get(i).content}�${todolist.get(i).color}".toByteArray())
                    fs1.close()
                    for (i in 0..todolist.lastIndex){
                        datalist.add("${todolist.get(i).title}�${todolist.get(i).content}�${todolist.get(i).color}")
                    }
                    var datastring=datalist.joinToString("#�#")
                    var ref= FirebaseDatabase.getInstance().getReference("data")
                    ref.child("$id$category").setValue(details("$id$category",datastring))
                    var intentobj= Intent(applicationContext,GridView::class.java)
                    intentobj.putExtra("id",intent.getStringExtra("id"))
                    intentobj.putExtra("category",intent.getStringExtra("category"))
                    intentobj.putExtra("password",intent.getStringExtra("password"))
                    intentobj.putExtra("name",intent.getStringExtra("name"))
                    intentobj.putExtra("email",intent.getStringExtra("email"))
                    startActivity(intentobj)
                    finish()
                }catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            else{
                    if(titleView.text.toString()=="Nothing Pending!!")
                        Toast.makeText(this,"Title Cannot be \"Nothing Pending!!\"",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show()
                }}
                else
                    Toast.makeText(this,"Title already Exists",Toast.LENGTH_SHORT).show()
            }
            R.id.close->{
                var intentobj= Intent(applicationContext,GridView::class.java)
                intentobj.putExtra("id",intent.getStringExtra("id"))
                intentobj.putExtra("category",intent.getStringExtra("category"))
                intentobj.putExtra("password",intent.getStringExtra("password"))
                intentobj.putExtra("name",intent.getStringExtra("name"))
                intentobj.putExtra("email",intent.getStringExtra("email"))
                startActivity(intentobj)
                finish()
            }
            R.id.deleteButton->{
                if(todolist.lastIndex==0) {
                    todolist.add(todo("Nothing Pending!!"," ","#faee78"))
                    todolist.removeAt(0)
                }
                else
                    todolist.removeAt(position)
                try {
                    var id=intent.getStringExtra("id")
                    var category=intent.getStringExtra("category")
                    var fs1: FileOutputStream = openFileOutput("$id$category",MODE_PRIVATE)
                    var first = todolist.get(0).title+"�"+todolist.get(0).content+"�"+todolist.get(0).color
                    fs1.write(first.toByteArray())
                    var datalist=ArrayList<String>()
                    for (i in 1..todolist.lastIndex)
                        fs1.write("\n${todolist.get(i).title}�${todolist.get(i).content}�${todolist.get(i).color}".toByteArray())
                    fs1.close()
                    for (i in 0..todolist.lastIndex){
                        datalist.add("${todolist.get(i).title}�${todolist.get(i).content}�${todolist.get(i).color}")
                    }
                    var datastring=datalist.joinToString("\n")
                    var ref= FirebaseDatabase.getInstance().getReference("data")
                    ref.child("$id$category").setValue(details("$id$category",datastring))
                    var intentobj= Intent(applicationContext,GridView::class.java)
                    intentobj.putExtra("id",intent.getStringExtra("id"))
                    intentobj.putExtra("category",intent.getStringExtra("category"))
                    intentobj.putExtra("password",intent.getStringExtra("password"))
                    intentobj.putExtra("name",intent.getStringExtra("name"))
                    intentobj.putExtra("email",intent.getStringExtra("email"))
                    startActivity(intentobj)
                    finish()
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            R.id.share->{
                if((titleView.text.toString()!="")&&(titleView.text.toString()!=" ")&&(titleView.text.toString()!="Nothing Pending!!")) {
                    var sharingIntent = Intent(android.content.Intent.ACTION_SEND)
                    sharingIntent.setType("text/plain")
                    var shareBodyText = titleView.text.toString() + ":" + contentView.text.toString()
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, titleView.text.toString())
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText)
                    startActivity(Intent.createChooser(sharingIntent, "Sharing Option"))
                }
                else{
                    if(titleView.text.toString()=="Nothing Pending!!")
                        Toast.makeText(this,"Title Cannot be \"Nothing Pending!!\"",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this,"Title cannot be empty",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun opencolorpicker(){
        var colorpicker:ColorPicker= ColorPicker(this)
        var colors=ArrayList<String>()
        colors.add("#ff0000")
        colors.add("#3C8D2F")
        colors.add("#20724F")
        colors.add("#6A3AB2")
        colors.add("#323299")
        colors.add("#faee78")
        colors.add("#B79716")
        colors.add("#966D37")
        colors.add("#B77231")
        colors.add("#32CD32")
        colorpicker.setColors(colors).setColumns(5).setRoundColorButton(true)
                .setOnChooseColorListener(object:ColorPicker.OnChooseColorListener{
                    override fun onChooseColor(position: Int, color: Int) {
                        layout.setBackgroundColor(color)
                        bckcolor = "#" + Integer.toHexString(color).substring(2)

                    }

                    override fun onCancel() {

                    }

                }).show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        var intentobj= Intent(applicationContext,GridView::class.java)
        intentobj.putExtra("id",intent.getStringExtra("id"))
        intentobj.putExtra("category",intent.getStringExtra("category"))
        intentobj.putExtra("password",intent.getStringExtra("password"))
        intentobj.putExtra("name",intent.getStringExtra("name"))
        intentobj.putExtra("email",intent.getStringExtra("email"))
        startActivity(intentobj)
        finish()
    }
}
