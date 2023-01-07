package com.sergey.zhuravlev.mobile.social.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.sergey.zhuravlev.mobile.social.data.database.entity.ProfileDetailEntity

@Dao
interface ProfileDetailModelDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(profileDetailEntity: ProfileDetailEntity)

}