package tech.dalapenko.shapeablebug

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import tech.dalapenko.shapeablebug.databinding.CoverWithTagViewBinding

class CoverWithTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        CoverWithTagViewBinding.inflate(LayoutInflater.from(context), this, true)
    }
}