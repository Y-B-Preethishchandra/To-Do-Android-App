package com.todo.ybpreethishchandra.todo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset


class Login : AppCompatActivity(), View.OnClickListener {
    lateinit private var email:String
    lateinit private var password:String
    lateinit var userlist:MutableList<user>
    lateinit var userdet:user
    lateinit var check:CheckBox
    var ref= FirebaseDatabase.getInstance().getReference("Users")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        getFromServer()
        check=findViewById(R.id.rememberMe)
        var button: Button = findViewById(R.id.loginButton)
        button.setOnClickListener(this)
        var button2:Button=findViewById(R.id.signupButton)
        button2.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.loginButton -> {
                getDetails()
                var i:Boolean =verifyDetails(email,password)
                if (i == true) {
                    if(check.isChecked){
                        try{
                            var fs: FileOutputStream =openFileOutput("remember",MODE_PRIVATE)
                            fs.write("${userdet.name},${userdet.email},${userdet.id}".toByteArray())
                            fs.close()
                            var intent: Intent = Intent(applicationContext, ToDoList::class.java)
                            intent.putExtra("email", userdet.email)
                            intent.putExtra("name", userdet.name)
                            intent.putExtra("id",userdet.id)
                            intent.putExtra("password",password)
                            startActivity(intent)
                            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
                            finish()

                        }

                        catch(e: FileNotFoundException){
                            e.printStackTrace()
                        }
                        catch(e: IOException){
                            e.printStackTrace()
                        }

                    }
                    else{
                        try {
                            var fs: FileOutputStream = openFileOutput("remember", MODE_PRIVATE)
                            fs.write("".toByteArray())
                            fs.close()
                            var intent: Intent = Intent(applicationContext, ToDoList::class.java)
                            intent.putExtra("email", email)
                            intent.putExtra("name", userdet.name)
                            intent.putExtra("id",userdet.id)
                            intent.putExtra("password",password)
                            startActivity(intent)

                            Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        catch(e: FileNotFoundException){
                            Toast.makeText(this,"Hey",Toast.LENGTH_SHORT).show()
                            e.printStackTrace()
                        }
                        catch(e: IOException){
                            e.printStackTrace()
                        }
                    }
                }
                else{
                    Toast.makeText(this,"Invalid ID/password",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.signupButton -> {
                val intent:Intent=Intent(applicationContext,SignUp::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getFromServer(){
        userlist= mutableListOf()
        ref.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
           }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (i in p0.children) {
                       val u = i.getValue(user::class.java)
                        userlist.add(u!!)
                    }
                }
            }
        })
    }

    private fun getDetails(){
        var passwordelement: EditText =findViewById(R.id.loginPassword)
        var mailelement:EditText=findViewById(R.id.loginEmail)
        email=mailelement.text.toString()
        var pass= passwordelement.text.toString()
        var encryptObject=Encode_Decode("AbC123pwqw856jhg".toByteArray())
        var passwordarray=encryptObject.encrypt(pass.toByteArray())
        password=passwordarray.toString(Charset.defaultCharset())
    }

    fun verifyDetails(id:String ,password:String):Boolean {
        for (us in userlist)
        {
            if(us.email==id && us.password==password){
                userdet=us
                return true
            }
        }
        return false
    }

}


