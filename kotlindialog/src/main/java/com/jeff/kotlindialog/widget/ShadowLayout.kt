package com.jeff.kotlindialog.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.support.annotation.FloatRange
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.jeff.kotlindialog.R


/**
 * author : Jeff  5899859876@qq.com
 * Github: https://github.com/Jay-YaoJie
 * Created :  2018-11-19.
 * description ：
 * Github: https://github.com/Devlight/ShadowLayout
 */
class ShadowLayout : FrameLayout {
    companion object {
        // Default shadow values
        private const val DEFAULT_SHADOW_RADIUS: Float = 30.0f
        private const val DEFAULT_SHADOW_DISTANCE: Float = 15.0f
        private const val DEFAULT_SHADOW_ANGLE: Float = 45.0f
        private const val DEFAULT_SHADOW_COLOR = Color.DKGRAY

        // Shadow bounds values
        private const val MAX_ALPHA = 255
        private const val MAX_ANGLE = 360.0f
        private const val MIN_RADIUS: Float = 0.1f
        private const val MIN_ANGLE: Float = 0.0f
    }

    // Shadow paint
    private val mPaint = object : Paint(Paint.ANTI_ALIAS_FLAG) {
        init {
            isDither = true
            isFilterBitmap = true
        }
    }
    // Shadow bitmap and canvas
    private var mBitmap: Bitmap? = null
    private val mCanvas:Canvas= Canvas()
    // View bounds
    private val mBounds = Rect()
    // Check whether need to redraw shadow
    private var mInvalidateShadow = true

    // Detect if shadow is visible
    private var mIsShadowed: Boolean = false

    // Shadow variables
    private var mShadowColor: Int = 0
    private var mShadowAlpha: Int = 0
    private var mShadowRadius: Float = 0.toFloat()
    private var mShadowDistance: Float = 0.toFloat()
    private var mShadowAngle: Float = 0.toFloat()
    private var mShadowDx: Float = 0.toFloat()
    private var mShadowDy: Float = 0.toFloat()
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, mPaint)

        // Retrieve attributes from xml
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout)
        try {
            setIsShadowed(typedArray.getBoolean(R.styleable.ShadowLayout_sl_shadowed, true))
            setShadowRadius(
                typedArray.getDimension(
                    R.styleable.ShadowLayout_sl_shadow_radius, DEFAULT_SHADOW_RADIUS
                )
            )
            setShadowDistance(
                typedArray.getDimension(
                    R.styleable.ShadowLayout_sl_shadow_distance, DEFAULT_SHADOW_DISTANCE
                )
            )
            setShadowAngle(
                typedArray.getInteger(
                    R.styleable.ShadowLayout_sl_shadow_angle, DEFAULT_SHADOW_ANGLE.toInt()
                ).toFloat()
            )
            setShadowColor(
                typedArray.getColor(
                    R.styleable.ShadowLayout_sl_shadow_color, DEFAULT_SHADOW_COLOR
                )
            )
        } finally {
            typedArray.recycle()
        }
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        // Clear shadow bitmap
        if (mBitmap != null) {
            mBitmap!!.recycle()
            mBitmap = null
        }
    }

    fun isShadowed(): Boolean {
        return mIsShadowed
    }

    fun setIsShadowed(isShadowed: Boolean) {
        mIsShadowed = isShadowed
        postInvalidate()
    }

    fun getShadowDistance(): Float {
        return mShadowDistance
    }

    fun setShadowDistance(shadowDistance: Float) {
        mShadowDistance = shadowDistance
        resetShadow()
    }

    fun getShadowAngle(): Float {
        return mShadowAngle
    }

    @SuppressLint("SupportAnnotationUsage")
    @FloatRange
    fun setShadowAngle(@FloatRange(from = MIN_ANGLE.toDouble(), to = MAX_ANGLE.toDouble()) shadowAngle: Float) {
        mShadowAngle = Math.max(MIN_ANGLE, Math.min(shadowAngle, MAX_ANGLE))
        resetShadow()
    }

    fun getShadowRadius(): Float {
        return mShadowRadius
    }

    fun setShadowRadius(shadowRadius: Float) {
        mShadowRadius = Math.max(MIN_RADIUS, shadowRadius)

        if (isInEditMode) return
        // Set blur filter to paint
        mPaint.maskFilter = BlurMaskFilter(mShadowRadius, BlurMaskFilter.Blur.NORMAL)
        resetShadow()
    }

    fun getShadowColor(): Int {
        return mShadowColor
    }

    fun setShadowColor(shadowColor: Int) {
        mShadowColor = shadowColor
        mShadowAlpha = Color.alpha(shadowColor)

        resetShadow()
    }

    fun getShadowDx(): Float {
        return mShadowDx
    }

    fun getShadowDy(): Float {
        return mShadowDy
    }

    // Reset shadow layer
    private fun resetShadow() {
        // Detect shadow axis offset
        mShadowDx = (mShadowDistance * Math.cos(mShadowAngle / 180.0f * Math.PI)).toFloat()
        mShadowDy = (mShadowDistance * Math.sin(mShadowAngle / 180.0f * Math.PI)).toFloat()

        // Set padding for shadow bitmap
        val padding = (mShadowDistance + mShadowRadius).toInt()
        setPadding(padding, padding, padding, padding)
        requestLayout()
    }

    private fun adjustShadowAlpha(adjust: Boolean): Int {
        return Color.argb(
            if (adjust) MAX_ALPHA else mShadowAlpha,
            Color.red(mShadowColor),
            Color.green(mShadowColor),
            Color.blue(mShadowColor)
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Set ShadowLayout bounds
        mBounds.set(0, 0, measuredWidth, measuredHeight)
    }

    override fun requestLayout() {
        // Redraw shadow
        mInvalidateShadow = true
        super.requestLayout()
    }

    override fun dispatchDraw(canvas: Canvas) {
        // If is not shadowed, skip
        if (mIsShadowed) {
            // If need to redraw shadow
            if (mInvalidateShadow) {
                // If bounds is zero
                if (mBounds.width() != 0 && mBounds.height() != 0) {
                    // Reset bitmap to bounds
                    mBitmap = Bitmap.createBitmap(
                        mBounds.width(), mBounds.height(), Bitmap.Config.ARGB_8888
                    )
                    // Canvas reset
                    mCanvas.setBitmap(mBitmap)

                    // We just redraw
                    mInvalidateShadow = false
                    // Main feature of this lib. We create the local copy of all content, so now
                    // we can draw bitmap as a bottom layer of natural canvas.
                    // We draw shadow like blur effect on bitmap, cause of setShadowLayer() method of
                    // paint does`t draw shadow, it draw another copy of bitmap
                    super.dispatchDraw(mCanvas)

                    // Get the alpha bounds of bitmap
                    val extractedAlpha = mBitmap!!.extractAlpha()
                    // Clear past content content to draw shadow
                    mCanvas.drawColor(0, PorterDuff.Mode.CLEAR)

                    // Draw extracted alpha bounds of our local canvas
                    mPaint.color = adjustShadowAlpha(false)
                    mCanvas.drawBitmap(extractedAlpha, mShadowDx, mShadowDy, mPaint)

                    // Recycle and clear extracted alpha
                    extractedAlpha.recycle()
                } else {
                    // Create placeholder bitmap when size is zero and wait until new size coming up
                    mBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
                }
            }

            // Reset alpha to draw child with full alpha
            mPaint.color = adjustShadowAlpha(true)
            // Draw shadow bitmap
            if (!mBitmap!!.isRecycled()){
                canvas.drawBitmap(mBitmap!!, 0.0f, 0.0f, mPaint)
            }


        }

        // Draw child`s
        super.dispatchDraw(canvas)
    }

}