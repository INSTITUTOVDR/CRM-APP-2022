package com.algarrapablo.mobile.tpalgarra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class SecondActivity : AppCompatActivity() {
    private var image2:ImageView?=null
    private var dato: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        dato=findViewById(R.id.tvsecond)
        image2=findViewById(R.id.imageView2)
        val bundle = intent.extras
        val vqv = bundle?.getString("dato")
        val image = bundle?.getString("image")
        dato!!.text = vqv

        Glide.with(this)
            .load(image)
            .placeholder(R.drawable.modificar)
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)

            .centerCrop()
            .into(image2!!)

    }
}