package com.japharr.videoplayer.common.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import java.io.File

/**
 * Created by Japharr on 8/5/2018.
 */
fun ViewGroup.inflate(layoutId: Int, attachToHost: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToHost)
}

fun ImageView.loadImage(imageUrl: String?, progressBar: ProgressBar) {
    val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

    Glide.with(context.applicationContext)
            .load(imageUrl)
            .listener(object: RequestListener<Drawable> {
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    progressBar.visibility = View.GONE
                    return false
                }
            })
            .apply(options)
            .thumbnail(0.5f)
            //.transition(withCrossFade())
            //.thumbnail(0.5f).crossFade()
            //.diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this);
}

fun ImageView.loadImage(imageUrl: String?, placeHolder: Int) {
    val options = RequestOptions()
            .placeholder(placeHolder)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    Glide.with(context.applicationContext)
            .load(imageUrl)
            .apply(options)
            .thumbnail(0.5f)
            //.transition(withCrossFade())
            //.thumbnail(0.5f).crossFade()
            //.diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this);
}

fun ImageView.loadImage(bitmap: Bitmap, placeHolder: Int) {
    val options = RequestOptions()
            .placeholder(placeHolder)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    Glide.with(context.applicationContext)
            .load(bitmap)
            .apply(options)
            .thumbnail(0.5f)
            //.transition(withCrossFade())
            //.thumbnail(0.5f).crossFade()
            //.diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this);
}


fun ImageView.loadImage(applicationContext: Context, imageUrl: String?, placeHolder: Int) {
    val options = RequestOptions()
            .placeholder(placeHolder)
            .diskCacheStrategy(DiskCacheStrategy.NONE);

    Glide.with(applicationContext)
            .load(imageUrl)
            .apply(options)
            .thumbnail(0.5f)
            //.transition(withCrossFade())
            //.thumbnail(0.5f).crossFade()
            //.diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(this);
}

fun ImageView.loadThumbnailCache(file: File) {
    val options = RequestOptions()
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    Glide.with(context.applicationContext)
            .asBitmap()
            .load(Uri.fromFile(file))
            .apply(options)
            .into(this);
}