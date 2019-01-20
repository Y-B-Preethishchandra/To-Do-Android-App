package com.todo.ybpreethishchandra.todo

import java.io.Serializable

class todo(val title:String,val content:String,val color:String):Serializable {
    constructor():this("","",""){
    }
}