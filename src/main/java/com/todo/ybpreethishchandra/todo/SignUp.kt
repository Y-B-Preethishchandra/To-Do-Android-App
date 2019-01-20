package com.todo.ybpreethishchandra.todo

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*


class SignUp : AppCompatActivity(), View.OnClickListener {

    lateinit var nameElement:EditText
    lateinit var mailElement:EditText
    lateinit var passwordElement:EditText
    lateinit var confirmElement:EditText
    lateinit var userlist:MutableList<user>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.setTitle("SignUp")
        getFromServer()
        val button:Button =findViewById(R.id.signupSubmit)
        button.setOnClickListener(this)
        val back:Button=findViewById(R.id.signupBack)
        back.setOnClickListener(this)
        var pass:EditText=findViewById(R.id.signupPassword)
        pass.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(checkPassword(pass.text.toString())){
                    var pinf:ImageView=findViewById(R.id.passwordmark)
                    pinf.setImageResource(R.drawable.tick)
                }
                else{
                    var pinf:ImageView=findViewById(R.id.passwordmark)
                    pinf.setImageResource(R.drawable.cross)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(checkPassword(pass.text.toString())){
                    var pinf:ImageView=findViewById(R.id.passwordmark)
                    pinf.setImageResource(R.drawable.tick)
                }
                else{
                    var pinf:ImageView=findViewById(R.id.passwordmark)
                    pinf.setImageResource(R.drawable.cross)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(checkPassword(pass.text.toString())){
                    var pinf:ImageView=findViewById(R.id.passwordmark)
                    pinf.setImageResource(R.drawable.tick)
                }
                else{
                    var pinf:ImageView=findViewById(R.id.passwordmark)
                    pinf.setImageResource(R.drawable.cross)
                }
            }

        })

        var cpass:EditText=findViewById(R.id.signupConfirmPassword)
        cpass.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(checkCorrect(pass.text.toString(),cpass.text.toString())){
                    var pinf:ImageView=findViewById(R.id.confpasswordmark)
                    pinf.setImageResource(R.drawable.tick)
                }
                else{
                    var pinf:ImageView=findViewById(R.id.confpasswordmark)
                    pinf.setImageResource(R.drawable.cross)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                if(checkCorrect(pass.text.toString(),cpass.text.toString())){
                    var pinf:ImageView=findViewById(R.id.confpasswordmark)
                    pinf.setImageResource(R.drawable.tick)
                }
                else{
                    var pinf:ImageView=findViewById(R.id.confpasswordmark)
                    pinf.setImageResource(R.drawable.cross)
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(checkCorrect(pass.text.toString(),cpass.text.toString())){
                    var pinf:ImageView=findViewById(R.id.confpasswordmark)
                    pinf.setImageResource(R.drawable.tick)
                }
                else{
                    var pinf:ImageView=findViewById(R.id.confpasswordmark)
                    pinf.setImageResource(R.drawable.cross)
                }
            }

        })

    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signupSubmit->{
                passwordElement=findViewById(R.id.signupPassword)
                confirmElement=findViewById(R.id.signupConfirmPassword)
                var j = signup()
                if (j == true) {
                    Toast.makeText(this, "SignedUp", Toast.LENGTH_SHORT).show()
                    var intent: Intent = Intent(applicationContext, Login::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            R.id.signupBack->{
                var intent: Intent = Intent(applicationContext, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun signup():Boolean {
        nameElement=findViewById(R.id.signupName)
        mailElement=findViewById(R.id.signupEmail)
        var q:Boolean=checkDetails(nameElement.text.toString(),mailElement.text.toString())
        var p:Boolean=checkPassword(passwordElement.text.toString())
        if(p==true&&q==true) {
            var k:Boolean=checkCorrect(passwordElement.text.toString(),confirmElement.text.toString())
            var flag=0
            for(i in userlist)
                if(i.email==mailElement.text.toString())
                    flag=1
            if((k==true)&&(flag==0))
            {
                var ref= FirebaseDatabase.getInstance().getReference("Users")
                var encryptObject=Encode_Decode("AbC123pwqw856jhg".toByteArray())
                var passwordarray=encryptObject.encrypt(passwordElement.text.toString().toByteArray())
                var pw=passwordarray.toString(Charset.defaultCharset())
                val key=ref.push().key!!
                var usr=user(mailElement.text.toString(),key,nameElement.text.toString().toLowerCase(),pw)
                ref.child(key).setValue(usr)
                var fileName="$key${mailElement.text.toString()}"
                try{
                    var fs:FileOutputStream=openFileOutput(fileName,MODE_PRIVATE)
                    fs.write("Work\nSchool\nWash".toByteArray())
                    ref= FirebaseDatabase.getInstance().getReference("userCategory")
                    ref.child(key).setValue(category(key,"Work�School�Wash"))
                    ref=FirebaseDatabase.getInstance().getReference("data")
                    ref.child("${key}Work").setValue(details("${key}Work","Nothing Pending!!� �#faee78"))
                    ref.child("${key}Wash").setValue(details("${key}Work","Nothing Pending!!� �#faee78"))
                    ref.child("${key}School").setValue(details("${key}Work","Nothing Pending!!� �#faee78"))
                    fs.close()
                    return true
                }
                catch(e: FileNotFoundException){
                    e.printStackTrace()
                }
                catch(e: IOException){
                    e.printStackTrace()
                }
            }
            else{
                if(flag==1)
                    Toast.makeText(this,"Account already Exists",Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this,"Passwords do not Match", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        else{
            if(q==false)
                Toast.makeText(this,"Invalid name or email",Toast.LENGTH_SHORT).show()
            if(p==false)
                Toast.makeText(this,"Password does not meet specificatons",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    private fun getFromServer(){
        userlist= mutableListOf()
        var ref= FirebaseDatabase.getInstance().getReference("Users")
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

    override fun onBackPressed() {
        var intent: Intent = Intent(applicationContext, Login::class.java)
        startActivity(intent)
        finish()
        super.onBackPressed()
    }

    fun checkPassword(password:String):Boolean{
        var flag=0
        var flag2=0
        var flag3=0
        var flag4=0
        for (i in password) {
            if (i.isDigit())
                flag = 1
            if (i.isLowerCase())
                flag2 = 1
            if (i.isUpperCase())
                flag3 = 1
            else if (!i.isWhitespace() && !i.isUpperCase() && !i.isDigit() && !i.isLowerCase())
                flag4 = 1
        }
        if((flag==1)&&(flag2==1)&&(flag3==1)&&(flag4==1)&&(password.length>=6)) {
            return true
        }
        else
            return false
    }
    fun checkCorrect(password1:String,password2:String):Boolean{
        if(password1==password2) {
            return true
        }
        else
            return false
    }
    fun checkDetails(name:String,email:String):Boolean{
        if((name.length<3)||email.isEmpty())
            return false
        return true
    }

}