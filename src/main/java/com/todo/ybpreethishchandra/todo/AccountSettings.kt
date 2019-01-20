package com.todo.ybpreethishchandra.todo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import java.nio.charset.Charset

class AccountSettings : AppCompatActivity(), View.OnClickListener {
    lateinit var password:String
    lateinit var id:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent=intent
        setContentView(R.layout.activity_account_settings)
        supportActionBar?.setTitle("Change Password")
        password=intent.getStringExtra("password")
        id=intent.getStringExtra("id")
        var changePassword: Button =findViewById(R.id.changePassword)
        changePassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val oldpass: EditText =findViewById(R.id.currentPassword)
        val newpass:EditText=findViewById(R.id.newPassword)
        val conf:EditText=findViewById(R.id.confirmNewPassword)
        val oldpassword=oldpass.text.toString()
        val newpassword=newpass.text.toString()
        val confirmpassword=conf.text.toString()
        val intent=intent
        val name=intent.getStringExtra("name")
        val email=intent.getStringExtra("email")
        var encryptObject=Encode_Decode("AbC123pwqw856jhg".toByteArray())
        var i=checkCorrect(intent.getStringExtra("password"),encryptObject.encrypt(oldpassword.toByteArray()).toString(Charset.defaultCharset()))
        var j=checkCorrect(newpassword,confirmpassword)
        var k=checkPassword(newpassword)
        var l=newpassword==oldpassword
        if(i==false)
            Toast.makeText(this,"Incorrect Old Password",Toast.LENGTH_SHORT).show()
        else if (k==false)
            Toast.makeText(this,"New password does not meet specifications",Toast.LENGTH_SHORT).show()
        else if(j==false)
            Toast.makeText(this,"New Password and Confirm Password doesn't match  password does not meet specifications",Toast.LENGTH_SHORT).show()
        else if(l==true)
            Toast.makeText(this,"New Password is same as old password",Toast.LENGTH_SHORT).show()
        else if((j==true) && (i==true) && (k==true) && (l==false))
        {
            var ref= FirebaseDatabase.getInstance().getReference("Users").child(id)
            var passwordarray=encryptObject.encrypt(newpassword.toByteArray())
            var pw=passwordarray.toString(Charset.defaultCharset())
            var usr=user(email,id,name,pw)
            ref.setValue(usr)
            Toast.makeText(this,"Updated", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    fun checkCorrect(password1:String,password2:String):Boolean{
        if(password1==password2) {
            return true
        }
        else
            return false
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
        if(flag==1&&flag2==1&&flag3==1&&flag4==1) {
            return true
        }
        else
            return false
    }
}
