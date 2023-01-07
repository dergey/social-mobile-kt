package com.sergey.zhuravlev.mobile.social.data.database.dao

import androidx.room.*
import com.sergey.zhuravlev.mobile.social.data.database.entity.ProfileAndDetailEntities
import com.sergey.zhuravlev.mobile.social.data.database.entity.ProfileEntity

@Dao
interface ProfileModelDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(profileEntity: ProfileEntity)

  @Query("SELECT * FROM profiles WHERE username = :username")
  fun getOneByUsername(username: String): ProfileEntity?

  @Query("SELECT * FROM profiles INNER JOIN profile_details ON profiles.username = profile_details.username WHERE profiles.attitude = 'YOU'")
  @Transaction
  fun getCurrentWithDetail(): ProfileAndDetailEntities?

}