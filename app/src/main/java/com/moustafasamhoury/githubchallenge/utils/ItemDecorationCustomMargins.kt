package com.moustafasamhoury.githubchallenge.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * @author moustafasamhoury
 * created on Monday, 13 May, 2019
 */

/**
 * @param start start margin value in dip
 * @param top top margin value in dip
 * @param end end margin value in dip
 * @param bottom bottom margin value in dip
 *
 *<p>
 * Created Margin around the Recyclerview row
 *
 * <b>Values passed will be considered as dip unit</b>
 * </p>
 */
class ItemDecorationCustomMargins(
    private val start: Int = 0,
    private val top: Int = 0,
    private val end: Int = 0,
    private val bottom: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (isLeftToRight()) {
            outRect.set(
                view.context.dip(start),
                view.context.dip(top),
                view.context.dip(end),
                view.context.dip(bottom)
            )
        } else {
            outRect.set(
                view.context.dip(end),
                view.context.dip(top),
                view.context.dip(start),
                view.context.dip(bottom)
            )
        }
    }

}

internal fun isLeftToRight() =
    TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_LTR

internal fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
