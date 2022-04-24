package com.conboi.talkcon.di

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TalkConApplication : Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        val imageLoader: ImageLoader by lazy {
            ImageLoader.Builder(applicationContext)
                .crossfade(true)
                .build()
        }
        return imageLoader
    }

}