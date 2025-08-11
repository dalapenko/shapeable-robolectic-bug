package tech.dalapenko.shapeablebug

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.core.content.res.getStringOrThrow
import androidx.core.content.withStyledAttributes
import tech.dalapenko.shapeablebug.databinding.AlphaTagViewBinding

class AlphaTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val tagViewBinding =
        AlphaTagViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        context.withStyledAttributes(attrs, R.styleable.TagView, defStyleAttr) {
            runCatching {
                getStringOrThrow(R.styleable.TagView_android_text)
            }.getOrNull()?.let(::setText)
        }
    }

    fun setText(text: CharSequence?) {
        tagViewBinding.textValue.text = text
    }
}