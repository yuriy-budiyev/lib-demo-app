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

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.libdemoapp.R
import com.budiyev.android.libdemoapp.base.BaseActivity
import com.google.zxing.Result

class CodeScannerActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_scanner)
        hintView = findViewById(R.id.scanner_hint)
        codeScanner = CodeScanner(
            this,
            findViewById(R.id.scanner)
        )
        codeScanner.decodeCallback = DecodeCallback { result: Result ->
            runOnUiThread {
                hintView.text = getString(
                    R.string.code_scan_last,
                    result.text
                )
                val dialog = ScanResultDialog(
                    this,
                    result
                )
                dialog.setOnDismissListener {
                    codeScanner.startPreview()
                }
                dialog.show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { error: Throwable ->
            runOnUiThread {
                Toast.makeText(
                    this,
                    getString(
                        R.string.scanner_error,
                        error
                    ),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = false
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    RC_PERMISSION
                )
            } else {
                isPermissionGranted = true
            }
        } else {
            isPermissionGranted = true
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
        if (requestCode == RC_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
                codeScanner.startPreview()
            } else {
                isPermissionGranted = false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private lateinit var codeScanner: CodeScanner
    private lateinit var hintView: TextView
    private var isPermissionGranted: Boolean = false

    companion object {
        private const val RC_PERMISSION = 10
    }
}
