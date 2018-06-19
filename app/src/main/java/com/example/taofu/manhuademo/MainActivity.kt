package com.example.taofu.manhuademo

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.app.Activity
import android.content.Context
import android.graphics.*
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.widget.ImageButton
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var mTvSynopsis: TextView

    private lateinit var mStringOriginContent: String
    private lateinit var mStringCollapse: String
    private lateinit var mContentCollapseSpannableString: SpannableString
    private lateinit var mContentExpandSpannableString: SpannableString
    private lateinit var mContentSpannableString: SpannableString

    private lateinit var mBtnExpand: ImageButton

    private var expand = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        //setContentView(R.layout.tex)


        /*mTvSynopsis = findViewById(R.id.reader_book_cover_synopsis_content)

        mStringOriginContent = getString(R.string.rader_book_cover_synopsis_content)


        mTvSynopsis.post({ setSynopsis(mTvSynopsis, mStringOriginContent,mStringOriginContent, maxCount = 3) })


        mBtnExpand = findViewById(R.id.reader_book_cover_btn_expand)

        mTvSynopsis.highlightColor = Color.TRANSPARENT


        mBtnExpand.setOnClickListener {

            if (!expand) {
                expand = true
                mTvSynopsis.text = mStringOriginContent
            } else {
                mTvSynopsis.text = mStringCollapse
                expand = false
            }
        }

        mBtnExpand.visibility = View.GONE*/

                val fragmentTransaction = supportFragmentManager.beginTransaction()

                fragmentTransaction.add(android.R.id.content,FragmentBookCover())
                fragmentTransaction.commit()


        /* val toolbar = findViewById<Toolbar>(R.id.tool_bar)
         val p = toolbar.layoutParams as FrameLayout.LayoutParams

         p.topMargin = getStatusBarHeight(this)



         reader_book_cover_tab.setOnCheckedChangeListener{group, checkedId ->

             if(checkedId == R.id.reader_book_cover_tab_catalog){
                 reader_book_cover_tv_details.visibility = View.GONE
             }else{
                 reader_book_cover_tv_details.visibility = View.VISIBLE
             }
             Toast.makeText(this,"" +ddddd.elevation,1).show()
         }


         setSupportActionBar(findViewById(R.id.tool_bar))*/
        transparencyBar(this)


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

                setSynopsis(view, originContent + "  ", originContent, maxCount =  if (mTvSynopsis.isSelected) Int.MAX_VALUE else 3 )

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
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.reader_book_cover_expand)
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

    override fun onResume() {
        super.onResume()

        //setSynopsis(mTvSynopsis,mStringOriginContent,maxCount = 3)
    }

    fun transparencyBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val window = activity.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    fun toggleHideyBar() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        val uiOptions = getWindow().getDecorView().getSystemUiVisibility()
        var newUiOptions = uiOptions
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        val isImmersiveModeEnabled = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY == uiOptions


        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions = newUiOptions xor View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions)
        //END_INCLUDE (set_ui_flags)
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
