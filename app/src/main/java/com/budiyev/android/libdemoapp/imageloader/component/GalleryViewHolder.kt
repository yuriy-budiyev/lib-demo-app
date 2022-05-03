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
package com.budiyev.android.libdemoapp.imageloader.component

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.budiyev.android.imageloader.ImageLoader
import com.budiyev.android.libdemoapp.R
import com.budiyev.android.libdemoapp.imageloader.PreviewActivity

class GalleryViewHolder(
    private val context: Context,
    parent: ViewGroup
): RecyclerView.ViewHolder(
    LayoutInflater.from(context).inflate(
        R.layout.item_gallery_image,
        parent,
        false
    )
) {

    fun bind(uri: Uri) {
        ImageLoader.with(context).from(uri).size(
            imageSize,
            imageSize
        ).noStorageCache().placeholder(ColorDrawable(Color.LTGRAY)).load(itemView)
    }

    private val imageSize: Int

    init {
        val displayMetrics = context.resources.displayMetrics
        imageSize =
            ((displayMetrics.widthPixels + displayMetrics.heightPixels) / 8).coerceAtMost(DEFAULT_THUMB_SIZE)
        itemView.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    PreviewActivity::class.java
                ).putExtra(
                    PreviewActivity.EXTRA_POSITION,
                    adapterPosition
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_THUMB_SIZE = 400
    }
}
