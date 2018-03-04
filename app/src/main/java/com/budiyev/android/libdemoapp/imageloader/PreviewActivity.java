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
package com.budiyev.android.libdemoapp.imageloader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;

import com.budiyev.android.libdemoapp.R;
import com.budiyev.android.libdemoapp.base.BaseActivity;
import com.budiyev.android.libdemoapp.imageloader.component.PreviewAdapter;

public class PreviewActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] PROJECTION =
            new String[] {MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_TAKEN,
                    MediaStore.Images.Media.DATA};
    public static String EXTRA_POSITION = "position";
    private ViewPager mPreviewPager;
    private int mPosition;
    private PreviewAdapter mPreviewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        int position = getIntent().getIntExtra(EXTRA_POSITION, -1);
        if (position == -1) {
            throw new IllegalArgumentException("Position not specified");
        }
        mPosition = position;
        mPreviewPager = findViewById(R.id.preview);
        mPreviewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.preview_page_margin));
        mPreviewAdapter = new PreviewAdapter(this);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION, null, null,
                MediaStore.Images.Media.DATE_TAKEN + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, @Nullable Cursor cursor) {
        if (cursor != null) {
            List<File> images = new ArrayList<>();
            if (cursor.moveToFirst()) {
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                do {
                    String data = cursor.getString(dataColumn);
                    if (data != null) {
                        images.add(new File(data));
                    }
                } while (cursor.moveToNext());
            }
            mPreviewAdapter.refresh(images);
            mPreviewPager.setAdapter(mPreviewAdapter);
            mPreviewPager.setCurrentItem(mPosition);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}
