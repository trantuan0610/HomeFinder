package com.tuantd.myapplication.DetailPost

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tuantd.myapplication.R

class DetailPostActivity : AppCompatActivity() {
    lateinit var webView:WebView
    lateinit var url : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)
        webView=  findViewById(R.id.webView)
        intent.getStringExtra("url")?.let { webView.loadUrl(it) }
    }
}