package com.example.taofu.manhuademo

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.Layout
import android.text.SpannableString
import android.text.Spanned
import android.text.StaticLayout
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ReplacementSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RadioGroup
import android.widget.TextView


/*
 * created by taofu on 2018/6/18
**/
class FragmentBookCover : Fragment() {

    private lateinit var mContentView: View
    private lateinit var mToolbar: Toolbar
    private lateinit var mTvSynopsis: TextView

    private lateinit var  mRgTabs : RadioGroup

    private  lateinit var mDetailsContainer : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mContentView = inflater.inflate(R.layout.fragment_reader_book_cover, container, false)


        mToolbar = mContentView.findViewById<Toolbar>(R.id.tool_bar)
        mTvSynopsis = mContentView.findViewById(R.id.reader_book_cover_tv_synopsis_content)

        mDetailsContainer = mContentView.findViewById(R.id.reader_book_cover_details_container)

        mRgTabs = mContentView.findViewById<RadioGroup>(R.id.reader_book_cover_tab).apply {

            setOnCheckedChangeListener { group, checkedId ->
                if(checkedId == R.id.reader_book_cover_tab_catalog){
                    mDetailsContainer.visibility = View.GONE
                }else{
                    mDetailsContainer.visibility = View.VISIBLE
                }
            }
        }


        val compatActivity : AppCompatActivity = activity as AppCompatActivity
        val synopsis = getString(R.string.reader_book_cover_synopsis_content)
        mTvSynopsis.post({ setSynopsis(mTvSynopsis, synopsis,synopsis, maxCount = 3) })
        compatActivity.setSupportActionBar(mToolbar)
        val p = mToolbar.layoutParams as FrameLayout.LayoutParams
        p.topMargin = getStatusBarHeight(compatActivity)



        return mContentView

    }

    fun setSynopsis(view: TextView, showContent: String, originContent: String,viewWidth: Int = 0, maxCount: Int) {


        val paint = mTvSynopsis.paint

        var width = viewWidth
        if (width == 0) {
            width = view.width
        }
        val staticLayout = StaticLayout(originContent, paint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false)
        val lineCount = staticLayout.lineCount

        if (lineCount > maxCount) {
            val endIndex = staticLayout.getLineStart(3) - 1
            setExpandOrCollapseOperation(view, originContent.slice(0..endIndex),originContent)

        } else {
            if (view.isSelected) {
                setExpandOrCollapseOperation(view, showContent,originContent)
            } else {
                view.text = showContent
            }
        }


    }


    fun setExpandOrCollapseOperation(view: TextView, showContent: String,originContent : String) {

        val spannableString = SpannableString(showContent)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View?) {
                mTvSynopsis.isSelected = !mTvSynopsis.isSelected

                setSynopsis(view, originContent + " ", originContent, maxCount =  if (mTvSynopsis.isSelected) Int.MAX_VALUE else 3 )

            }

        }, showContent.length - 2, showContent.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannableString.setSpan(object : ReplacementSpan() {

            override fun getSize(paint: Paint?, text: CharSequence?, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
                return paint?.measureText(text, start, end)!!.toInt()
            }

            override fun draw(canvas: Canvas?, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint?) {
                if (canvas == null)
                    return
                if (paint == null)
                    return
                canvas.drawText(text, start, end, x, y.toFloat(), paint)
                val fontMetrics = paint.fontMetrics ?: return
                var rId = R.drawable.reader_book_cover_expand
                if(view.isSelected){
                    rId = R.drawable.reader_book_cover_collapse
                }
                val bitmap = BitmapFactory.decodeResource(resources, rId)

                val rLeft: Float = x
                val rTop: Float = y + fontMetrics.ascent
                val rRight: Float = rLeft + bitmap.width
                val rBottom: Float = rTop + bitmap.height
                val rectF = RectF(rLeft, rTop, rRight, rBottom)
                canvas.drawBitmap(bitmap, null, rectF, paint)
            }

        }, showContent.length - 2, showContent.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)


        view.movementMethod = LinkMovementMethod.getInstance()
        view.text = spannableString

    }
    private fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val res = context.getResources()
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

}