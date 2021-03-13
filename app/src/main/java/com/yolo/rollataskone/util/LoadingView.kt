package com.yolo.rollataskone.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.yolo.rollataskone.databinding.LoadingViewBinding

class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding =
        LoadingViewBinding.inflate(LayoutInflater.from(context), this, true)

}