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

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.viewpager.widget.ViewPager
import com.budiyev.android.libdemoapp.R
import com.budiyev.android.libdemoapp.base.BaseActivity
import com.budiyev.android.libdemoapp.imageloader.component.PreviewAdapter

class PreviewActivity: BaseActivity(), LoaderManager.LoaderCallbacks<Cursor?> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        val position = intent.getIntExtra(
            EXTRA_POSITION,
            -1
        )
        require(position != -1) { "Position not specified" }
        this.position = position
        previewPager = findViewById(R.id.preview)
        previewPager.pageMargin = resources.getDimensionPixelOffset(R.dimen.preview_page_margin)
        previewAdapter = PreviewAdapter(this)
        LoaderManager.getInstance(this).initLoader(
            0,
            null,
            this
        )
    }

    override fun onCreateLoader(
        id: Int,
        args: Bundle?
    ): Loader<Cursor?> {
        return CursorLoader(
            this,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN
            ),
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
            val images: MutableList<Uri> = ArrayList()
            if (cursor.moveToFirst()) {
                val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                do {
                    val id = cursor.getLong(idColumn)
                    images += Uri.withAppendedPath(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id.toString()
                    )
                } while (cursor.moveToNext())
            }
            previewAdapter.refresh(images)
            previewPager.adapter = previewAdapter
            previewPager.currentItem = position
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor?>) {
        // Do nothing
    }

    private lateinit var previewPager: ViewPager
    private lateinit var previewAdapter: PreviewAdapter
    private var position: Int = 0

    companion object {

        const val EXTRA_POSITION = "position"
    }
}
