package com.sergey.zhuravlev.mobile.social.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.sergey.zhuravlev.mobile.social.data.database.entity.ChatAndLastMessageEntities
import com.sergey.zhuravlev.mobile.social.data.database.entity.ChatEntity

@Dao
interface ChatModelDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(chatEntity: ChatEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(chatEntities: List<ChatEntity>)

  @Query("SELECT * FROM chats WHERE id = :chatId")
  suspend fun getOneById(chatId: Long): ChatEntity?

  @Query("SELECT * FROM chats WHERE profile_username = :username")
  suspend fun getOneByUsername(username: String): ChatEntity?

  @Transaction
  @Query("SELECT * FROM chats WHERE id = :newChatId")
  suspend fun getOneChatAndLastMessageModelById(newChatId: Long): ChatAndLastMessageEntities?

  @Transaction
  @Query("SELECT * FROM chats WHERE id IN (:newChatIds)")
  suspend fun getAllChatAndLastMessageModelByIds(newChatIds: List<Long>): List<ChatAndLastMessageEntities>

  @Query("SELECT * FROM chats INNER JOIN messages ON chats.last_message_id = messages.id ORDER BY messages.create_at DESC")
  fun getAllChatAndLastMessageModel(): PagingSource<Int, ChatAndLastMessageEntities>

  @Query("SELECT max(chats.pageable_page) FROM chats")
  suspend fun getLastPage(): Int?

  @Query("UPDATE chats SET pageable_page = NULL WHERE pageable_page >= :page")
  suspend fun resetPageableAfterPageMessageModel(page: Int)

}