package com.tuantd.myapplication.mainscreen.posts

data class Posts(
                 var postTitle: String,
                 var postImageUrl: String,
                 var postContent:String,
                 var url : String
                ){
    constructor():this("","","","")
}
