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
package com.budiyev.android.libdemoapp.circularprogressbar

import android.os.Bundle
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.budiyev.android.circularprogressbar.CircularProgressBar
import com.budiyev.android.libdemoapp.R
import com.budiyev.android.libdemoapp.base.BaseActivity

class CircularProgressBarActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_circular_progress_bar)
        val progressBar = findViewById<CircularProgressBar>(R.id.progress_bar)
        findViewById<SeekBar>(R.id.progress).setOnSeekBarChangeListener(object:
            OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                progressBar.progress = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        findViewById<CheckBox>(R.id.indeterminate).setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            progressBar.isIndeterminate = isChecked
        }
        findViewById<CheckBox>(R.id.draw_background_stroke).setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            progressBar.isDrawBackgroundStroke = isChecked
        }
    }
}
