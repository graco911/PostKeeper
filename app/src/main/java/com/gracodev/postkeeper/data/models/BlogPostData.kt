package com.gracodev.postkeeper.data.models

import java.util.Date

data class BlogPostData(
    var title: String = "",
    var author: String = "",
    var timeStamp: Long = 0,
    var body: String = "",
    var id: String = ""
)