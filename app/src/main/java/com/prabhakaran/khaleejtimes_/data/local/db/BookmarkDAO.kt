package com.prabhakaran.khaleejtimes_.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.prabhakaran.khaleejtimes_.model.ArticleCollections
import com.prabhakaran.khaleejtimes_.model.Bookmarks

@Dao
interface BookmarkDAO {
    @Insert
    suspend fun insertArticle(bookmarks: Bookmarks): Long

    @Delete
    suspend fun deleteArticle(bookmarks: Bookmarks)

    @Query("SELECT * FROM Bookmarks ORDER BY  bookmark_id DESC")
    fun getAllBookmarks(): LiveData<List<Bookmarks>>

    @Query("DELETE FROM Bookmarks")
    suspend fun deleteAll()

//    @Insert
//    suspend fun insertArticleCollections(collections: ArticleCollections): Long
//
//    @Delete
//    suspend fun deleteArticleCollections(collections : ArticleCollections)
//
//    @Query("SELECT * FROM ArticleCollections ORDER BY  category DESC")
//    fun getAllArticleCollections(): LiveData<List<ArticleCollections>>
//
//    @Query("DELETE FROM ArticleCollections")
//    suspend fun deleteAllArticleCollections()



}