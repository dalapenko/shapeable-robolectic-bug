package tech.dalapenko.shapeablebug

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.content.withStyledAttributes
import tech.dalapenko.shapeablebug.databinding.CoverViewBinding

class CoverView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val coverViewBinding =
        CoverViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.CoverView, defStyleAttr) {
            try {
                getResourceIdOrThrow(R.styleable.CoverView_coverImage)
            } catch (e:  IllegalArgumentException) {
                null
            }?.let(::setImage)
        }
    }

    fun setImage(@DrawableRes drawable: Int) {
        coverViewBinding.sampleContent.setImageResource(drawable)
    }
}