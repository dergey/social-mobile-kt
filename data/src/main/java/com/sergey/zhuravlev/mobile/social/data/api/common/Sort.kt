package com.sergey.zhuravlev.mobile.social.data.api.common

import com.sergey.zhuravlev.mobile.social.data.api.enums.Direction

class Sort private constructor(private val sortBy: String, private val direction: Direction) {

  override fun toString(): String {
    return sortBy + "," + direction.name
  }

  companion object {
    fun desc(sortBy: String): Sort {
      return Sort(sortBy, Direction.DESC)
    }

    fun asc(sortBy: String): Sort {
      return Sort(sortBy, Direction.ASC)
    }
  }
}