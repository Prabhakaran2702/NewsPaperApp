package com.prabhakaran.khaleejtimes_.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ArticleCollections")
data class ArticleCollections(


    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "collection_id")
    val id: Int,

    @ColumnInfo(name = "category")
    val category: String,

    @Embedded val article: Article?
)