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
package com.budiyev.android.libdemoapp.codescanner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.budiyev.android.libdemoapp.R
import com.google.zxing.Result

class ScanResultDialog(
    context: Context,
    result: Result
): AppCompatDialog(
    context,
    resolveDialogTheme(context)
) {

    init {
        setTitle(R.string.scan_result)
        setContentView(R.layout.dialog_scan_result)
        findViewById<TextView>(R.id.result)!!.text = result.text
        findViewById<TextView>(R.id.format)!!.text = result.barcodeFormat.toString()
        findViewById<View>(R.id.copy)!!.setOnClickListener {
            (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
                ClipData.newPlainText(
                    null,
                    result.text
                )
            )
            Toast.makeText(
                context,
                R.string.copied_to_clipboard,
                Toast.LENGTH_LONG
            ).show()
            dismiss()
        }
        findViewById<View>(R.id.close)!!.setOnClickListener {
            dismiss()
        }
    }

    companion object {

        private fun resolveDialogTheme(context: Context): Int {
            val outValue = TypedValue()
            context.theme.resolveAttribute(
                androidx.appcompat.R.attr.alertDialogTheme,
                outValue,
                true
            )
            return outValue.resourceId
        }
    }
}
