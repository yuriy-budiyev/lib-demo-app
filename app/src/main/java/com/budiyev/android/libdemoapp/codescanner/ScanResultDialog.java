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
package com.budiyev.android.libdemoapp.codescanner;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.libdemoapp.R;
import com.google.zxing.Result;

public class ScanResultDialog extends AppCompatDialog {
    public ScanResultDialog(@NonNull Context context, @NonNull Result result) {
        super(context, resolveDialogTheme(context));
        setTitle(R.string.scan_result);
        setContentView(R.layout.dialog_scan_result);
        //noinspection ConstantConditions
        ((TextView) findViewById(R.id.result)).setText(result.getText());
        //noinspection ConstantConditions
        ((TextView) findViewById(R.id.format)).setText(String.valueOf(result.getBarcodeFormat()));
        //noinspection ConstantConditions
        findViewById(R.id.copy).setOnClickListener(v -> {
            //noinspection ConstantConditions
            ((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE))
                    .setPrimaryClip(ClipData.newPlainText(null, result.getText()));
            Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_LONG).show();
            dismiss();
        });
        //noinspection ConstantConditions
        findViewById(R.id.close).setOnClickListener(v -> dismiss());
    }

    private static int resolveDialogTheme(@NonNull Context context) {
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.support.v7.appcompat.R.attr.alertDialogTheme, outValue, true);
        return outValue.resourceId;
    }
}
