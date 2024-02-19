package com.example.test10.presenter.extensions

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun Context.loadImageWithGlide(uri:String, view: ImageView){
    Glide.with(this)
        .load(uri)
        .into(view)
}