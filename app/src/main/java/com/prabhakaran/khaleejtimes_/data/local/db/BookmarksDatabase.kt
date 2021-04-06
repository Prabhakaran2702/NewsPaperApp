package com.prabhakaran.khaleejtimes_.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prabhakaran.khaleejtimes_.model.ArticleCollections
import com.prabhakaran.khaleejtimes_.model.Bookmarks

@Database(entities = [Bookmarks::class,ArticleCollections::class], version = 3, exportSchema = false)
abstract class BookmarksDatabase :RoomDatabase(){

    abstract val daoBook: BookmarkDAO
    abstract val daoArticles: ArticleCollectionDAO

    companion object{
        @Volatile
        private var INSTANCE: BookmarksDatabase? = null

        fun getInstance(context: Context): BookmarksDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookmarksDatabase::class.java,
                        "Bookmarks"
                    ).fallbackToDestructiveMigration()
                        .build()

                }
                    return instance

            }

        }
    }
}