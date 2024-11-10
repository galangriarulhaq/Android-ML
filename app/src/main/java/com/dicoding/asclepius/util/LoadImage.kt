package com.dicoding.asclepius.util

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

class LoadImage {

    companion object {

        fun load(
            context: Context,
            imageView: ImageView?,
            imageUrl: String,
            placeholder: Int,
            isCircle: Boolean = false
        ) {

            val placeholderDrawable = ColorDrawable(ContextCompat.getColor(context, placeholder))
            val picassoBuilder = Picasso.get()
                .load(imageUrl)
                .placeholder(placeholderDrawable)
                .error(placeholderDrawable)
                .fit()
                .centerCrop()

            if (isCircle) {
                picassoBuilder.transform(CropCircleTransformation())
            } else {
                picassoBuilder.transform(RoundedCornersTransformation(12, 0))
            }

            picassoBuilder.into(imageView)

        }

    }

}