package com.jeff.kotlindialogs.widget

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.renderscript.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import com.jeff.kotlindialogs.R
import com.jeff.kotlindialogs.utils.CrashHandler
import java.io.InterruptedIOException


/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-19.
 * description ：
 */

class BlurView : View {

    private var mDownsampleFactor: Float = 0.toFloat() // default 4
    private var mOverlayColor: Int = 0 // default #aaffffff
    private var mBlurRadius: Float = 0.toFloat() // default 10dp (0 < r <= 25)

    private var mDirty: Boolean = false
    private var mBitmapToBlur: Bitmap? = null
    private var mBlurredBitmap: Bitmap? = null


    private var mBlurringCanvas: Canvas? = null
    private var mRenderScript: RenderScript? = null
    private var mBlurScript: ScriptIntrinsicBlur? = null
    private var mBlurInput: Allocation? = null
    private var mBlurOutput: Allocation? = null
    private var mIsRendering: Boolean = false
    private val mRectSrc = Rect()
    private val mRectDst = Rect()
    // mDecorView should be the root view of the activity (even if you are on a different window like a dialog)
    private var mDecorView: View? = null
    // If the view is on different root view (usually means we are on a PopupWindow),
    // we need to manually call invalidate() in onPreDraw(), otherwise we will not be able to see the changes
    private var mDifferentRoot: Boolean = false
    private var RENDERING_COUNT: Int = 0

    private val mPaint: Paint
    private val mRectF: RectF
    private var mXRadius: Float = 0.toFloat()
    private var mYRadius: Float = 0.toFloat()

    private var mRoundBitmap: Bitmap? = null
    private var mTmpCanvas: Canvas? = null

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RealtimeBlurView)
        mBlurRadius = a.getDimension(
            R.styleable.RealtimeBlurView_realtimeBlurRadius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28f, context.resources.displayMetrics)
        )
        mDownsampleFactor = a.getFloat(R.styleable.RealtimeBlurView_realtimeDownsampleFactor, 4f)
        mOverlayColor = a.getColor(R.styleable.RealtimeBlurView_realtimeOverlayColor, 0x00ffffff)

        //ready rounded corner
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mRectF = RectF()

        mXRadius = a.getDimension(
            R.styleable.RealtimeBlurView_xRadius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, context.resources.displayMetrics)
        )
        mYRadius = a.getDimension(
            R.styleable.RealtimeBlurView_yRadius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, context.resources.displayMetrics)
        )
        a.recycle()
    }

    fun setBlurRadius(radius: Float) {
        if (mBlurRadius != radius) {
            mBlurRadius = radius
            mDirty = true
            invalidate()
        }
    }

    fun setRadius(context: Context, x: Float, y: Float) {
        if (mXRadius != x || mYRadius != y) {
            mXRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, x, context.resources.displayMetrics)
            mYRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, y, context.resources.displayMetrics)
            mDirty = true
            invalidate()
        }
    }

    fun setDownsampleFactor(factor: Float) {
        if (factor <= 0) {
            throw IllegalArgumentException("Downsample factor must be greater than 0.")
        }

        if (mDownsampleFactor != factor) {
            mDownsampleFactor = factor
            mDirty = true // may also change blur radius
            releaseBitmap()
            invalidate()
        }
    }

    fun setOverlayColor(color: Int) {
        if (mOverlayColor != color) {
            mOverlayColor = color
            invalidate()
        }
    }

    private fun releaseBitmap() {
        if (mBlurInput != null) {
            mBlurInput!!.destroy()
            mBlurInput = null
        }
        if (mBlurOutput != null) {
            mBlurOutput!!.destroy()
            mBlurOutput = null
        }
        if (mBitmapToBlur != null) {
            mBitmapToBlur!!.recycle()
            mBitmapToBlur = null
        }
        if (mBlurredBitmap != null) {
            mBlurredBitmap!!.recycle()
            mBlurredBitmap = null
        }
    }

    private fun releaseScript() {
        if (mRenderScript != null) {
            mRenderScript!!.destroy()
            mRenderScript = null
        }
        if (mBlurScript != null) {
            mBlurScript!!.destroy()
            mBlurScript = null
        }
    }

    protected fun release() {
        releaseBitmap()
        releaseScript()
    }

    protected fun prepare(): Boolean {
        if (mBlurRadius == 0f) {
            release()
            return false
        }

        var downsampleFactor = mDownsampleFactor

        if (mDirty || mRenderScript == null) {
            if (mRenderScript == null) {
                try {
                    mRenderScript = RenderScript.create(context)
                    mBlurScript = ScriptIntrinsicBlur.create(mRenderScript, Element.U8_4(mRenderScript))
                } catch (e: java.lang.Exception) {
                    if (isDebug(context)) {
                        if (e.message != null && e.message!!.startsWith("Error loading RS jni library: java.lang.UnsatisfiedLinkError:")) {
                            throw RuntimeException("Error loading RS jni library, Upgrade buildToolsVersion=\"24.0.2\" or higher may solve this issue")
                        } else {
                            throw e
                        }
                    } else {
                        // In release mode, just ignore
                        releaseScript()
                        return false
                    }
                }

            }

            mDirty = false
            var radius = mBlurRadius / downsampleFactor
            if (radius > 25) {
                downsampleFactor = downsampleFactor * radius / 25
                radius = 25f
            }
            mBlurScript!!.setRadius(radius)
        }

        val width = width
        val height = height

        val scaledWidth = Math.max(1, (width / downsampleFactor).toInt())
        val scaledHeight = Math.max(1, (height / downsampleFactor).toInt())

        if (mBlurringCanvas == null || mBlurredBitmap == null
            || mBlurredBitmap!!.getWidth() != scaledWidth
            || mBlurredBitmap!!.getHeight() != scaledHeight
        ) {
            releaseBitmap()

            var r = false
            try {
                mBitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (mBitmapToBlur == null) {
                    return false
                }
                mBlurringCanvas = Canvas(mBitmapToBlur)

                mBlurInput = Allocation.createFromBitmap(
                    mRenderScript, mBitmapToBlur,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT
                )
                mBlurOutput = Allocation.createTyped(mRenderScript, mBlurInput!!.getType())

                mBlurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (mBlurredBitmap == null) {
                    return false
                }

                r = true
            } catch (e: OutOfMemoryError) {
                // Bitmap.createBitmap() may cause OOM error
                // Simply ignore and fallback
            } finally {
                if (!r) {
                    releaseBitmap()
                    return false
                }
            }
        }
        return true
    }

    protected fun blur(bitmapToBlur: Bitmap, blurredBitmap: Bitmap) {
        mBlurInput!!.copyFrom(bitmapToBlur)
        mBlurScript!!.setInput(mBlurInput)
        mBlurScript!!.forEach(mBlurOutput)
        mBlurOutput!!.copyTo(blurredBitmap)
    }

    private val preDrawListener = ViewTreeObserver.OnPreDrawListener {
        val locations = IntArray(2)
        var oldBmp: Bitmap? = mBlurredBitmap
        val decor = mDecorView
        if (decor != null && isShown && prepare()) {
            val redrawBitmap = mBlurredBitmap != oldBmp
            oldBmp = null
            decor.getLocationOnScreen(locations)
            var x = -locations[0]
            var y = -locations[1]

            getLocationOnScreen(locations)
            x += locations[0]
            y += locations[1]

            // just erase transparent
            mBitmapToBlur!!.eraseColor(mOverlayColor and 0xffffff)

            val rc = mBlurringCanvas!!.save()
            mIsRendering = true
            RENDERING_COUNT++
            try {
                mBlurringCanvas!!.scale(1f * mBitmapToBlur!!.getWidth() / width, 1f * mBitmapToBlur!!.getHeight() / height)
                mBlurringCanvas!!.translate((-x).toFloat(), (-y).toFloat())
                if (decor.background != null) {
                    decor.background.draw(mBlurringCanvas)
                }
                decor.draw(mBlurringCanvas)
            } catch (e: StopException) {
            } finally {
                mIsRendering = false
                RENDERING_COUNT--
                mBlurringCanvas!!.restoreToCount(rc)
            }

            blur(mBitmapToBlur!!, mBlurredBitmap!!)

            if (redrawBitmap || mDifferentRoot) {
                invalidate()
            }
        }

        true
    }

    protected fun getActivityDecorView(): View? {
        var ctx: Context? = context
        var i = 0
        while (i < 4 && ctx != null && ctx !is Activity && ctx is ContextWrapper) {
            ctx = ctx.baseContext
            i++
        }
        return if (ctx is Activity) {
            ctx.window.decorView
        } else {
            null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mDecorView = getActivityDecorView()
        if (mDecorView != null) {
            mDecorView!!.getViewTreeObserver().addOnPreDrawListener(preDrawListener)
            mDifferentRoot = mDecorView!!.getRootView() !== rootView
            if (mDifferentRoot) {
                mDecorView!!.postInvalidate()
            }
        } else {
            mDifferentRoot = false
        }
    }

    override fun onDetachedFromWindow() {
        if (mDecorView != null) {
            mDecorView!!.getViewTreeObserver().removeOnPreDrawListener(preDrawListener)
        }
        release()
        super.onDetachedFromWindow()
    }

    override fun draw(canvas: Canvas) {
        if (mIsRendering) {
            // Quit here, don't draw views above me
            throw STOP_EXCEPTION
        } else if (RENDERING_COUNT > 0) {
            // Doesn't support blurview overlap on another blurview
        } else {
            super.draw(canvas)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBlurredBitmap(canvas, mBlurredBitmap, mOverlayColor)
    }

    /**
     * Custom draw the blurred bitmap and color to define your own shape
     *
     * @param canvas
     * @param blurredBitmap
     * @param overlayColor
     */
    protected fun drawBlurredBitmap(canvas: Canvas, blurredBitmap: Bitmap?, overlayColor: Int) {
        if (blurredBitmap != null) {
            mRectSrc.right = blurredBitmap.width
            mRectSrc.bottom = blurredBitmap.height
            mRectDst.right = width
            mRectDst.bottom = height
            canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst, null)
        }
        canvas.drawColor(overlayColor)
        //Rounded corner
        mRectF.right = width.toFloat()
        mRectF.bottom = height.toFloat()
        if (mRoundBitmap == null) {
            mRoundBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        if (mTmpCanvas == null) {
            mTmpCanvas = Canvas(mRoundBitmap)
        }
        mTmpCanvas!!.drawRoundRect(mRectF, mXRadius, mYRadius, mPaint)

        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        canvas.drawBitmap(mRoundBitmap, 0f, 0f, mPaint)
    }

    //添加异常抛出类对象
    class StopException : RuntimeException() {
        init {
            try {
                BlurView::class.java.classLoader.loadClass("android.renderscript.RenderScript")
            } catch (e: Exception) {
                throw java.lang.RuntimeException(
                    "\n错误！\nRenderScript支持库未启用，要启用模糊效果，请在您的app的Gradle配置文件中添加以下语句：" +
                            "\nandroid { \n...\n  defaultConfig { \n    ...\n    renderscriptTargetApi 19 \n    renderscriptSupportModeEnabled true \n  }\n}"
                )
            }

        }
    }

    companion object {
        val STOP_EXCEPTION = StopException()
        var DEBUG: Boolean? = null
        fun isDebug(ctx: Context?): Boolean {
            if (DEBUG == null && ctx != null) {
                DEBUG = ctx.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
            }
            return DEBUG === java.lang.Boolean.TRUE
        }
    }

}



