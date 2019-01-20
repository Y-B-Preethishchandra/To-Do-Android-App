package com.todo.ybpreethishchandra.todo


import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.Charset

class Intro : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        Thread(
                {
                    val fileName="remember"
                    try{
                        Thread.sleep(2000)
                        var fs: FileInputStream =openFileInput(fileName)
                        var str:String=fs.readBytes().toString(Charset.defaultCharset())
                        fs.close()
                        if(str=="")
                        {
                            val intent: Intent = Intent(applicationContext,Login::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            val intent: Intent = Intent(applicationContext,ToDoList::class.java)
                            var strs=str.split(",")
                            intent.putExtra("name",strs[0])
                            intent.putExtra("email",strs[1])
                            intent.putExtra("id",strs[2])
                            startActivity(intent)
                            finish()
                        }
                    }
                    catch(e: FileNotFoundException){
                        val intent: Intent = Intent(applicationContext,Login::class.java)
                        startActivity(intent)
                    }
                    catch(e: IOException){
                        val intent: Intent = Intent(applicationContext,Login::class.java)
                        startActivity(intent)
                    }
                }).start()

    }
}
