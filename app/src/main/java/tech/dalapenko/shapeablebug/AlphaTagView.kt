package tech.dalapenko.shapeablebug

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import tech.dalapenko.shapeablebug.databinding.AlphaTagViewBinding

class AlphaTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val tagViewBinding =
        AlphaTagViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setText(text: CharSequence?) {
        tagViewBinding.textValue.text = text
    }
}