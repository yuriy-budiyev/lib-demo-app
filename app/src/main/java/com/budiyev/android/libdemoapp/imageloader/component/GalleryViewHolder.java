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
package com.budiyev.android.libdemoapp.imageloader.component;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.budiyev.android.imageloader.ImageLoader;
import com.budiyev.android.libdemoapp.R;
import com.budiyev.android.libdemoapp.imageloader.PreviewActivity;

public class GalleryViewHolder extends RecyclerView.ViewHolder {
    private static final int DEFAULT_THUMB_SIZE = 400;
    private final Context mContext;
    private final ImageView mImageView;
    private final int mImageSize;

    public GalleryViewHolder(@NonNull Context context, @Nullable ViewGroup parent) {
        super(LayoutInflater.from(context).inflate(R.layout.item_gallery_image, parent, false));
        mContext = context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mImageSize = Math.min((displayMetrics.widthPixels + displayMetrics.heightPixels) / 8, DEFAULT_THUMB_SIZE);
        mImageView = itemView.findViewById(R.id.image);
        mImageView.setOnClickListener(v -> context.startActivity(new Intent(context, PreviewActivity.class)
                .putExtra(PreviewActivity.EXTRA_POSITION, getAdapterPosition())));
    }

    public void bind(@NonNull File file) {
        ImageLoader.with(mContext).from(file).size(mImageSize, mImageSize).placeholder(new ColorDrawable(Color.LTGRAY))
                .load(mImageView);
    }
}
