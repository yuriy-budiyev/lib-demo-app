/*
 * MIT License
 *
 * Copyright (c) 2018 Yuriy Budiyev [yuriy.budiyev@yandex.ru]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.budiyev.android.libdemoapp.imageloader

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.libdemoapp.R
import com.budiyev.android.libdemoapp.base.BaseActivity
import com.budiyev.android.libdemoapp.imageloader.component.GalleryAdapter
import java.io.File

class GalleryActivity: BaseActivity(), LoaderManager.LoaderCallbacks<Cursor?> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        val galleryView = findViewById<RecyclerView>(R.id.gallery)
        galleryView.layoutManager = GridLayoutManager(
            this,
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) COLUMN_COUNT_LANDSCAPE else COLUMN_COUNT_PORTRAIT
        )
        galleryAdapter = GalleryAdapter(this)
        galleryView.adapter = galleryAdapter
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LoaderManager.getInstance(this).initLoader(
                0,
                null,
                this
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                RC_STORAGE_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        if (requestCode == RC_STORAGE_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoaderManager.getInstance(this).initLoader(
                    0,
                    null,
                    this
                )
            }
        }
    }

    override fun onCreateLoader(
        id: Int,
        args: Bundle?
    ): Loader<Cursor?> {
        return CursorLoader(
            this,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            PROJECTION,
            null,
            null,
            MediaStore.Images.Media.DATE_TAKEN + " DESC"
        )
    }

    override fun onLoadFinished(
        loader: Loader<Cursor?>,
        cursor: Cursor?
    ) {
        if (cursor != null) {
            val images: MutableList<File> = ArrayList()
            if (cursor.moveToFirst()) {
                val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                do {
                    val data = cursor.getString(dataColumn)
                    if (data != null) {
                        images.add(File(data))
                    }
                } while (cursor.moveToNext())
            }
            galleryAdapter.refresh(images)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        // Do nothing
    }

    private lateinit var galleryAdapter: GalleryAdapter

    companion object {

        private val PROJECTION = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA
        )

        private const val COLUMN_COUNT_PORTRAIT = 3
        private const val COLUMN_COUNT_LANDSCAPE = 5
        private const val RC_STORAGE_PERMISSION = 1
    }
}
