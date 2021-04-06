package com.prabhakaran.khaleejtimes_.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.prabhakaran.khaleejtimes_.model.ArticleCollections

@Dao
interface ArticleCollectionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleCollections(collections: List<ArticleCollections>): List<Long>

    @Delete
    suspend fun deleteArticleCollections(collections : ArticleCollections)

    @Query("SELECT * FROM ArticleCollections WHERE category= :category")
    fun getAllArticleCollections(category: String): LiveData<List<ArticleCollections>>

    @Query("DELETE FROM ArticleCollections")
    suspend fun deleteAllArticleCollections()


}