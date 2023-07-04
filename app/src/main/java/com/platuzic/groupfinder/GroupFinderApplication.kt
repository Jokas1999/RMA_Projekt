package com.platuzic.groupfinder

import android.app.Application
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.platuzic.groupfinder.auth.koinAuthModule
import com.platuzic.groupfinder.database.databaseModule
import com.platuzic.groupfinder.groupposts.groupModule
import lv.chi.photopicker.ChiliPhotoPicker
import lv.chi.photopicker.loader.ImageLoader
import org.koin.core.context.startKoin

class GroupFinderApplication: Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()

        startKoin{
            modules(
                listOf(
                    databaseModule,
                    koinAuthModule,
                    groupModule
                )
            )
        }


        ChiliPhotoPicker.init(
            loader = GlideImageLoader(),
            authority = "com.platuzic.groupfinder.fileprovider"
        )
    }

    companion object {
        private var instance: GroupFinderApplication? = null

        fun getAppContext(): Context {
            return instance?.applicationContext!!
        }
    }
}

internal class GlideImageLoader: ImageLoader {

    override fun loadImage(context: Context, view: ImageView, uri: Uri) {
        Glide.with(context)
            .load(uri)
            .placeholder(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .into(view)
    }
}