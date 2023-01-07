package com.sergey.zhuravlev.mobile.social.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sergey.zhuravlev.mobile.social.data.api.common.SocialResponse
import com.sergey.zhuravlev.mobile.social.data.api.dto.PageDto
import com.sergey.zhuravlev.mobile.social.data.mapper.transform.toException

class CommonPagingSource<V : Any>(
  private val pageSize: Int = DEFAULT_PAGE_SIZE,
  private val payload: Map<String, Any?>,
  private val pageableCall: suspend (load: PageableLoad) -> SocialResponse<PageDto<V>>
) : PagingSource<Int, V>() {

  data class PageableLoad(
    val pageSize: Int,
    val pageNumber: Int,
    private val staticPayload: Map<String, Any?>
  ) {
    fun <T> getPayload(name: String): T? {
      return staticPayload[name] as T?
    }

    fun getPayloadInt(name: String): Int? {
      return staticPayload[name] as Int?
    }

    fun getPayloadString(name: String): String? {
      return staticPayload[name] as String?
    }

    fun requirePayloadString(name: String): String {
      return staticPayload[name] as String?
        ?: throw IllegalArgumentException("Payload argument `$name` not provided")
    }
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
    val pageNumber = params.key ?: DEFAULT_PAGE_INDEX

    val response = pageableCall(PageableLoad(pageSize, pageNumber, payload))

    response.onSuccess { page ->
      val previousPage: Int? = if (page.number - 1 < 0) null else page.number - 1
      val nextPage: Int? = if (page.number + 1 >= page.totalPages) null else page.number + 1
      val elementsBefore: Int = page.number * page.size
      val elementsAfter: Int = page.totalElements - elementsBefore - page.content.size

      return LoadResult.Page(
        data = page.content,
        prevKey = previousPage,
        nextKey = nextPage,
        itemsAfter = elementsAfter,
        itemsBefore = elementsBefore,
      )
    }

    return response.errorData?.let {
      LoadResult.Error(it.toException())
    } ?: throw IllegalStateException("impossible")
  }

  override fun getRefreshKey(state: PagingState<Int, V>): Int? {
    return state.anchorPosition?.let {
      state.closestPageToPosition(it)?.prevKey?.plus(1)
        ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
    }
  }

  companion object {
    const val DEFAULT_PAGE_SIZE: Int = 20
    const val DEFAULT_PAGE_INDEX: Int = 0
  }
}