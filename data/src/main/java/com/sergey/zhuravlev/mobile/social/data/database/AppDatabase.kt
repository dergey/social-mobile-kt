package com.sergey.zhuravlev.mobile.social.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sergey.zhuravlev.mobile.social.data.database.converter.LocalDateTimeConverter
import com.sergey.zhuravlev.mobile.social.data.database.dao.ChatModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.MessageModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.ProfileDetailModelDao
import com.sergey.zhuravlev.mobile.social.data.database.dao.ProfileModelDao
import com.sergey.zhuravlev.mobile.social.data.database.entity.*

@Database(
  version = 1,
  entities = [
    ChatEntity::class,
    MessageEntity::class,
    ProfileEntity::class,
    ProfileDetailEntity::class,
    FriendEntity::class
  ],
  exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {

  abstract fun chatModelDao(): ChatModelDao
  abstract fun messageModelDao(): MessageModelDao
  abstract fun profileModelDao(): ProfileModelDao
  abstract fun profileDetailModelDao(): ProfileDetailModelDao

  companion object {

    private const val SOCIAL_DB = "social.db"

    fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        SOCIAL_DB
      )
        .fallbackToDestructiveMigration()
        .build()
    }
  }
}