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
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.budiyev.android.imageloader.ImageLoader
import com.budiyev.android.libdemoapp.R

class PreviewAdapter(private val context: Context): PagerAdapter() {

    fun refresh(items: Collection<Uri>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun instantiateItem(
        container: ViewGroup,
        position: Int
    ): Any {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_preview_image,
            container,
            false
        )
        ImageLoader.with(context).from(items[position]).noStorageCache().size(
            previewSize,
            previewSize
        ).load(view)
        container.addView(view)
        return view
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        obj: Any
    ) {
        container.removeView(obj as View)
    }

    override fun isViewFromObject(
        view: View,
        obj: Any
    ): Boolean =
        view === obj

    override fun getCount(): Int =
        items.size

    private val items: MutableList<Uri>
    private val previewSize: Int

    init {
        items = ArrayList()
        val displayMetrics = context.resources.displayMetrics
        previewSize =
            ((displayMetrics.widthPixels + displayMetrics.heightPixels) / 3).coerceAtMost(DEFAULT_PREVIEW_SIZE)
    }

    companion object {
        private const val DEFAULT_PREVIEW_SIZE = 1600
    }
}