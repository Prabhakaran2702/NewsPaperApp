package com.prabhakaran.khaleejtimes_.ui.base

import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.prabhakaran.khaleejtimes_.R
import com.squareup.picasso.Picasso
import com.prabhakaran.khaleejtimes_.model.Article

abstract class BaseViewHolder<viewDataBinding : ViewDataBinding>(private val binding: viewDataBinding) :
    RecyclerView.ViewHolder(binding.root) {


    protected abstract val titleArticle: TextView
    protected abstract val publishedAt: TextView
    protected abstract val source: TextView
    protected abstract val image: ImageView
    protected abstract val viewLayout: CardView


    fun bind(
        article: Article,
        clickListener: (Article, ImageView) -> Unit,
        longClickListener: (Article) -> Unit
    ) {

        if (article.urlToImage.isNullOrEmpty()) {
            image.setImageResource(R.drawable.khaleej_times)
        } else {

            Picasso.get()
                .load(article.urlToImage)
                .fit()
                 .placeholder(R.drawable.images)
                .centerCrop()
                .into(image)
        }

        titleArticle.text = article.title
        publishedAt.text =
            "${article.publishedAt!!.substring(0, article.publishedAt!!.indexOf('T'))}"
        source.text = article.source.name


        viewLayout.setOnClickListener {
            clickListener(article, image)
        }

        viewLayout.setOnLongClickListener {
            longClickListener(article)
            true
        }

    }
}