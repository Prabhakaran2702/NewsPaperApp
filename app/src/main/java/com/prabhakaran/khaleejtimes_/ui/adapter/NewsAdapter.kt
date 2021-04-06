package com.prabhakaran.khaleejtimes_.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.prabhakaran.khaleejtimes_.R
import com.prabhakaran.khaleejtimes_.databinding.ItemArticleViewtypeListBinding
import com.prabhakaran.khaleejtimes_.databinding.ItemArticleViewtypeTabBinding
import com.prabhakaran.khaleejtimes_.model.Article
import com.prabhakaran.khaleejtimes_.ui.base.BaseNewsAdapter
import com.prabhakaran.khaleejtimes_.ui.base.BaseViewHolder


class NewsListAdapter(
    private val clickListener: (Article, ImageView) -> Unit,
    private val longClickListener: (Article) -> Unit
) : BaseNewsAdapter<ItemArticleViewtypeListBinding>(clickListener, longClickListener) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemArticleViewtypeListBinding>(
            inflater,
            R.layout.item_article_viewtype_list,
            parent,
            false
        )
        return NewsListViewHolder(binding)
    }
}


class NewsListViewHolder(private val binding: ItemArticleViewtypeListBinding) :
    BaseViewHolder<ItemArticleViewtypeListBinding>(binding) {

    override val titleArticle: TextView
        get() = binding.titleArticle
    override val publishedAt: TextView
        get() = binding.publishedAt
    override val source: TextView
        get() = binding.source
    override val image: ImageView
        get() = binding.image
    override val viewLayout: CardView
        get() = binding.linLayout


}

class NewsTabAdapter(
    private val clickListener: (Article, ImageView) -> Unit,
    private val longClickListener: (Article) -> Unit
) :
    BaseNewsAdapter<ItemArticleViewtypeTabBinding>(clickListener, longClickListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsTabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemArticleViewtypeTabBinding>(
            inflater,
            R.layout.item_article_viewtype_tab,
            parent,
            false
        )
        return NewsTabViewHolder(binding)
    }
}


class NewsTabViewHolder(
    private val binding: ItemArticleViewtypeTabBinding
) :
    BaseViewHolder<ItemArticleViewtypeTabBinding>(binding) {

    override val titleArticle: TextView
        get() = binding.titleArticle
    override val publishedAt: TextView
        get() = binding.publishedAt
    override val source: TextView
        get() = binding.source
    override val image: ImageView
        get() = binding.image
    override val viewLayout: CardView
        get() = binding.linLayout
}





