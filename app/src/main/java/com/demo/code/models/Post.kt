package com.demo.code.models

data class Post(var userId : Int, var id : Int, var title : String, var body : String,var comments: List<Comment>)