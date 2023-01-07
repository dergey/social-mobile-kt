package com.sergey.zhuravlev.mobile.social.data.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.sergey.zhuravlev.mobile.social.data.database.entity.MessageEntity

@Dao
interface MessageModelDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(messageEntity: MessageEntity): Long

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(messageEntities: List<MessageEntity>): LongArray

  @Query("SELECT * FROM messages WHERE id = :id")
  suspend fun getOneById(id: Long): MessageEntity?

  @Query("SELECT * FROM messages WHERE chat_id = :chatId AND sender = :messageSenderType")
  suspend fun getAllByChatIdAndMessageSenderType(
    chatId: Long,
    messageSenderType: String
  ): List<MessageEntity>

  @Query("SELECT * FROM messages WHERE chat_id = :chatId ORDER BY create_at DESC")
  fun getAllMessageModel(chatId: Long): PagingSource<Int, MessageEntity>

  @Query("UPDATE messages SET pageable_page = NULL WHERE chat_id = :chatId AND pageable_page >= :page")
  suspend fun resetPageableAfterPageMessageModel(chatId: Long, page: Int)

  @Query("SELECT * FROM messages WHERE network_id = :networkId")
  suspend fun getAllByNetworkId(networkId: Long): List<MessageEntity>

  @Query("SELECT * FROM messages WHERE network_id in (:networkIds)")
  suspend fun getAllByNetworkIds(networkIds: List<Long>): List<MessageEntity>

  @Query("DELETE FROM messages WHERE id = :id")
  suspend fun clear(id: Long)

  @Query("DELETE FROM messages WHERE id in (:ids)")
  suspend fun clearAll(ids: List<Long>)

  @Query("SELECT max(messages.pageable_page) FROM messages WHERE chat_id = :chatId")
  suspend fun getLastPage(chatId: Long): Int

}