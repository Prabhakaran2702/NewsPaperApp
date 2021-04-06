package com.prabhakaran.khaleejtimes_.data.repository

import com.prabhakaran.khaleejtimes_.data.local.db.BookmarkDAO
import com.prabhakaran.khaleejtimes_.model.Bookmarks

class BookmarksRepository(private val dao: BookmarkDAO){

    val bookmarks = dao.getAllBookmarks()

    suspend fun insertArticleIntoBookmarks(bookmark: Bookmarks){
        dao.insertArticle(bookmark)
    }

    suspend fun deleteArticlefromBookmarks(bookmark: Bookmarks){
        dao.deleteArticle(bookmark)
    }

    suspend fun deleteALlArticleFromBookmarks(){
        dao.deleteAll()
    }
}