package com.example.taofu.manhuademo

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.widget.RelativeLayout


/*
 * created by taofu on 2018/6/16
**/
class  MyContentView : RelativeLayout{

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun fitSystemWindows(insets: Rect?): Boolean {


       return super.fitSystemWindows(insets)
        //return true
    }
}