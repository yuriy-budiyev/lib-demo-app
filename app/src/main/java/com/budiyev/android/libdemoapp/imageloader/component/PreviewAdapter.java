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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.budiyev.android.imageloader.ImageLoader;
import com.budiyev.android.libdemoapp.R;

public class PreviewAdapter extends PagerAdapter {
    private static final int DEFAULT_PREVIEW_SIZE = 1600;
    private final Context mContext;
    private final List<File> mItems;
    private final int mPreviewSize;

    public PreviewAdapter(@NonNull Context context) {
        mContext = context;
        mItems = new ArrayList<>();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mPreviewSize = Math.min((displayMetrics.widthPixels + displayMetrics.heightPixels) / 3, DEFAULT_PREVIEW_SIZE);
    }

    public void refresh(@NonNull Collection<File> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_preview_image, container, false);
        ImageLoader.with(mContext).from(mItems.get(position)).noStorageCache().size(mPreviewSize, mPreviewSize)
                .load(view.findViewById(R.id.image));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }
}
